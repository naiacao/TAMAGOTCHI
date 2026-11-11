package tamagotchi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// Mi pantalla del menú principal con la selección de los 3 minijuegos
public class JuegosClase extends JFrame {

	private static final long serialVersionUID = 1L;

	// Variable para guardar el color de la mascota que nos llega desde Inicio o Percha
	private Color colorMascota;

	// Constructor 1: Sin parámetros (pone amarillo por defecto por seguridad)
	public JuegosClase() {
		this(null, new Color(255, 193, 7));
	}

	// Constructor 2: Recibe solo el color
	public JuegosClase(Color colorMascota) {
		this(null, colorMascota);
	}

	// Constructor 3: Recibe el JFrame padre (Inicio) y el color seleccionado para la mascota
	public JuegosClase(JFrame padre, Color colorMascota) {
		// Si por alguna razón llega nulo, le asigno el amarillo por defecto
		this.colorMascota = (colorMascota != null) ? colorMascota : new Color(255, 193, 7);

		this.setTitle("TAMAGOCHI - Juegos");
		this.setSize(430, 550);
		this.setResizable(false);
		this.setLocationRelativeTo(padre); // Se posiciona respecto al marco padre o centrado si es null
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Panel contenedor principal de la interfaz
		JPanel panelPrincipal = new JPanel(new BorderLayout(10, 15));
		panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 20, 15));

		// --- PANEL SUPERIOR (HEADER) ---
		JPanel panelSuperior = new JPanel(new BorderLayout());

		// Botón con icono de 3 puntos verticales para volver atrás
		JButton btnVolver = new JButton("⋮");
		btnVolver.setFont(new Font("SansSerif", Font.BOLD, 22));
		btnVolver.setFocusPainted(false);
		btnVolver.setContentAreaFilled(false);
		btnVolver.setBorderPainted(false);
		btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnVolver.setToolTipText("Volver a Inicio");

		btnVolver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				volverAInicio();
			}
		});

		JLabel lblTitulo = new JLabel("SALA DE JUEGOS", SwingConstants.CENTER);
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblTitulo.setForeground(new Color(63, 143, 208));

		// Panel invisible a la derecha para equilibrar el botón de volver y dejar el título centrado
		JPanel panelEspacio = new JPanel();
		panelEspacio.setPreferredSize(btnVolver.getPreferredSize());
		panelEspacio.setOpaque(false);

		panelSuperior.add(btnVolver, BorderLayout.WEST);
		panelSuperior.add(lblTitulo, BorderLayout.CENTER);
		panelSuperior.add(panelEspacio, BorderLayout.EAST);

		panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

		// --- PANEL DE BOTONES DE LOS MINIJUEGOS ---
		JPanel panelJuegos = new JPanel(new GridLayout(3, 1, 10, 15));
		panelJuegos.setBorder(BorderFactory.createTitledBorder("Selecciona un juego"));

		BotonRedondo btnJuego1 = new BotonRedondo("JUEGO 1", new Color(63, 143, 208), new Color(31, 92, 143));
		BotonRedondo btnJuego2 = new BotonRedondo("JUEGO 2", new Color(76, 175, 80), new Color(46, 125, 50));
		BotonRedondo btnJuego3 = new BotonRedondo("JUEGO 3", new Color(255, 193, 7), new Color(199, 145, 0));

		// --- EVENTO JUEGO 1: Flappy Bird ---
		btnJuego1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Le paso el color de la mascota para que la dibuje con ese tono
				Juego1 dialog = new Juego1(JuegosClase.this, colorMascota);
				dialog.setVisible(true);
			}
		});

		// --- EVENTO JUEGO 2: Space Shooter ---
		btnJuego2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Le paso el color de la mascota a la nave espacial
				Juego2 dialog = new Juego2(JuegosClase.this, colorMascota);
				dialog.setVisible(true);
			}
		});

		// --- EVENTO JUEGO 3: Snake ---
		btnJuego3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Le paso el color de la mascota a la serpiente
				Juego3 dialog = new Juego3(JuegosClase.this, colorMascota);
				dialog.setVisible(true);
			}
		});

		panelJuegos.add(btnJuego1);
		panelJuegos.add(btnJuego2);
		panelJuegos.add(btnJuego3);

		panelPrincipal.add(panelJuegos, BorderLayout.CENTER);

		this.add(panelPrincipal);
	}

	// Al hacer clic en el botón superior, vuelvo a la ventana Inicio manteniendo el color de la mascota
	private void volverAInicio() {
		Inicio inicio = new Inicio(colorMascota);
		inicio.setVisible(true);
		this.dispose();
	}
}