package tamagotchi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

// Juego 4 - 3 en Raya Tamagotchi (basado en el tutorial de Tic-Tac-Toe en Java Swing)
public class Juego4 extends JDialog {

	private static final long serialVersionUID = 1L;

	private Color colorMascota;

	private JLabel textLabel = new JLabel();
	private JPanel textPanel = new JPanel(new BorderLayout());
	private JPanel boardPanel = new JPanel();

	private JButton[][] board = new JButton[3][3];
	private String playerX = "X"; // Jugador (usa el color de la mascota)
	private String playerO = "O"; // Rival
	private String currentPlayer = playerX;

	private boolean gameOver = false;
	private int turns = 0;
	private int victorias = 0;

	private Timer timerRival;
	private Random random = new Random();

	// Colores del tablero (tono intermedio para que se vea bien cualquier color de mascota, incluido blanco y negro)
	private final Color colorFondoTablero = new Color(35, 40, 48);
	private final Color colorCasilla = new Color(70, 78, 90);
	private final Color colorCasillaGanadora = new Color(45, 55, 65);

	public Juego4(JFrame padre, Color colorMascota) {
		super(padre, "Juego 4 - 3 en Raya Tamagotchi", true);
		this.colorMascota = (colorMascota != null) ? colorMascota : new Color(255, 193, 7);

		this.setSize(380, 480);
		this.setResizable(false);
		this.setLocationRelativeTo(padre);
		this.setLayout(new BorderLayout());

		// --- PANEL SUPERIOR (Estado + Botones Reiniciar y Salir X) ---
		textPanel.setBackground(colorFondoTablero);
		textPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 8));

		textLabel.setForeground(Color.WHITE);
		textLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		textLabel.setHorizontalAlignment(SwingConstants.LEFT);
		textLabel.setText("Tu turno (X) | Victorias: 0");

		JPanel panelBotonesTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		panelBotonesTop.setOpaque(false);

		// Botón para empezar otra ronda
		JButton btnReiniciar = new JButton("↻");
		btnReiniciar.setFont(new Font("SansSerif", Font.BOLD, 15));
		btnReiniciar.setPreferredSize(new Dimension(42, 30));
		btnReiniciar.setMargin(new Insets(0, 0, 0, 0));
		btnReiniciar.setFocusable(false);
		btnReiniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnReiniciar.setToolTipText("Nueva ronda");
		btnReiniciar.addActionListener(e -> reiniciarPartida());

		// Botón X para salir (igual que en Juego 1, 2 y 3)
		JButton btnSalir = new JButton("X");
		btnSalir.setFont(new Font("SansSerif", Font.BOLD, 14));
		btnSalir.setPreferredSize(new Dimension(42, 30));
		btnSalir.setMargin(new Insets(0, 0, 0, 0));
		btnSalir.setFocusable(false);
		btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnSalir.setToolTipText("Salir al menú de juegos");
		btnSalir.addActionListener(e -> {
			if (timerRival != null) {
				timerRival.stop();
			}
			EstadoMascota.registrarActividadJuego();
			Juego4.this.dispose();
		});

		panelBotonesTop.add(btnReiniciar);
		panelBotonesTop.add(btnSalir);

		textPanel.add(textLabel, BorderLayout.CENTER);
		textPanel.add(panelBotonesTop, BorderLayout.EAST);
		this.add(textPanel, BorderLayout.NORTH);

		// --- TABLERO 3x3 ---
		boardPanel.setLayout(new GridLayout(3, 3, 6, 6));
		boardPanel.setBackground(colorFondoTablero);
		boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(boardPanel, BorderLayout.CENTER);

		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				JButton tile = new JButton();
				board[r][c] = tile;
				boardPanel.add(tile);

				tile.setBackground(colorCasilla);
				tile.setForeground(this.colorMascota);
				tile.setFont(new Font("SansSerif", Font.BOLD, 72));
				tile.setFocusable(false);
				tile.setBorder(BorderFactory.createLineBorder(new Color(50, 56, 66), 2));
				tile.setCursor(new Cursor(Cursor.HAND_CURSOR));

				tile.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (gameOver || !currentPlayer.equals(playerX)) return;

						JButton clickedTile = (JButton) e.getSource();
						if (clickedTile.getText().equals("")) {
							clickedTile.setForeground(Juego4.this.colorMascota);
							clickedTile.setText(playerX);
							turns++;
							checkWinner();

							if (!gameOver) {
								currentPlayer = playerO;
								textLabel.setText("Turno del rival (O)...");
								timerRival.restart();
							}
						}
					}
				});
			}
		}

		// Temporizador breve para que el rival responda tras tu jugada
		timerRival = new Timer(350, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jugadaRival();
			}
		});
		timerRival.setRepeats(false);
	}

	public Juego4(JFrame padre) {
		this(padre, new Color(255, 193, 7));
	}

	public Juego4() {
		this(null, new Color(255, 193, 7));
	}

	// Movimiento automático del rival ("O")
	private void jugadaRival() {
		if (gameOver) return;

		JButton elegida = buscarJugadaClave(playerO); // 1. Intentar ganar
		if (elegida == null) {
			elegida = buscarJugadaClave(playerX);    // 2. Bloquear al jugador
		}
		if (elegida == null && board[1][1].getText().equals("")) {
			elegida = board[1][1];                   // 3. Ocupar el centro si está libre
		}
		if (elegida == null) {
			List<JButton> libres = new ArrayList<>();
			for (int r = 0; r < 3; r++) {
				for (int c = 0; c < 3; c++) {
					if (board[r][c].getText().equals("")) {
						libres.add(board[r][c]);
					}
				}
			}
			if (!libres.isEmpty()) {
				elegida = libres.get(random.nextInt(libres.size()));
			}
		}

		if (elegida != null) {
			elegida.setForeground(new Color(235, 235, 235));
			elegida.setText(playerO);
			turns++;
			checkWinner();

			if (!gameOver) {
				currentPlayer = playerX;
				textLabel.setText("Tu turno (X) | Victorias: " + victorias);
			}
		}
	}

	// Busca si algún jugador tiene 2 en raya y la 3ª casilla libre
	private JButton buscarJugadaClave(String marca) {
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				if (board[r][c].getText().equals("")) {
					board[r][c].setText(marca);
					boolean hariaTres = hayTresEnRaya(marca);
					board[r][c].setText("");
					if (hariaTres) {
						return board[r][c];
					}
				}
			}
		}
		return null;
	}

	private boolean hayTresEnRaya(String m) {
		for (int i = 0; i < 3; i++) {
			if (board[i][0].getText().equals(m) && board[i][1].getText().equals(m) && board[i][2].getText().equals(m)) return true;
			if (board[0][i].getText().equals(m) && board[1][i].getText().equals(m) && board[2][i].getText().equals(m)) return true;
		}
		if (board[0][0].getText().equals(m) && board[1][1].getText().equals(m) && board[2][2].getText().equals(m)) return true;
		if (board[0][2].getText().equals(m) && board[1][1].getText().equals(m) && board[2][0].getText().equals(m)) return true;
		return false;
	}

	// Comprueba horizontal, vertical, diagonales y empate (igual que en el tutorial)
	private void checkWinner() {
		// Horizontal
		for (int r = 0; r < 3; r++) {
			if (board[r][0].getText().equals("")) continue;

			if (board[r][0].getText().equals(board[r][1].getText()) &&
				board[r][1].getText().equals(board[r][2].getText())) {
				for (int i = 0; i < 3; i++) {
					setWinner(board[r][i]);
				}
				finalizarRondaConGanador(board[r][0].getText());
				return;
			}
		}

		// Vertical
		for (int c = 0; c < 3; c++) {
			if (board[0][c].getText().equals("")) continue;

			if (board[0][c].getText().equals(board[1][c].getText()) &&
				board[1][c].getText().equals(board[2][c].getText())) {
				for (int i = 0; i < 3; i++) {
					setWinner(board[i][c]);
				}
				finalizarRondaConGanador(board[0][c].getText());
				return;
			}
		}

		// Diagonal principal
		if (!board[0][0].getText().equals("") &&
			board[0][0].getText().equals(board[1][1].getText()) &&
			board[1][1].getText().equals(board[2][2].getText())) {
			for (int i = 0; i < 3; i++) {
				setWinner(board[i][i]);
			}
			finalizarRondaConGanador(board[0][0].getText());
			return;
		}

		// Diagonal secundaria
		if (!board[0][2].getText().equals("") &&
			board[0][2].getText().equals(board[1][1].getText()) &&
			board[1][1].getText().equals(board[2][0].getText())) {
			setWinner(board[0][2]);
			setWinner(board[1][1]);
			setWinner(board[2][0]);
			finalizarRondaConGanador(board[0][2].getText());
			return;
		}

		// Empate
		if (turns == 9) {
			for (int r = 0; r < 3; r++) {
				for (int c = 0; c < 3; c++) {
					setTie(board[r][c]);
				}
			}
			gameOver = true;
			EstadoMascota.sumarFelicidadGastandoEnergia(8);
			textLabel.setText("¡Empate! (+8 felicidad)");
		}
	}

	private void finalizarRondaConGanador(String ganador) {
		gameOver = true;
		if (ganador.equals(playerX)) {
			victorias++;
			EstadoMascota.sumarFelicidadGastandoEnergia(15);
			textLabel.setText("¡Ganaste! (+15 felicidad)");
		} else {
			EstadoMascota.sumarFelicidadGastandoEnergia(3);
			textLabel.setText("¡Ganó O! Pulsa ↻");
		}
	}

	private void setWinner(JButton tile) {
		tile.setForeground(new Color(76, 175, 80));
		tile.setBackground(colorCasillaGanadora);
	}

	private void setTie(JButton tile) {
		tile.setForeground(new Color(255, 193, 7));
		tile.setBackground(colorCasillaGanadora);
	}

	private void reiniciarPartida() {
		if (EstadoMascota.getEnergia() <= 0 || EstadoMascota.getHambre() <= 0) {
			if (timerRival != null) {
				timerRival.stop();
			}
			JOptionPane.showMessageDialog(
					Juego4.this,
					"Tu mascota no puede seguir jugando: necesita comer o descansar.",
					"Sin fuerzas",
					JOptionPane.WARNING_MESSAGE);
			Juego4.this.dispose();
			return;
		}
		
		if (timerRival != null) {
			timerRival.stop();
		}
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				board[r][c].setText("");
				board[r][c].setBackground(colorCasilla);
				board[r][c].setForeground(this.colorMascota);
			}
		}
		currentPlayer = playerX;
		gameOver = false;
		turns = 0;
		textLabel.setText("Tu turno (X) | Victorias: " + victorias);
	}
}