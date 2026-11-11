package tamagotchi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class Inicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private boolean dormido = false;

	// Guarda el color seleccionado de la mascota para aplicarlo aquí y enviarlo a la pantalla de juegos
	private Color colorMascota;

	private JProgressBar barrahambre = new JProgressBar(0, 100);
	private JProgressBar barraenergia = new JProgressBar(0, 100);
	private JProgressBar barrafelicidad = new JProgressBar(0, 100);

	private JPanel panelTamagotchi = new JPanel(new BorderLayout());
	private JPanel oscurecedor;
	private Hiloenergia hiloenergia;

	// CONSTRUCTOR 1: Por defecto (si se abre el juego desde cero, asigna amarillo)
	public Inicio() {
		this(new Color(255, 193, 7));
	}

	// CONSTRUCTOR 2: Recibe el color seleccionado desde la pantalla Percha
	public Inicio(Color colorMascota) {
		// Asignamos el color recibido (o amarillo por seguridad si llega nulo)
		this.colorMascota = (colorMascota != null) ? colorMascota : new Color(255, 193, 7);

		// Añadir el menú
		this.setJMenuBar(new MenuInicio(
				e -> cerrarJuego(),
				e -> cambiarUsuario()));

		this.setTitle("TAMAGOCHI - Inicio");
		this.setSize(430, 550);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Creación de los paneles
		JPanel panelPrincipal = new JPanel(new BorderLayout());
		JPanel panelCentro = new JPanel(new GridLayout(2, 1, 0, 10));
		JPanel panelBotones = new JPanel(new GridLayout(1, 3, 10, 10));
		panelBotones.setPreferredSize(new Dimension(0, 75));
		panelBotones.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		JPanel panelInfo = new JPanel(new GridLayout(3, 1, 0, 10));

		panelInfo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		panelInfo.add(crearFila("Hambre", "fotos/POLLO.png", 0, new Color(255, 152, 0), barrahambre));
		panelInfo.add(crearFila("Energia", "fotos/RAYO.png", 0, new Color(76, 175, 80), barraenergia));
		panelInfo.add(crearFila("Felicidad", "fotos/CORAZON.png", 0, new Color(63, 143, 208), barrafelicidad));

		panelTamagotchi.setBorder(BorderFactory.createTitledBorder("Estado"));

		// Creación de botones
		BotonRedondo btnalimentar = new BotonRedondo("ALIMENTAR", new Color(76, 175, 80), new Color(46, 125, 50));
		btnalimentar.setToolTipText("COMIDA");
		BotonRedondo btnjugar = new BotonRedondo("JUGAR", new Color(63, 143, 208), new Color(31, 92, 143));
		btnjugar.setToolTipText("JUGAR");
		BotonRedondo btndormir = new BotonRedondo("DORMIR", new Color(255, 193, 7), new Color(199, 145, 0));
		btndormir.setToolTipText("DORMIR");

		panelBotones.add(btnalimentar);
		panelBotones.add(btnjugar);
		panelBotones.add(btndormir);

		BotonCircular botonpercha = new BotonCircular("fotos/PERCHA.png", 30);
		BotonCircular botonusuario = new BotonCircular("fotos/USUARIO.png", 30);

		JPanel zonaboton = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		zonaboton.add(botonusuario);
		zonaboton.add(botonpercha);

		panelTamagotchi.add(zonaboton, BorderLayout.NORTH);

		// Listeners de botones superiores
		ActionListener listenerUsuario = e -> cambiarUsuario();
		botonusuario.addActionListener(listenerUsuario);

		ActionListener listenerAlimentar = e -> irAComida();
		btnalimentar.addActionListener(listenerAlimentar);

		// Listener para abrir la pantalla de juegos pasando el color de la mascota
		ActionListener listenerJugar = e -> irAJuegos();
		btnjugar.addActionListener(listenerJugar);

		// Listener para abrir la pantalla de percha
		ActionListener listenerPercha = e -> iraPercha();
		botonpercha.addActionListener(listenerPercha);

		panelCentro.add(panelTamagotchi);
		panelCentro.add(panelInfo);

		// Oscurecedor de pantalla
		oscurecedor = new PanelOscuro(90);
		oscurecedor.setVisible(false);

		JLayeredPane capas = new JLayeredPane();
		capas.setLayout(null);
		capas.add(panelCentro, Integer.valueOf(0));
		capas.add(oscurecedor, Integer.valueOf(1));

		capas.addComponentListener(new java.awt.event.ComponentAdapter() {
			@Override
			public void componentResized(java.awt.event.ComponentEvent e) {
				panelCentro.setBounds(0, 0, capas.getWidth(), capas.getHeight());
				oscurecedor.setBounds(0, 0, capas.getWidth(), capas.getHeight());
			}
		});

		// Listener del botón dormir
		ActionListener listenerDormir = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (dormido) {
					dormido = false;
					oscurecedor.setVisible(false);
					btndormir.setText("DORMIR");
					btndormir.setToolTipText("DORMIR");

					if (hiloenergia != null) {
						hiloenergia.interrupt();
					}
				} else {
					dormido = true;
					oscurecedor.setVisible(true);
					btndormir.setText("DESPERTAR");
					btndormir.setToolTipText("DESPERTAR");

					hiloenergia = new Hiloenergia();
					hiloenergia.start();
				}
			}
		};

		btndormir.addActionListener(listenerDormir);

		panelPrincipal.add(capas, BorderLayout.CENTER);
		panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
		this.add(panelPrincipal);
	}

	private JPanel crearFila(String texto, String rutaIcono, int valor, Color color, JProgressBar barra) {
		Image img = new ImageIcon(rutaIcono).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		JLabel etiqueta = new JLabel(texto, new ImageIcon(img), JLabel.LEFT);
		etiqueta.setHorizontalTextPosition(JLabel.LEFT);
		etiqueta.setPreferredSize(new Dimension(120, 0));

		barra.setValue(valor);
		barra.setForeground(color);
		barra.setStringPainted(true);
		barra.setString(valor + " / 100");

		JPanel fila = new JPanel(new BorderLayout(10, 0));
		fila.add(etiqueta, BorderLayout.WEST);
		fila.add(barra, BorderLayout.CENTER);
		return fila;
	}

	private void irAComida() {
		new Comida().setVisible(true);
		this.dispose();
	}

	// Pasa el color actual guardado en Inicio a la pantalla JuegosClase
	private void irAJuegos() {
		new JuegosClase(this, this.colorMascota).setVisible(true);
		this.dispose();
	}

	private void cambiarUsuario() {
		new PrimeraVentana().setVisible(true);
		this.dispose();
	}

	private void iraPercha() {
		new Percha().setVisible(true);
		this.dispose();
	}

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

	// Permite actualizar el color de la mascota y repintar la zona
	public void setColorSeleccionado(Color colorMascota) {
		this.colorMascota = colorMascota;
		panelTamagotchi.setBackground(colorMascota);
		panelTamagotchi.repaint();
	}

	public class Hiloenergia extends Thread {
		@Override
		public void run() {
			while (!isInterrupted() && barraenergia.getValue() < 100) {
				for (int i = 0; i < 100; i++) {
					int value = i;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						this.interrupt();
						return;
					}
					SwingUtilities.invokeLater(() -> {
						barraenergia.setValue(value);
						barraenergia.setString(value + " / 100");
					});
				}
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Inicio login = new Inicio();
			login.setVisible(true);
		});
	}
}