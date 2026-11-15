package tamagotchi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

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
	
	private BotonRedondo btnalimentar;
	private BotonRedondo btnjugar;
	private BotonRedondo btndormir;
	
	//para poder poner el antifaz
	private static final double ANCHO_ANTIFAZ = 0.6;
	private static final double ALTURA_OJOS = 0.45;
	
	

	// CONSTRUCTOR 1: Por defecto (si se abre el juego desde cero, asigna amarillo)
	public Inicio() {
		this(EstadoMascota.getColor());
	}

	// CONSTRUCTOR 2: Recibe el color seleccionado desde la pantalla Percha
	public Inicio(Color colorMascota) {
		// Asignamos el color recibido (o amarillo por seguridad si llega nulo)
		this.colorMascota = (colorMascota != null) ? colorMascota : new Color(255, 193, 7);
		EstadoMascota.setColor(this.colorMascota);

		// Añadir el menú
		this.setJMenuBar(new MenuInicio(
				e -> cerrarJuego(),
				e -> cambiarUsuario()));

		this.setTitle("TAMAGOCHI - Inicio");
		this.setSize(430, 550);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		hiloenergia = new Hiloenergia();

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

		panelInfo.add(crearFila("Hambre", "fotos/POLLO.png", 0, new Color(255, 152, 0), barrahambre));
		panelInfo.add(crearFila("Energia", "fotos/RAYO.png", 0, new Color(76, 175, 80), barraenergia));
		panelInfo.add(crearFila("Felicidad", "fotos/CORAZON.png", 0, new Color(63, 143, 208), barrafelicidad));

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
		BotonCircular botonusuario = new BotonCircular("fotos/USUARIO.png", 30);
		BotonCircular botonranking = new BotonCircular("fotos/COPA.png", 40);

		//Panel izquierdo: usuario + percha
		JPanel zonabotonIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		zonabotonIzq.add(botonusuario);
		zonabotonIzq.add(botonpercha);
		
		//Panel derecho: rankin
		JPanel zonabotonDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		zonabotonDer.add(botonranking);

		//Panel de botones que contiene ambos lados
		JPanel zonabotones = new JPanel(new BorderLayout());
		
		zonabotones.add(zonabotonIzq, BorderLayout.WEST);
		zonabotones.add(zonabotonDer, BorderLayout.EAST);
		panelTamagotchi.add(zonabotones, BorderLayout.NORTH);

		// Listeners de botones superiores
		ActionListener listenerUsuario = e -> cambiarUsuario();
		botonusuario.addActionListener(listenerUsuario);
		
		ActionListener listenerRanking = e -> abrirRanking();
		botonranking.addActionListener(listenerRanking);
		
		// Listener para abrir la pantalla de percha
		ActionListener listenerPercha = e -> iraPercha();
		botonpercha.addActionListener(listenerPercha);

		ActionListener listenerAlimentar = e -> irAComida();
		btnalimentar.addActionListener(listenerAlimentar);

		// Listener para abrir la pantalla de juegos pasando el color de la mascota
		ActionListener listenerJugar = e -> irAJuegos();
		btnjugar.addActionListener(listenerJugar);

		panelCentro.add(panelTamagotchi, BorderLayout.CENTER);
		panelCentro.add(panelInfo, BorderLayout.SOUTH);

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
					mostrarMascota();  //llamamos al metodo para el tema antifaz
		
				} else {
					btnalimentar.setEnabled(false);
					btnjugar.setEnabled(false);
					hiloenergia.dormir();
					oscurecedor.setVisible(true);
					btndormir.setText("DESPERTAR");
					btndormir.setToolTipText("DESPERTAR");
					mostrarMascota(); //llamamos al metodo para el tema atnfiaz

				
				}
			}
		};

		btndormir.addActionListener(listenerDormir);

		panelPrincipal.add(capas, BorderLayout.CENTER);
		panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
		this.add(panelPrincipal);
		mostrarMascota();
		hiloenergia.arrancarSiHaceFalta();

		
	
	}

	private void mostrarMascota() { //Este es el metodo que nos va a buscar el PNG según el color
		// TODO Auto-generated method stub
		 ImageIcon icono = EstadoMascota.componerMascota(200, 180);
		    if (icono == null) {
		        imagenMascota.setIcon(null);
		        imagenMascota.setText("falta imagen de mascota");
		        return;
		    }
		    if (hiloenergia.isDormido()) {
		    	ImageIcon antifaz = new ImageIcon("fotos/antifaz_dormir.png");
		    	//IAG (CLAUDE)
		    	if (antifaz.getIconWidth() > 0) {
		            int iw = icono.getIconWidth();
		            int ih = icono.getIconHeight();

		            BufferedImage conAntifaz = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB);
		            Graphics2D g = conAntifaz.createGraphics();
		            g.drawImage(icono.getImage(), 0, 0, null);

		            int aw = (int) (iw * 0.64);                               // ancho del antifaz
		            int ah = antifaz.getIconHeight() * aw / antifaz.getIconWidth();
		            int x = (iw - aw) / 2;
		            int y = (int) (ih * 0.49) - ah / 2; //para cambiar la altura donde esta  
		            //si queremos que eesté más arriba poner un numero más bajo
		            g.drawImage(antifaz.getImage(), x, y, aw, ah, null);
		            g.dispose();

		            icono = new ImageIcon(conAntifaz);
		        }//FIN DE USO DE IAG
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

	// Permite actualizar el color de la mascota y repintar la zona
	public void setColorSeleccionado(Color colorMascota) {
		this.colorMascota = colorMascota;
		panelTamagotchi.setBackground(colorMascota);
		mostrarMascota();
		panelTamagotchi.repaint();
	}

	public class Hiloenergia extends Thread {
		private volatile boolean dormido = false;
		private void despertarPorEnergia() {
		    hiloenergia.despertar();          // dormido = false + notifyAll()
		    oscurecedor.setVisible(false);
		    btndormir.setText("DORMIR");
		    btndormir.setToolTipText("DORMIR");
		    btnalimentar.setEnabled(true);
		    btnjugar.setEnabled(true);
		    SwingUtilities.invokeLater(()-> mostrarMascota());
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
	            notifyAll(); // despierta al hilo si estaba esperando
	        }

	        public synchronized boolean isDormido() {
	            return dormido;
	        }

		@Override
		public void run() {
		    while (!isInterrupted() && barraenergia.getValue() < 100) {
		        synchronized (this) {
		            while (!dormido && !isInterrupted()) {
		                try {
		                    wait();          // suelta el candado y espera el notify()
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
		            int v = barraenergia.getValue() + 1;
		            if (v > 100) {
		                v = 100;
		            }
		            barraenergia.setValue(v);
		            barraenergia.setString(v + " / 100");
		        });
		    }
		    if (barraenergia.getValue()>=100) {
		    	despertarPorEnergia();  
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