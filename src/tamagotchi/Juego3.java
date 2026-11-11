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
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

// Juego 3 - Snake, la idea de este juego está sacada de un tutorial de Youtube
public class Juego3 extends JDialog {

	private static final long serialVersionUID = 1L;

	// Le paso la ventana de los juegos y el color para la mascota
	public Juego3(JFrame padre, Color colorMascota) {
		super(padre, "Juego 3 - Snake Tamagotchi", true);
		this.setResizable(false);

		PanelJuego panelJuego = new PanelJuego(380, 480, colorMascota);
		this.add(panelJuego);
		this.pack();
		this.setLocationRelativeTo(padre);
	}

	// Por si abro la ventana sin pasarle color (pongo amarillo por defecto)
	public Juego3(JFrame padre) {
		this(padre, new Color(255, 193, 7));
	}

	public Juego3() {
		this(null, new Color(255, 193, 7));
	}

	// El panel del juego
	private class PanelJuego extends JPanel implements ActionListener {
		private static final long serialVersionUID = 1L;

		// El tamaño del tablero y las casillas (de 20x20 px cada una)
		private final int ancho;
		private final int alto;
		private final int tileSize = 20;
		private Color colorMascota;

		// Una clase para guardar posiciones X e Y
		private class Tile {
			int x;
			int y;

			Tile(int x, int y) {
				this.x = x;
				this.y = y;
			}
		}

		// Cabeza, cuerpecito y la manzana
		private Tile snakeHead;
		private List<Tile> snakeBody;
		private Tile food;
		private Random random;

		// Por dónde se va moviendo (-1, 0, 1)
		private int velocityX;
		private int velocityY;

		// Control de estados y los puntos
		private boolean iniciado = false;
		private boolean pausado = false;
		private boolean gameOver = false;
		private int puntuacion = 0;

		// Los botones de arriba
		private JButton btnPausa;
		private JButton btnSalir;

		// El timer que va moviendo todo todo el rato
		private Timer gameLoop;

		public PanelJuego(int ancho, int alto, Color colorMascota) {
			this.ancho = ancho;
			this.alto = alto;
			// Si por lo que sea no viene color, le meto un amarillo estándar
			this.colorMascota = (colorMascota != null) ? colorMascota : new Color(255, 193, 7);

			this.setPreferredSize(new Dimension(ancho, alto));
			this.setBackground(new Color(20, 25, 30)); // Fondo oscurito arcade
			this.setLayout(null);
			this.setFocusable(true);

			random = new Random();
			snakeHead = new Tile(9, 12);
			snakeBody = new ArrayList<>();
			food = new Tile(10, 10);
			placeFood(); // Coloco la primera manzana

			// De salida arranca tirando hacia la derecha
			velocityX = 1;
			velocityY = 0;

			// --- Botón de Pausa ---
			btnPausa = new JButton("||");
			btnPausa.setFont(new Font("SansSerif", Font.BOLD, 12));
			btnPausa.setBounds(ancho - 105, 10, 45, 30);
			btnPausa.setMargin(new Insets(0, 0, 0, 0)); // Quito los márgenes feos para que no salgan los 3 puntos
			btnPausa.setFocusable(false); // Clave: false para que no me robe los controles del teclado mientras juego
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
			btnSalir.setFocusable(false); // Mismo truco del foco aquí
			btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
			btnSalir.setToolTipText("Salir al menú de juegos");

			btnSalir.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Paro el timer para no dejar procesos colgados por ahí
					if (gameLoop != null) {
						gameLoop.stop();
					}
					Juego3.this.dispose();
				}
			});
			this.add(btnSalir);

			// Los controles del teclado (Flechas o WASD)
			this.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					int key = e.getKeyCode();

					// El espacio solo para empezar o volver a jugar tras morir
					if (!iniciado && key == KeyEvent.VK_SPACE) {
						iniciado = true;
						return;
					}

					if (gameOver && key == KeyEvent.VK_SPACE) {
						reiniciarJuego();
						return;
					}

					if (!iniciado || pausado || gameOver) return;

					// Flechas o WASD (controlo que no pueda dar la vuelta sobre su propia cabeza)
					if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && velocityY != 1) {
						velocityX = 0;
						velocityY = -1;
					} else if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && velocityY != -1) {
						velocityX = 0;
						velocityY = 1;
					} else if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && velocityX != 1) {
						velocityX = -1;
						velocityY = 0;
					} else if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && velocityX != -1) {
						velocityX = 1;
						velocityY = 0;
					}
				}
			});

			// La velocidad del juego: un tick cada 100ms
			gameLoop = new Timer(100, this);
			gameLoop.start();
		}

		// Para colocar la manzana en un sitio random sin liarla
		private void placeFood() {
			int cols = ancho / tileSize;
			int rows = alto / tileSize;

			boolean colocada = false;
			while (!colocada) {
				int fx = random.nextInt(cols);
				int fy = random.nextInt(rows);

				// Que no aparezca arriba del todo para que no la tapen los botones
				if (fy < 3) continue;

				// Si justo cae donde está la serpiente, vuelvo a probar
				boolean solapada = (snakeHead.x == fx && snakeHead.y == fy);
				for (Tile part : snakeBody) {
					if (part.x == fx && part.y == fy) {
						solapada = true;
						break;
					}
				}

				if (!solapada) {
					food.x = fx;
					food.y = fy;
					colocada = true;
				}
			}
		}

		// Limpio todo para empezar de cero
		private void reiniciarJuego() {
			snakeHead = new Tile(9, 12);
			snakeBody.clear();
			placeFood();
			velocityX = 1;
			velocityY = 0;
			puntuacion = 0;
			gameOver = false;
			pausado = false;
			iniciado = true;
			btnPausa.setText("||");
		}

		// Toda la lógica de mover, comer y morir
		private void mover() {
			if (!iniciado || pausado || gameOver) return;

			// ¿Nos hemos comido la manzana?
			if (collision(snakeHead, food)) {
				snakeBody.add(new Tile(food.x, food.y)); // Sumamos cuerpo
				puntuacion += 10;
				placeFood(); // Otra manzana
			}

			// El cuerpo va siguiendo el camino de la cabeza
			for (int i = snakeBody.size() - 1; i >= 0; i--) {
				Tile part = snakeBody.get(i);
				if (i == 0) {
					part.x = snakeHead.x;
					part.y = snakeHead.y;
				} else {
					Tile prevPart = snakeBody.get(i - 1);
					part.x = prevPart.x;
					part.y = prevPart.y;
				}
			}

			// Muevo la cabeza
			snakeHead.x += velocityX;
			snakeHead.y += velocityY;

			// ¿Nos hemos mordido la cola? Game over
			for (int i = 0; i < snakeBody.size(); i++) {
				if (collision(snakeHead, snakeBody.get(i))) {
					gameOver = true;
				}
			}

			// ¿Choque contra la pared? Game over
			if (snakeHead.x < 0 || snakeHead.x >= (ancho / tileSize) ||
				snakeHead.y < 0 || snakeHead.y >= (alto / tileSize)) {
				gameOver = true;
			}
		}

		// Comprobar si dos fichas están en la misma casilla
		private boolean collision(Tile t1, Tile t2) {
			return t1.x == t2.x && t1.y == t2.y;
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

			// Rejilla de fondo suave
			g2d.setColor(new Color(30, 35, 42));
			for (int i = 0; i < ancho / tileSize; i++) {
				g2d.drawLine(i * tileSize, 0, i * tileSize, alto);
			}
			for (int i = 0; i < alto / tileSize; i++) {
				g2d.drawLine(0, i * tileSize, ancho, i * tileSize);
			}

			// --- La manzana roja ---
			g2d.setColor(new Color(229, 57, 53));
			g2d.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

			// --- La cabeza con el color de mi mascota ---
			g2d.setColor(this.colorMascota);
			g2d.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

			// Un ojito para que quede más mona
			g2d.setColor(Color.BLACK);
			g2d.fillOval(snakeHead.x * tileSize + 6, snakeHead.y * tileSize + 6, 4, 4);

			// --- El cuerpo un pelín más oscuro ---
			g2d.setColor(this.colorMascota.darker());
			for (Tile part : snakeBody) {
				g2d.fill3DRect(part.x * tileSize, part.y * tileSize, tileSize, tileSize, true);
			}

			// La puntuación arriba a la izquierda
			g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
			g2d.setColor(Color.WHITE);
			g2d.drawString("Puntos: " + puntuacion, 20, 32);

			// --- Menús superpuestos (Inicio, Pausa, Game Over) ---
			if (!iniciado) {
				g2d.setColor(new Color(0, 0, 0, 160));
				g2d.fillRect(0, 0, ancho, alto);

				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font("SansSerif", Font.BOLD, 22));
				g2d.drawString("SNAKE TAMAGOTCHI", ancho / 2 - 110, alto / 2 - 40);
				g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
				g2d.drawString("Muévete: FLECHAS / WASD", ancho / 2 - 95, alto / 2 + 5);
				g2d.setFont(new Font("SansSerif", Font.BOLD, 15));
				g2d.setColor(Color.YELLOW);
				g2d.drawString("Pulsa ESPACIO para jugar", ancho / 2 - 95, alto / 2 + 55);
			} else if (pausado) {
				g2d.setColor(new Color(0, 0, 0, 150));
				g2d.fillRect(0, 0, ancho, alto);

				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font("SansSerif", Font.BOLD, 26));
				g2d.drawString("PAUSA", ancho / 2 - 45, alto / 2 - 10);
				g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
				g2d.drawString("Haz clic en > para continuar", ancho / 2 - 95, alto / 2 + 25);
			} else if (gameOver) {
				g2d.setColor(new Color(0, 0, 0, 170));
				g2d.fillRect(0, 0, ancho, alto);

				g2d.setColor(Color.RED);
				g2d.setFont(new Font("SansSerif", Font.BOLD, 26));
				g2d.drawString("¡GAME OVER!", ancho / 2 - 85, alto / 2 - 20);

				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font("SansSerif", Font.PLAIN, 15));
				g2d.drawString("Puntuación final: " + puntuacion, ancho / 2 - 70, alto / 2 + 10);
				g2d.drawString("Pulsa ESPACIO para reiniciar", ancho / 2 - 105, alto / 2 + 35);
			}
		}
	}
}