package tamagotchi;

import java.awt.BorderLayout;
import java.awt.Color;
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


import java.awt.Dimension;
import java.awt.FlowLayout;

public class Inicio extends JFrame{

	private static final long serialVersionUID = 1L;
	private boolean dormido = false;
	
	private JProgressBar barrahambre = new JProgressBar(0,100);
	private JProgressBar barraenergia = new JProgressBar(0,100);
	private JProgressBar barrafelicidad = new JProgressBar(0,100);
	
	private JPanel panelTamagotchi = new JPanel(new BorderLayout());
	
	private JPanel oscurecedor;
	
	private Hiloenergia hiloenergia;
	
	public Inicio() {
		// TODO Auto-generated constructor stub
		//Añadir el menu
		   this.setJMenuBar(new MenuInicio(
	                e -> cerrarJuego(),
	                e -> cambiarUsuario()));
		   
		
		this.setTitle("TAMAGOCHI - Inicio");
		this.setSize(430, 550);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//Creación de los paneles
		JPanel panelPrincipal = new JPanel(new BorderLayout());
		JPanel panelCentro = new JPanel (new GridLayout(2, 1, 0, 10));
		JPanel panelBotones = new JPanel (new GridLayout(1, 3, 10, 10));
		panelBotones.setPreferredSize(new java.awt.Dimension(0, 75));
		panelBotones.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		JPanel panelInfo = new JPanel(new GridLayout(3, 1, 0, 10));
		
		panelInfo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		panelInfo.add(crearFila("Hambre", "fotos/POLLO.png", 0, new Color(255, 152, 0), barrahambre));
		panelInfo.add(crearFila("Energia", "fotos/RAYO.png", 0, new Color(76, 175, 80), barraenergia));
		panelInfo.add(crearFila("Felicidad", "fotos/CORAZON.png", 0, new Color(63, 143, 208), barrafelicidad));
		
		panelTamagotchi.setBorder(BorderFactory.createTitledBorder("Estado"));
		panelInfo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(5, 10, 5, 10))); // IA para combinar
		
		//Creacion de botones
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
        //zonaboton.setAlignmentX(0.5f);
		zonaboton.add(botonusuario);
		zonaboton.add(botonpercha);

		panelTamagotchi.add(zonaboton, BorderLayout.NORTH);
		
        
		
		//Añadir listener a los botones
        ActionListener listenerUsuario = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarUsuario();
            }
        };

        botonusuario.addActionListener(listenerUsuario);
        
        
		ActionListener listenerAlimentar = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				irAComida();
			}
		};
		
		btnalimentar.addActionListener(listenerAlimentar);
		
		// Listener para abrir la pantalla de juegos
		ActionListener listenerJugar = new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        irAJuegos();
		    }
		};

		btnjugar.addActionListener(listenerJugar);
		
		//Listener para abrir la pantalla de percha
		ActionListener listenerPercha = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				iraPercha();
			}
		};
		
		botonpercha.addActionListener(listenerPercha);
		
		//Añadir todos los paneles
		
		panelCentro.add(panelTamagotchi);
		panelCentro.add(panelInfo);
		
		//PAra poder oscurecer
		oscurecedor = new PanelOscuro(90);   // 0 = nada, 255 = negro total
		oscurecedor.setVisible(false);
        
        // Contenedor con capas: el contenido abajo, el velo encima
        JLayeredPane capas = new JLayeredPane();
        capas.setLayout(null);
        capas.add(panelCentro, Integer.valueOf(0));
        capas.add(oscurecedor, Integer.valueOf(1));

        // El contenido y el velo ocupan siempre todo el contenedor
        capas.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                panelCentro.setBounds(0, 0, capas.getWidth(), capas.getHeight());
                oscurecedor.setBounds(0, 0, capas.getWidth(), capas.getHeight());
            }
        });
        
        //Creación del listener para el boton dormir
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
		// Label con texto a la izquierda e icono a su derecha
		Image img = new ImageIcon(rutaIcono).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		JLabel etiqueta = new JLabel(texto, new ImageIcon(img), JLabel.LEFT);
		etiqueta.setHorizontalTextPosition(JLabel.LEFT);
		etiqueta.setPreferredSize(new Dimension(120, 0)); // ancho fijo para alinear las barras

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
	
	private void irAJuegos() {
	    new JuegosClase().setVisible(true);
	    this.dispose(); 
	}
	
	private void cambiarUsuario() {
		// TODO Auto-generated method stub
	    new PrimeraVentana().setVisible(true);
	    this.dispose();
	}
	
	private void iraPercha() {
		new Percha().setVisible(true);
		this.dispose();
	}

	// Cierra el juego entero, pidiendo confirmacion antes.
	private void cerrarJuego() {
		int opcion = JOptionPane.showConfirmDialog(
	    this,
	    "¿Seguro que quieres cerrar el juego?",
	    "Cerrar juego",
	    JOptionPane.YES_NO_OPTION,
	    JOptionPane.QUESTION_MESSAGE);

	    if (opcion == JOptionPane.YES_OPTION) {
	    	System.exit(0);            // termina el proceso, no solo la ventana
	    }
	}
	
	public void setColorSeleccionado(Color colorMascota) {
		// TODO Auto-generated method stub
		colorMascota = colorMascota;
	    panelTamagotchi.setBackground(colorMascota);   // o lo que dibujes con el color
	    panelTamagotchi.repaint();
	}
	     
	public class Hiloenergia extends Thread{
    	@Override
    	public void run() {
    		
    		while(!isInterrupted() && barraenergia.getValue() < 100) {
    			for(int i = 0; i < 100; i++) { 	
    				int value = i;
    				try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO: handle exception
						this.interrupt();
						return;
					}
		    		// Actualizar la Progress Bar
    	    		SwingUtilities.invokeLater(() -> {
    	    		    barraenergia.setValue(value);
    	    		    barraenergia.setString(value + " / 100");
    	    		});
		    	}
    		}
    	}
    	
	
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(() -> {
	          Inicio login = new Inicio();
	            login.setVisible(true);
	        });
	}

	


}
