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

public class JuegosClase extends JFrame {

	private static final long serialVersionUID = 1L;
	private Color colorMascota;

	public JuegosClase() {
		this(new Color(255, 193, 7)); // Color por defecto si no recibe uno
	}

	public JuegosClase(Color colorMascota) {
		this.colorMascota = colorMascota;

		this.setTitle("TAMAGOCHI - Juegos");
		this.setSize(430, 550);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Panel contenedor principal
		JPanel panelPrincipal = new JPanel(new BorderLayout(10, 15));
		panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 20, 15));

		// Panel superior (Header)
		JPanel panelSuperior = new JPanel(new BorderLayout());

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

		JPanel panelEspacio = new JPanel();
		panelEspacio.setPreferredSize(btnVolver.getPreferredSize());
		panelEspacio.setOpaque(false);

		panelSuperior.add(btnVolver, BorderLayout.WEST);
		panelSuperior.add(lblTitulo, BorderLayout.CENTER);
		panelSuperior.add(panelEspacio, BorderLayout.EAST);

		panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

		// Panel de minijuegos
		JPanel panelJuegos = new JPanel(new GridLayout(3, 1, 10, 15));
		panelJuegos.setBorder(BorderFactory.createTitledBorder("Selecciona un juego"));

		BotonRedondo btnJuego1 = new BotonRedondo("JUEGO 1", new Color(63, 143, 208), new Color(31, 92, 143));
		BotonRedondo btnJuego2 = new BotonRedondo("JUEGO 2", new Color(76, 175, 80), new Color(46, 125, 50));
		BotonRedondo btnJuego3 = new BotonRedondo("JUEGO 3", new Color(255, 193, 7), new Color(199, 145, 0));

		// Evento Juego 1 (Pasa el color de la mascota a la ventana del juego)
		btnJuego1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Juego1 dialog = new Juego1(JuegosClase.this, colorMascota);
				dialog.setVisible(true);
			}
		});

		btnJuego2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Juego2 dialog = new Juego2(JuegosClase.this);
				dialog.setVisible(true);
			}
		});

		btnJuego3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Juego3 dialog = new Juego3(JuegosClase.this);
				dialog.setVisible(true);
			}
		});

		panelJuegos.add(btnJuego1);
		panelJuegos.add(btnJuego2);
		panelJuegos.add(btnJuego3);

		panelPrincipal.add(panelJuegos, BorderLayout.CENTER);

		this.add(panelPrincipal);
	}

	private void volverAInicio() {
		Inicio inicio = new Inicio();
		inicio.setColorSeleccionado(colorMascota);
		inicio.setVisible(true);
		this.dispose();
	}
}