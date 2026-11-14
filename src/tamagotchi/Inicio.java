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

		
				} else {
					btnalimentar.setEnabled(false);
					btnjugar.setEnabled(false);
					hiloenergia.dormir();
					oscurecedor.setVisible(true);
					btndormir.setText("DESPERTAR");
					btndormir.setToolTipText("DESPERTAR");
					

				
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
		String nombre = nombreArchivoColor(colorMascota);
		ImageIcon icono = new ImageIcon("fotos/"+ nombre + ".png");
		if (icono.getIconWidth() <= 0) {
	        imagenMascota.setIcon(null);
	        imagenMascota.setOpaque(true);
	        imagenMascota.setBackground(new Color(235, 235, 235));
	        imagenMascota.setText("falta fotos/" + nombre + ".png");
	        return;
	    } 
		// Ahora es más o menos lo mismo que lo de percha, para el tema tamaño y tal
		int anchoMax = 200, altoMax = 180;
	    int w = icono.getIconWidth(), h = icono.getIconHeight();
	    double factor = Math.min((double) anchoMax / w, (double) altoMax / h);
	    int nuevoW = (int) (w * factor);
	    int nuevoH = (int) (h * factor);

	    Image escalada = icono.getImage().getScaledInstance(nuevoW, nuevoH, Image.SCALE_REPLICATE);
	    imagenMascota.setIcon(new ImageIcon(escalada));
	    imagenMascota.setText("");
		
	}

	private String nombreArchivoColor(Color c) {
		// TODO Auto-generated method stub
		if (c.equals(Color.white))            return "Blanco";
	    if (c.equals(Color.yellow))           return "Amarillo";
	    if (c.equals(Color.orange))           return "Naranja";
	    if (c.equals(Color.pink))             return "Rosa";
	    if (c.equals(Color.red))              return "Rojo";
	    if (c.equals(new Color(105, 12, 181))) return "Morado";
	    if (c.equals(Color.blue))             return "Azul";
	    if (c.equals(new Color(15, 245, 231))) return "Mentoso";
	    if (c.equals(Color.green))            return "Verde";
	    if (c.equals(Color.black))            return "Negro";
	    return "Blanco";
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