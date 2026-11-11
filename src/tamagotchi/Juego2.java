package tamagotchi;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

// Juego 2: Space Shooter . Meteoritos. La idea base la saqué de un vídeo de YouTube,
// y me apoyé en la IA para estructurar bien los timers, la física de disparo y los power-ups.
public class Juego2 extends JDialog {

	private static final long serialVersionUID = 1L;

	// Recibe la ventana de juegos y el color elegido para mi mascota
	public Juego2(JFrame padre, Color colorMascota) {
		super(padre, "Juego 2 - Space Tamagotchi", true);
		this.setResizable(false);

		// Panel donde ocurre toda la acción del juego
		PanelJuego panelJuego = new PanelJuego(380, 480, colorMascota);
		this.add(panelJuego);
		this.pack();
		this.setLocationRelativeTo(padre);
	}

	// Constructores por defecto por si lo abro sin pasar parámetros
	public Juego2(JFrame padre) {
		this(padre, new Color(255, 193, 7));
	}

	public Juego2() {
		this(null, new Color(255, 193, 7));
	}

	// El panel donde se dibuja el espacio, mi nave, los meteoros y los disparos
	private class PanelJuego extends JPanel implements ActionListener {
		private static final long serialVersionUID = 1L;

		private final int ancho;
		private final int alto;
		private Color colorMascota;

		// Posición y tamaño de mi nave (el Tamagotchi espacial)
		private int naveX;
		private int naveY;
		private final int naveAncho = 34;
		private final int naveAlto = 30;
		private final int velocidadNave = 5;

		// Controles de movimiento lateral y el cooldown de disparo
		private boolean moverIzquierda = false;
		private boolean moverDerecha = false;
		private long ultimoDisparo = 0;

		// Sistema de Power-Ups (1 = Simple, 2 = Doble, 3 = Triple)
		private int nivelDisparo = 1; 
		private long tiempoFinPowerUp = 0;

		// Control de estados del juego y los puntos
		private boolean iniciado = false;
		private boolean pausado = false;
		private boolean gameOver = false;
		private int puntuacion = 0;

		// Los botones de la interfaz
		private JButton btnPausa;
		private JButton btnSalir;

		// Clases internas sencillas para los objetos que se mueven por el mapa
		private class Laser {
			int x, y;
			int ancho = 4;
			int alto = 12;
			int velocidad = 8;

			Laser(int x, int y) {
				this.x = x;
				this.y = y;
			}
		}

		private class Meteoro {
			int x, y;
			int tamano;
			int velocidad;

			Meteoro(int x, int y, int tamano, int velocidad) {
				this.x = x;
				this.y = y;
				this.tamano = tamano;
				this.velocidad = velocidad;
			}
		}

		private class PowerUp {
			int x, y;
			int tamano = 20;
			int velocidad = 2;

			PowerUp(int x, int y) {
				this.x = x;
				this.y = y;
			}
		}

		// Listas para manejar los disparos, meteoros y los bonus que caen
		private List<Laser> lasers;
		private List<Meteoro> meteoros;
		private List<PowerUp> powerUps;
		private Timer gameLoop;
		private Timer creadorMeteoros;

		public PanelJuego(int ancho, int alto, Color colorMascota) {
			this.ancho = ancho;
			this.alto = alto;
			// Aseguro un color amarillo por defecto si el objeto viene nulo
			this.colorMascota = (colorMascota != null) ? colorMascota : new Color(255, 193, 7);

			// Coloco mi nave centrada en la parte de abajo
			this.naveX = ancho / 2 - naveAncho / 2;
			this.naveY = alto - 60;

			this.setPreferredSize(new Dimension(ancho, alto));
			this.setBackground(new Color(15, 15, 30)); // Fondo espacio oscuro
			this.setLayout(null);
			this.setFocusable(true);

			lasers = new ArrayList<>();
			meteoros = new ArrayList<>();
			powerUps = new ArrayList<>();

			// --- Botón de Pausa ---
			btnPausa = new JButton("||");
			btnPausa.setFont(new Font("SansSerif", Font.BOLD, 12));
			btnPausa.setBounds(ancho - 105, 10, 45, 30);
			btnPausa.setMargin(new Insets(0, 0, 0, 0)); // Evito los 3 puntos por margen
			btnPausa.setFocusable(false); // Clave para que no me robe los controles del teclado
			btnPausa.setCursor(new Cursor(Cursor.HAND_CURSOR));
			btnPausa.setToolTipText("Pausar / Reanudar");

			btnPausa.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (iniciado && !gameOver) {
						pausado = !pausado;
						btnPausa.setText(pausado ? ">" : "||");
						repaint();
					}
				}
			});
			this.add(btnPausa);

			// --- Botón de Salir ---
			btnSalir = new JButton("X");
			btnSalir.setFont(new Font("SansSerif", Font.BOLD, 14));
			btnSalir.setBounds(ancho - 55, 10, 45, 30);
			btnSalir.setMargin(new Insets(0, 0, 0, 0));
			btnSalir.setFocusable(false); // Evita perder el foco del teclado al pulsar
			btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
			btnSalir.setToolTipText("Salir al menú de juegos");

			btnSalir.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Detengo los timers para no dejar tareas corriendo en segundo plano
					gameLoop.stop();
					creadorMeteoros.stop();
					Juego2.this.dispose();
				}
			});
			this.add(btnSalir);

			// Controles de teclado (ESPACIO solo para empezar o reiniciar)
			this.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					int key = e.getKeyCode();

					if (!iniciado && key == KeyEvent.VK_SPACE) {
						iniciado = true;
						return;
					}

					if (gameOver && key == KeyEvent.VK_SPACE) {
						reiniciarJuego();
						return;
					}

					// Movimiento lateral con flechas o A/D
					if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
						moverIzquierda = true;
					}
					if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
						moverDerecha = true;
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {
					int key = e.getKeyCode();
					if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
						moverIzquierda = false;
					}
					if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
						moverDerecha = false;
					}
				}
			});

			// Timer principal del juego (~60 FPS)
			gameLoop = new Timer(16, this);
			gameLoop.start();

			// Timer para hacer spawn de meteoros cada 900 ms
			creadorMeteoros = new Timer(900, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					generarMeteoro();
				}
			});
			creadorMeteoros.start();
		}

		// Método de disparo (automático con cadencia de 180 ms)
		private void disparar() {
			if (!iniciado || pausado || gameOver) return;

			long ahora = System.currentTimeMillis();
			if (ahora - ultimoDisparo > 180) {
				
				// Según mi nivel de power-up genero 1, 2 o 3 láseres a la vez
				if (nivelDisparo == 1) {
					lasers.add(new Laser(naveX + naveAncho / 2 - 2, naveY));
				} else if (nivelDisparo == 2) {
					lasers.add(new Laser(naveX + 4, naveY + 6));
					lasers.add(new Laser(naveX + naveAncho - 8, naveY + 6));
				} else {
					lasers.add(new Laser(naveX + 2, naveY + 8));
					lasers.add(new Laser(naveX + naveAncho / 2 - 2, naveY));
					lasers.add(new Laser(naveX + naveAncho - 6, naveY + 8));
				}

				ultimoDisparo = ahora;
			}
		}

		// Genero meteoros en posiciones X aleatorias en la parte superior
		private void generarMeteoro() {
			if (!iniciado || pausado || gameOver) return;

			int tamano = 24 + (int) (Math.random() * 20);
			int xRandom = (int) (Math.random() * (ancho - tamano));
			int vel = 2 + (int) (Math.random() * 3);

			meteoros.add(new Meteoro(xRandom, -tamano, tamano, vel));
		}

		// Reseteo todo para volver a jugar tras perder
		private void reiniciarJuego() {
			naveX = ancho / 2 - naveAncho / 2;
			lasers.clear();
			meteoros.clear();
			powerUps.clear();
			nivelDisparo = 1;
			tiempoFinPowerUp = 0;
			puntuacion = 0;
			gameOver = false;
			pausado = false;
			iniciado = true;
			btnPausa.setText("||");
		}

		// Lógica de física, movimiento y colisiones
		private void mover() {
			if (!iniciado || pausado || gameOver) return;

			// Disparo automático continuo en cada frame
			disparar();

			// Si se terminan los 8 segundos del Power-Up, vuelvo al disparo base
			if (nivelDisparo > 1 && System.currentTimeMillis() > tiempoFinPowerUp) {
				nivelDisparo = 1;
			}

			// Movimiento de la nave sin salir del marco
			if (moverIzquierda && naveX > 0) {
				naveX -= velocidadNave;
			}
			if (moverDerecha && naveX < ancho - naveAncho) {
				naveX += velocidadNave;
			}

			// Muevo los láseres hacia arriba y elimino los que salen de pantalla
			Iterator<Laser> itLaser = lasers.iterator();
			while (itLaser.hasNext()) {
				Laser l = itLaser.next();
				l.y -= l.velocidad;
				if (l.y < -10) {
					itLaser.remove();
				}
			}

			// Muevo los Power-Ups y detecto si la nave los recoge
			Iterator<PowerUp> itPower = powerUps.iterator();
			while (itPower.hasNext()) {
				PowerUp p = itPower.next();
				p.y += p.velocidad;

				if (naveX < p.x + p.tamano && naveX + naveAncho > p.x &&
					naveY < p.y + p.tamano && naveY + naveAlto > p.y) {
					
					if (nivelDisparo < 3) {
						nivelDisparo++;
					}
					tiempoFinPowerUp = System.currentTimeMillis() + 8000;
					itPower.remove();
					continue;
				}

				if (p.y > alto) {
					itPower.remove();
				}
			}

			// Muevo meteoros y compruebo colisiones
			Iterator<Meteoro> itMeteoro = meteoros.iterator();
			while (itMeteoro.hasNext()) {
				Meteoro m = itMeteoro.next();
				m.y += m.velocidad;

				// Choque contra la nave = Game Over
				if (naveX < m.x + m.tamano && naveX + naveAncho > m.x &&
					naveY < m.y + m.tamano && naveY + naveAlto > m.y) {
					gameOver = true;
				}

				if (m.y > alto) {
					itMeteoro.remove();
					continue;
				}

				// Colisión entre disparo y meteoro
				Iterator<Laser> itL = lasers.iterator();
				while (itL.hasNext()) {
					Laser l = itL.next();
					if (l.x < m.x + m.tamano && l.x + l.ancho > m.x &&
						l.y < m.y + m.tamano && l.y + l.alto > m.y) {
						
						itL.remove();
						itMeteoro.remove();
						puntuacion += 10;

						// 25% de probabilidad de que caiga una cápsula de mejora
						if (Math.random() < 0.25) {
							powerUps.add(new PowerUp(m.x + m.tamano / 2 - 10, m.y));
						}
						break;
					}
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mover();
			repaint();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// --- Dibujo la nave con el color de mi mascota ---
			g2d.setColor(this.colorMascota);
			g2d.fillOval(naveX, naveY + 6, naveAncho, naveAlto - 6);
			g2d.fillRect(naveX + naveAncho / 2 - 3, naveY, 6, 8);
			g2d.setColor(Color.BLACK);
			g2d.fillOval(naveX + naveAncho / 2 - 2, naveY + 12, 4, 4);

			// --- Dibujo los láseres (cambian de color según el nivel de disparo) ---
			if (nivelDisparo == 1) {
				g2d.setColor(Color.CYAN);
			} else if (nivelDisparo == 2) {
				g2d.setColor(new Color(255, 215, 0));
			} else {
				g2d.setColor(new Color(255, 64, 129));
			}

			for (Laser l : lasers) {
				g2d.fillRect(l.x, l.y, l.ancho, l.alto);
			}

			// --- Dibujo los meteoros ---
			Color colorMeteoro = new Color(140, 120, 110);
			Color colorBordeMeteoro = new Color(90, 75, 70);
			for (Meteoro m : meteoros) {
				g2d.setColor(colorMeteoro);
				g2d.fillOval(m.x, m.y, m.tamano, m.tamano);
				g2d.setColor(colorBordeMeteoro);
				g2d.drawOval(m.x, m.y, m.tamano, m.tamano);
			}

			// --- Dibujo las cápsulas doradas con la 'P' de Power-Up ---
			for (PowerUp p : powerUps) {
				g2d.setColor(new Color(255, 193, 7));
				g2d.fillOval(p.x, p.y, p.tamano, p.tamano);
				g2d.setColor(Color.WHITE);
				g2d.drawOval(p.x, p.y, p.tamano, p.tamano);

				g2d.setColor(Color.BLACK);
				g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
				g2d.drawString("P", p.x + 6, p.y + 14);
			}

			// --- Información del HUD (Puntos y tiempo restante del bonus) ---
			g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
			g2d.setColor(Color.WHITE);
			g2d.drawString("Puntos: " + puntuacion, 20, 32);

			if (nivelDisparo > 1) {
				long restante = Math.max(0, (tiempoFinPowerUp - System.currentTimeMillis()) / 1000 + 1);
				g2d.setFont(new Font("SansSerif", Font.BOLD, 13));
				g2d.setColor(new Color(255, 215, 0));
				g2d.drawString("POWER-UP x" + nivelDisparo + " (" + restante + "s)", 20, 52);
			}

			// Pantalla de Inicio
			if (!iniciado) {
				g2d.setColor(new Color(0, 0, 0, 160));
				g2d.fillRect(0, 0, ancho, alto);

				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
				g2d.drawString("SPACE TAMAGOTCHI", ancho / 2 - 105, alto / 2 - 40);
				g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
				g2d.drawString("Muévete: FLECHAS / A y D", ancho / 2 - 90, alto / 2);
				g2d.drawString("Disparo: AUTOMÁTICO", ancho / 2 - 75, alto / 2 + 25);
				g2d.setFont(new Font("SansSerif", Font.BOLD, 15));
				g2d.setColor(Color.YELLOW);
				g2d.drawString("Pulsa ESPACIO para jugar", ancho / 2 - 95, alto / 2 + 70);
			}
			// Pantalla de Pausa
			else if (pausado) {
				g2d.setColor(new Color(0, 0, 0, 150));
				g2d.fillRect(0, 0, ancho, alto);

				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font("SansSerif", Font.BOLD, 26));
				g2d.drawString("PAUSA", ancho / 2 - 45, alto / 2 - 10);
				g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
				g2d.drawString("Haz clic en > para continuar", ancho / 2 - 95, alto / 2 + 25);
			}
			// Pantalla de Game Over
			else if (gameOver) {
				g2d.setColor(new Color(0, 0, 0, 170));
				g2d.fillRect(0, 0, ancho, alto);

				g2d.setColor(Color.RED);
				g2d.setFont(new Font("SansSerif", Font.BOLD, 26));
				g2d.drawString("¡DESTRUIDO!", ancho / 2 - 80, alto / 2 - 20);

				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font("SansSerif", Font.PLAIN, 15));
				g2d.drawString("Pulsa ESPACIO para reiniciar", ancho / 2 - 105, alto / 2 + 25);
			}
		}
	}
}