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
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Juego1 extends JDialog {

	private static final long serialVersionUID = 1L;

	public Juego1(JFrame padre, Color colorMascota) {
		super(padre, "Juego 1 - Flappy Tamagotchi", true);
		this.setResizable(false);

		PanelJuego panelJuego = new PanelJuego(380, 480, colorMascota);
		this.add(panelJuego);
		this.pack();
		this.setLocationRelativeTo(padre);
	}

	public Juego1(JFrame padre) {
		this(padre, new Color(255, 193, 7));
	}

	public Juego1() {
		this(null, new Color(255, 193, 7));
	}

	private class PanelJuego extends JPanel implements ActionListener {
		private static final long serialVersionUID = 1L;

		private final int ancho;
		private final int alto;
		private Color colorMascota;

		// Variables de la mascota
		private int tamagotchiX = 60;
		private int tamagotchiY = 200;
		private int tamagotchiAncho = 30;
		private int tamagotchiAlto = 30;

		private double velocidadY = 0;
		private final double gravedad = 0.6;
		private final double salto = -9.0;

		// Estados del juego
		private boolean iniciado = false;
		private boolean pausado = false;
		private boolean gameOver = false;
		private int puntuacion = 0;

		// Botones superiores
		private JButton btnPausa;
		private JButton btnSalir;

		// Estructura de tuberías
		private class Tubo {
			int x;
			int y;
			int ancho = 55;
			int alto = 400;
			boolean esSuperior;
			boolean superado = false;

			Tubo(int x, int y, boolean esSuperior) {
				this.x = x;
				this.y = y;
				this.esSuperior = esSuperior;
			}
		}

		private List<Tubo> tuberias;
		private Timer gameLoop;
		private Timer creadorTuberias;

		public PanelJuego(int ancho, int alto, Color colorMascota) {
			this.ancho = ancho;
			this.alto = alto;
			this.colorMascota = (colorMascota != null) ? colorMascota : new Color(255, 193, 7);

			this.setPreferredSize(new Dimension(ancho, alto));
			this.setBackground(new Color(180, 225, 250));
			this.setLayout(null);
			this.setFocusable(true);

			tuberias = new ArrayList<>();

			// --- Botón de Pausa ---
			btnPausa = new JButton("||");
			btnPausa.setFont(new Font("SansSerif", Font.BOLD, 12));
			btnPausa.setBounds(ancho - 105, 10, 45, 30);
			btnPausa.setMargin(new Insets(0, 0, 0, 0)); // Elimina margen interno para evitar "..."
			btnPausa.setFocusable(false);
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
			btnSalir.setMargin(new Insets(0, 0, 0, 0)); // Elimina margen interno para evitar "..."
			btnSalir.setFocusable(false);
			btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
			btnSalir.setToolTipText("Salir a la pantalla anterior");

			btnSalir.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gameLoop.stop();
					creadorTuberias.stop();
					Juego1.this.dispose();
				}
			});
			this.add(btnSalir);

			// Controles por teclado (ESPACIO)
			this.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_SPACE) {
						if (!iniciado) {
							iniciado = true;
							velocidadY = salto;
						} else if (gameOver) {
							reiniciarJuego();
						} else if (!pausado) {
							velocidadY = salto;
						}
					}
				}
			});

			gameLoop = new Timer(16, this);
			gameLoop.start();

			creadorTuberias = new Timer(1600, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					generarTuberia();
				}
			});
			creadorTuberias.start();
		}

		private void generarTuberia() {
			if (!iniciado || pausado || gameOver) return;

			int espacioHueco = 130;
			int altoTuberia = 400;
			int yHueco = 50 + (int) (Math.random() * 200);

			tuberias.add(new Tubo(ancho, yHueco - altoTuberia, true));
			tuberias.add(new Tubo(ancho, yHueco + espacioHueco, false));
		}

		private void reiniciarJuego() {
			tamagotchiY = 200;
			velocidadY = 0;
			tuberias.clear();
			puntuacion = 0;
			gameOver = false;
			pausado = false;
			iniciado = true;
			btnPausa.setText("||");
		}

		private void mover() {
			if (!iniciado || pausado || gameOver) return;

			velocidadY += gravedad;
			tamagotchiY += velocidadY;
			tamagotchiY = Math.max(tamagotchiY, 0);

			for (int i = 0; i < tuberias.size(); i++) {
				Tubo t = tuberias.get(i);
				t.x -= 3;

				if (!t.superado && tamagotchiX > t.x + t.ancho) {
					t.superado = true;
					if (i % 2 == 0) {
						puntuacion++;
					}
				}

				if (colisiona(t)) {
					gameOver = true;
				}
			}

			if (tamagotchiY + tamagotchiAlto >= alto - 15) {
				gameOver = true;
			}
		}

		private boolean colisiona(Tubo t) {
			int xMin = t.x - 4;
			int xMax = t.x + t.ancho + 4;
			return tamagotchiX < xMax &&
				   tamagotchiX + tamagotchiAncho > xMin &&
				   tamagotchiY < t.y + t.alto &&
				   tamagotchiY + tamagotchiAlto > t.y;
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

			// Colores tuberías
			Color colorCuerpo = new Color(46, 125, 50);
			Color colorReborde = new Color(67, 160, 71);
			Color colorBordeOscuro = new Color(27, 94, 32);

			for (Tubo t : tuberias) {
				// Cuerpo
				g2d.setColor(colorCuerpo);
				g2d.fillRect(t.x, t.y, t.ancho, t.alto);
				g2d.setColor(colorBordeOscuro);
				g2d.drawRect(t.x, t.y, t.ancho, t.alto);

				// Reborde / Cabeza de la tubería
				int anchoReborde = t.ancho + 8;
				int xReborde = t.x - 4;
				int altoReborde = 22;
				int yReborde = t.esSuperior ? (t.y + t.alto - altoReborde) : t.y;

				g2d.setColor(colorReborde);
				g2d.fillRoundRect(xReborde, yReborde, anchoReborde, altoReborde, 6, 6);
				g2d.setColor(colorBordeOscuro);
				g2d.drawRoundRect(xReborde, yReborde, anchoReborde, altoReborde, 6, 6);
			}

			// Suelo
			g2d.setColor(new Color(76, 175, 80));
			g2d.fillRect(0, alto - 15, ancho, 15);

			// Mascota con el color elegido
			g2d.setColor(this.colorMascota);
			g2d.fillOval(tamagotchiX, tamagotchiY, tamagotchiAncho, tamagotchiAlto);

			// Ojo
			g2d.setColor(Color.BLACK);
			g2d.fillOval(tamagotchiX + 20, tamagotchiY + 8, 5, 5);

			// Puntuación
			g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
			g2d.setColor(Color.WHITE);
			g2d.drawString("Puntos: " + puntuacion, 20, 35);

			// Pantalla de Inicio
			if (!iniciado) {
				g2d.setColor(new Color(0, 0, 0, 140));
				g2d.fillRect(0, 0, ancho, alto);

				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
				g2d.drawString("PULSA ESPACIO", ancho / 2 - 80, alto / 2 - 15);
				g2d.setFont(new Font("SansSerif", Font.PLAIN, 16));
				g2d.drawString("para empezar a jugar", ancho / 2 - 82, alto / 2 + 15);
			}
			// Pantalla de Pausa
			else if (pausado) {
				g2d.setColor(new Color(0, 0, 0, 140));
				g2d.fillRect(0, 0, ancho, alto);

				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font("SansSerif", Font.BOLD, 26));
				g2d.drawString("PAUSA", ancho / 2 - 45, alto / 2 - 10);
				g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
				g2d.drawString("Haz clic en > para continuar", ancho / 2 - 95, alto / 2 + 25);
			}
			// Pantalla de Game Over
			else if (gameOver) {
				g2d.setColor(new Color(0, 0, 0, 160));
				g2d.fillRect(0, 0, ancho, alto);

				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font("SansSerif", Font.BOLD, 26));
				g2d.drawString("¡GAME OVER!", ancho / 2 - 85, alto / 2 - 20);

				g2d.setFont(new Font("SansSerif", Font.PLAIN, 15));
				g2d.drawString("Pulsa ESPACIO para reiniciar", ancho / 2 - 105, alto / 2 + 20);
			}
		}
	}
}