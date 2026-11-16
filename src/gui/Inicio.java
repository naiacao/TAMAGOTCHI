package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import domain.BotonCircular;
import domain.BotonRedondo;
import domain.EstadoMascota;
import domain.Musica;
import domain.PanelOscuro;

public class Inicio extends JFrame {

	private static final long serialVersionUID = 1L;

	// Guarda el color seleccionado de la mascota para aplicarlo aquí y enviarlo a la pantalla de juegos
	private Color colorMascota;

	private JProgressBar barrahambre = new JProgressBar(0, 100);
	private JProgressBar barraenergia = new JProgressBar(0, 100);
	private JProgressBar barrafelicidad = new JProgressBar(0, 100);

	private JPanel panelTamagotchi = new JPanel(new BorderLayout());
	private JLabel imagenMascota;
	private JPanel oscurecedor;
	private Hiloenergia hiloenergia;
	private Hilofelicidad hilofelicidad;
	private Hiloinactividad hiloinactividad;
	
	private BotonRedondo btnalimentar;
	private BotonRedondo btnjugar;
	private BotonRedondo btndormir;
	private BotonCircular btnmusica;

	// CONSTRUCTOR 1: Por defecto (si se abre el juego desde cero, asigna amarillo)
	public Inicio() {
		this(EstadoMascota.getColor());
	}

	// CONSTRUCTOR 2: Recibe el color seleccionado desde la pantalla Percha
	public Inicio(Color colorMascota) {
		this.colorMascota = (colorMascota != null) ? colorMascota : new Color(255, 193, 7);
		EstadoMascota.setColor(this.colorMascota);


		this.setTitle("TAMAGOCHI - Inicio");
		this.setSize(430, 550);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		hiloenergia = new Hiloenergia();
		hilofelicidad = new Hilofelicidad();

		// Creación de los paneles
		JPanel panelPrincipal = new JPanel(new BorderLayout());
		JPanel panelCentro = new JPanel(new BorderLayout(0, 10));
		JPanel panelBotones = new JPanel(new GridLayout(1, 3, 10, 10));
		panelBotones.setPreferredSize(new Dimension(0, 75));
		panelBotones.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		JPanel panelInfo = new JPanel(new GridLayout(3, 1, 0, 10));

		panelInfo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		panelInfo.add(crearFila("Hambre", "fotos/POLLO.png", EstadoMascota.getHambre(), new Color(255, 152, 0), barrahambre));
		panelInfo.add(crearFila("Energia", "fotos/RAYO.png", EstadoMascota.getEnergia(), new Color(76, 175, 80), barraenergia));
		panelInfo.add(crearFila("Felicidad", "fotos/CORAZON.png", EstadoMascota.getFelicidad(), new Color(63, 143, 208), barrafelicidad));

		panelTamagotchi.setBorder(BorderFactory.createTitledBorder("Estado"));
		imagenMascota = new JLabel("", SwingConstants.CENTER);
		imagenMascota.setPreferredSize(new Dimension(200, 180));
		imagenMascota.setOpaque(true);
		imagenMascota.setBackground(new Color(235, 235, 235));

		JPanel zonaMascota = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
		zonaMascota.add(imagenMascota);
		panelTamagotchi.add(zonaMascota, BorderLayout.CENTER);

		// Creación de botones
		btnalimentar = new BotonRedondo("ALIMENTAR", new Color(76, 175, 80), new Color(46, 125, 50));
		btnalimentar.setToolTipText("COMIDA");
		btnjugar = new BotonRedondo("JUGAR", new Color(63, 143, 208), new Color(31, 92, 143));
		btnjugar.setToolTipText("JUGAR");
		btndormir = new BotonRedondo("DORMIR", new Color(255, 193, 7), new Color(199, 145, 0));
		btndormir.setToolTipText("DORMIR");

		panelBotones.add(btnalimentar);
		panelBotones.add(btnjugar);
		panelBotones.add(btndormir);

		BotonCircular botonpercha = new BotonCircular("fotos/PERCHA.png", 30);
		botonpercha.setToolTipText("Percha");
		BotonCircular botonusuario = new BotonCircular("fotos/USUARIO.png", 30);
		botonusuario.setToolTipText("Cambiar usuario");
		BotonCircular botonranking = new BotonCircular("fotos/COPA.png", 40);
		botonranking.setToolTipText("Ranking");

		//BotonDeMusica
		btnmusica = new BotonCircular("fotos/MUSICA.png", 30);
		
		btnmusica.setToolTipText("Volumen música");
		
		// Panel izquierdo: usuario + percha
		JPanel zonabotonIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		zonabotonIzq.add(botonusuario);
		zonabotonIzq.add(botonpercha);
		
		// Panel derecho: ranking
		JPanel zonabotonDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		zonabotonDer.add(btnmusica);
		zonabotonDer.add(botonranking);

		// Panel de botones que contiene ambos lados
		JPanel zonabotones = new JPanel(new BorderLayout());
		zonabotones.add(zonabotonIzq, BorderLayout.WEST);
		zonabotones.add(zonabotonDer, BorderLayout.EAST);
		panelTamagotchi.add(zonabotones, BorderLayout.NORTH);

		// Listeners de botones superiores
		botonusuario.addActionListener(e -> cambiarUsuario());
		botonranking.addActionListener(e -> abrirRanking());
		botonpercha.addActionListener(e -> iraPercha());
		btnalimentar.addActionListener(e -> irAComida());
		btnjugar.addActionListener(e -> irAJuegos());

		panelCentro.add(panelTamagotchi, BorderLayout.CENTER);
		panelCentro.add(panelInfo, BorderLayout.SOUTH);

		//Mouse listener para el tamagochi
		imagenMascota.setCursor(new Cursor(Cursor.HAND_CURSOR));
		imagenMascota.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (hiloenergia.isDormido()) {
					JOptionPane.showMessageDialog(
							Inicio.this,
							"Zzz... (Está durmiendo)",
							"Mascota",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(
							Inicio.this,
							"¡Holaaa! ¿Qué tal?",
							"Mascota",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
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
				if (hiloenergia.isDormido()) {
					hiloenergia.despertar();
					oscurecedor.setVisible(false);
					btndormir.setText("DORMIR");
					btndormir.setToolTipText("DORMIR");
					btnalimentar.setEnabled(true);
					btnjugar.setEnabled(true);
					mostrarMascota();
				} else {
					if (EstadoMascota.getEnergia() >= 100) {
						JOptionPane.showMessageDialog(
								Inicio.this,
								"Tu mascota ya tiene la energía al máximo.");
						return;
					}
					btnalimentar.setEnabled(false);
					btnjugar.setEnabled(false);
					hiloenergia.dormir();
					oscurecedor.setVisible(true);
					btndormir.setText("DESPERTAR");
					btndormir.setToolTipText("DESPERTAR");
					mostrarMascota();
				}
			}
		};

		btndormir.addActionListener(listenerDormir);

		//Listener del boton musica
		btnmusica.addActionListener(e -> new VentanaMusica(this, Musica.getInstance()).setVisible(true));
		panelPrincipal.add(capas, BorderLayout.CENTER);
		panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
		this.add(panelPrincipal);
		mostrarMascota();
		hiloenergia.arrancarSiHaceFalta();
		hilofelicidad.start();
		hiloinactividad = new Hiloinactividad();
		hiloinactividad.start();
		Musica.getInstance().reproducir();
	}

	private void mostrarMascota() {
		ImageIcon icono = EstadoMascota.componerMascota(200, 180);
		if (icono == null) {
			imagenMascota.setIcon(null);
			imagenMascota.setText("falta imagen de mascota");
			return;
		}
		if (hiloenergia.isDormido()) {
			ImageIcon antifaz = new ImageIcon("fotos/antifaz_dormir.png");
			if (antifaz.getIconWidth() > 0) {
				int iw = icono.getIconWidth();
				int ih = icono.getIconHeight();

				//248-250 IA Claude
				BufferedImage conAntifaz = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = conAntifaz.createGraphics();
				g.drawImage(icono.getImage(), 0, 0, null);

				int aw = (int) (iw * 0.64);
				int ah = antifaz.getIconHeight() * aw / antifaz.getIconWidth();
				int x = (iw - aw) / 2;
				int y = (int) (ih * 0.49) - ah / 2;
				g.drawImage(antifaz.getImage(), x, y, aw, ah, null);
				g.dispose();

				icono = new ImageIcon(conAntifaz);
			}
		}
		imagenMascota.setIcon(icono);
		imagenMascota.setText("");
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

	private void detenerHilos() {
		if (hiloenergia != null) hiloenergia.interrupt();
		if (hilofelicidad != null) hilofelicidad.interrupt();
		if (hiloinactividad != null) hiloinactividad.interrupt();
	}

	private void irAComida() {
		if (EstadoMascota.getHambre() >= 100) {
			JOptionPane.showMessageDialog(
					this,
					"Tu mascota está llena, no tiene hambre ahora mismo.",
					"Sin hambre",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		detenerHilos();
		new Comida().setVisible(true);
		this.dispose();
	}

	// Pasa el color actual guardado en Inicio a la pantalla JuegosClase
	private void irAJuegos() {
		boolean sinEnergia = EstadoMascota.getEnergia() <= 0;
		boolean muchaHambre = EstadoMascota.getHambre() <= 0;

		if (sinEnergia && muchaHambre) {
			JOptionPane.showMessageDialog(
					this,
					"Tu mascota no tiene energía y además está hambrienta.\nDebe comer antes de hacer nada más.",
					"Sin energía y con hambre",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (muchaHambre) {
			JOptionPane.showMessageDialog(
					this,
					"Tu mascota tiene mucha hambre, por eso no puede jugar.\nHay que darle de comer.",
					"Hambre",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (sinEnergia) {
			JOptionPane.showMessageDialog(
					this,
					"No tienes energía, por eso no puedes jugar.\nDeja que tu mascota duerma para recuperarla.",
					"Sin energía",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		detenerHilos();
		EstadoMascota.registrarActividadJuego();
		new JuegosClase(this, this.colorMascota).setVisible(true);
		this.dispose();
	}

	private void cambiarUsuario() {
		detenerHilos();
		new PrimeraVentana().setVisible(true);
		this.dispose();
	}

	private void iraPercha() {
		detenerHilos();
		new Percha(this.colorMascota).setVisible(true);
		this.dispose();
	}
	
	private void abrirRanking() {
		Ranking ranking = new Ranking(this);
		ranking.setVisible(true);
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

	public void setColorSeleccionado(Color colorMascota) {
		this.colorMascota = colorMascota;
		panelTamagotchi.setBackground(colorMascota);
		mostrarMascota();
		panelTamagotchi.repaint();
	}

	public class Hiloenergia extends Thread {
		private volatile boolean dormido = false;
		private int ticksSueno = 0;
		
		private void despertarPorEnergia() {
			despertar();
			SwingUtilities.invokeLater(() -> {
				oscurecedor.setVisible(false);
				btndormir.setText("DORMIR");
				btndormir.setToolTipText("DORMIR");
				btnalimentar.setEnabled(true);
				btnjugar.setEnabled(true);
				mostrarMascota();
			});
		}
		 
		public synchronized void arrancarSiHaceFalta() {
			if (!isAlive()) {
				dormido = false;
				start();
			}
		}

		public synchronized void dormir() {
			dormido = true;
			notifyAll();
		}

		public synchronized void despertar() {
			dormido = false;
			notifyAll();
		}

		public synchronized boolean isDormido() {
			return dormido;
		}

		@Override
		public void run() {
			while (!isInterrupted()) {
				synchronized (this) {
					while (!dormido && !isInterrupted()) {
						try {
							wait();
						} catch (InterruptedException e) {
							return;
						}
					}
				}

				try {
					Thread.sleep(450);
				} catch (InterruptedException e) {
					return;
				}

				SwingUtilities.invokeLater(() -> {
					int v = Math.min(100, barraenergia.getValue() + 1);
					EstadoMascota.setEnergia(v);
					barraenergia.setValue(v);
					barraenergia.setString(v + " / 100");
					ticksSueno++;
					if (ticksSueno % 5 == 0) { // el hambre baja 1 punto por cada 5 de energía ganada
						int h = Math.max(0, EstadoMascota.getHambre() - 1);
						EstadoMascota.setHambre(h);
						barrahambre.setValue(h);
						barrahambre.setString(h + " / 100");
					}
				});

				if (barraenergia.getValue() >= 99 && isDormido()) {
					despertarPorEnergia();
				}
			}
		}
	}

	// Hilo que descarga la felicidad cuando lleva un rato sin jugar
	public class Hilofelicidad extends Thread {
		private static final long TIEMPO_ESPERA_SIN_JUGAR_MS = 45000; // 45 segundos sin jugar antes de empezar a bajar
		private static final long INTERVALO_DESCARGA_MS = 5000;       // baja 1 punto cada 5 segundos

		@Override
		public void run() {
			while (!isInterrupted()) {
				try {
					Thread.sleep(INTERVALO_DESCARGA_MS);
				
				} catch (InterruptedException e) {
					return;
				}

				long sinJugar = System.currentTimeMillis() - EstadoMascota.getUltimoJuego(); //el tiempo de ahora mismo
				if (sinJugar >= TIEMPO_ESPERA_SIN_JUGAR_MS && barrafelicidad.getValue() > 0) {
					SwingUtilities.invokeLater(() -> {
						int v = Math.max(0, barrafelicidad.getValue() - 1);
						EstadoMascota.setFelicidad(v);
						barrafelicidad.setValue(v);
						barrafelicidad.setString(v + " / 100");
					});
				}
			}
		}
	}
	
	public class Hiloinactividad extends Thread {
		private static final long TIEMPO_ESPERA_SIN_JUGAR_MS = 45000; // 45 s sin jugar antes de empezar a bajar
		private static final long INTERVALO_DESCARGA_MS = 5000;       // baja 1 punto cada 5 s

		@Override
		public void run() {
			while (!isInterrupted()) {
				try {
					Thread.sleep(INTERVALO_DESCARGA_MS);
				} catch (InterruptedException e) {
					return;
				}

				long sinJugar = System.currentTimeMillis() - EstadoMascota.getUltimoJuego();
				boolean durmiendo = hiloenergia.isDormido();

				if (!durmiendo && sinJugar >= TIEMPO_ESPERA_SIN_JUGAR_MS) {
					SwingUtilities.invokeLater(() -> {
						int e = Math.max(0, EstadoMascota.getEnergia() - 1);
						EstadoMascota.setEnergia(e);
						barraenergia.setValue(e);
						barraenergia.setString(e + " / 100");

						int h = Math.max(0, EstadoMascota.getHambre() - 1);
						EstadoMascota.setHambre(h);
						barrahambre.setValue(h);
						barrahambre.setString(h + " / 100");
					});
				}
			}
		}
	}


}