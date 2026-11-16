package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import domain.BotonRedondo;
import domain.EstadoMascota;
import domain.Juego1;
import domain.Juego2;
import domain.Juego3;
import domain.Juego4;

// Mi pantalla del menú principal con la selección de los 4 minijuegos
public class JuegosClase extends JFrame {

	private static final long serialVersionUID = 1L;

	// Variable para guardar el color de la mascota que nos llega desde Inicio o Percha
	private Color colorMascota;

	// Constructor 1: Le metemos el estado de la mascota actual
	public JuegosClase() {
		this(null, EstadoMascota.getColor());
	}

	// Constructor 2: Recibe solo el color
	public JuegosClase(Color colorMascota) {
		this(null, colorMascota);
	}

	// Constructor 3: Recibe el JFrame padre (Inicio) y el color seleccionado para la mascota
	public JuegosClase(JFrame padre, Color colorMascota) {
		if (colorMascota != null) {
			this.colorMascota = colorMascota;
		} else {
			this.colorMascota = new Color(255, 193, 7);
		}
		EstadoMascota.setColor(this.colorMascota);

		// Mismo menú superior que en Comida y Percha (añadido antes de setSize)
		this.setJMenuBar(new MenuJuego(
				e -> irAInicio(),
				e -> cerrarJuego(),
				e -> cambiarUsuario()));

		this.setTitle("TAMAGOCHI - Juegos");
		this.setSize(430, 550);
		this.setResizable(false);
		this.setLocationRelativeTo(padre);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Panel contenedor principal de la interfaz
		JPanel panelPrincipal = new JPanel(new BorderLayout(10, 15));
		panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 20, 15));

		// --- PANEL SUPERIOR (TÍTULO CENTRADO) ---
		JLabel lblTitulo = new JLabel("SALA DE JUEGOS", SwingConstants.CENTER);
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblTitulo.setForeground(new Color(63, 143, 208));
		panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

		// --- PANEL DE BOTONES DE LOS MINIJUEGOS ---
		JPanel panelJuegos = new JPanel(new GridLayout(4, 1, 10, 12));
		panelJuegos.setBorder(BorderFactory.createTitledBorder("Selecciona un juego"));

		BotonRedondo btnJuego1 = new BotonRedondo("JUEGO 1", new Color(63, 143, 208), new Color(31, 92, 143));
		BotonRedondo btnJuego2 = new BotonRedondo("JUEGO 2", new Color(76, 175, 80), new Color(46, 125, 50));
		BotonRedondo btnJuego3 = new BotonRedondo("JUEGO 3", new Color(255, 193, 7), new Color(199, 145, 0));
		BotonRedondo btnJuego4 = new BotonRedondo("JUEGO 4", new Color(171, 71, 188), new Color(123, 31, 162));

		// --- EVENTO JUEGO 1: Flappy Bird ---
		btnJuego1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Juego1 dialog = new Juego1(JuegosClase.this, colorMascota);
				dialog.setVisible(true);
			}
		});

		// --- EVENTO JUEGO 2: Space Shooter ---
		btnJuego2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Juego2 dialog = new Juego2(JuegosClase.this, colorMascota);
				dialog.setVisible(true);
			}
		});

		// --- EVENTO JUEGO 3: Snake ---
		btnJuego3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Juego3 dialog = new Juego3(JuegosClase.this, colorMascota);
				dialog.setVisible(true);
			}
		});

		// --- EVENTO JUEGO 4: 3 en Raya ---
		btnJuego4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Juego4 dialog = new Juego4(JuegosClase.this, colorMascota);
				dialog.setVisible(true);
			}
		});

		panelJuegos.add(btnJuego1);
		panelJuegos.add(btnJuego2);
		panelJuegos.add(btnJuego3);
		panelJuegos.add(btnJuego4);

		panelPrincipal.add(panelJuegos, BorderLayout.CENTER);

		// Mascota con gorro en la parte inferior
		JLabel imagenMascota = new JLabel("", SwingConstants.CENTER);
		imagenMascota.setIcon(EstadoMascota.componerMascota(120, 100));
		panelPrincipal.add(imagenMascota, BorderLayout.SOUTH);

		this.add(panelPrincipal);
	}

	/** Vuelve a la pantalla principal manteniendo el color de la mascota. */
	private void irAInicio() {
		new Inicio(this.colorMascota).setVisible(true);
		this.dispose();
	}

	/** Vuelve a la pantalla de login para cambiar de usuario. */
	private void cambiarUsuario() {
		new PrimeraVentana().setVisible(true);
		this.dispose();
	}

	/** Cierra el juego entero, pidiendo confirmación antes. */
	private void cerrarJuego() {
		int opcion = JOptionPane.showConfirmDialog(
				this,
				"¿Seguro que quieres cerrar el juego?",
				"Cerrar juego",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);

		if (opcion == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
}