package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import domain.ColorMascota;
import domain.ColorRenderer;
import domain.EstadoMascota;

public class Percha extends JFrame {

    private static final long serialVersionUID = 1L;

    private Font fuenteTitulo = new Font("Monospaced", Font.BOLD, 14);
    private int cantidadBotones = 9;

    private JComboBox<ColorMascota> comboColores;
    private JLabel imagenMascota;
    private Color colorSeleccionado;
    
    private String[] gorros = {
    		"gorro1", "gorro2", "gorro3", "gorro4", "gorro5", "gorro6", "gorro7", "gorro8", "gorro9"
    };
    private String rutaMascotaActual;
    private String gorroActual = EstadoMascota.getGorro();
    
    //Ajustes para el tamaño del gorro un poco a ojo
    private static final double ANCHO_GORRO   = 0.42; // ancho del gorro respecto a la mascota (pequeño)
    private static final double ALTURA_CABEZA = 0.12; // donde esta lo alto de la cabeza (0 = arriba del todo, 1 = abajo)
    private static final int    AJUSTE_X      = 0;    // mover a izquierda (-) o derecha (+), en pixeles
    private static final int    AJUSTE_Y      = 0;    // mover arriba (-) o abajo (+), en pixeles
    private static final double MARGEN_ARRIBA = 0.30;
    public Percha() {
    	this(EstadoMascota.getColor());
    }

    public Percha(Color colorInicial) {
    	this.colorSeleccionado = colorInicial;
        // MENU BAR (clase aparte, lo usamos en otras pantallas tb, si queremos poner el menu en otra pantalla ponemos esto
        // siempre vamos a tener que añadirlo ANTES de setSize(), porque la barra ocupa altura y como que si no no va.
        this.setJMenuBar(new MenuJuego(
                e -> irAInicio(),
                e -> cerrarJuego(),
                e -> cambiarUsuario()));

        //ZONA DE COLORES CON COMBO
        comboColores = new JComboBox<>(new ColorMascota[]{
            new ColorMascota("Blanco",   Color.white),
            new ColorMascota("Amarillo", Color.yellow),
            new ColorMascota("Naranja",  Color.orange),
            new ColorMascota("Rosa",     Color.pink),
            new ColorMascota("Rojo",     Color.red),
            new ColorMascota("Morado",   new Color(105, 12, 181)), //le hemos pedido a la ia los numeros de este color
            new ColorMascota("Azul",     Color.blue),
            new ColorMascota("Mentoso",  new Color(15, 245, 231)), //le hemos pedido a la ia los numeros de este color
            new ColorMascota("Verde",    Color.green),
            new ColorMascota("Negro",    Color.black)
        });
        comboColores.setRenderer(new ColorRenderer());
        comboColores.setMaximumRowCount(10);
        comboColores.setPreferredSize(new Dimension(200, 28));
        comboColores.addActionListener(e -> {
            ColorMascota sel = (ColorMascota) comboColores.getSelectedItem();
            if (sel != null) {
                colorSeleccionado = sel.getColor();
                EstadoMascota.setColor(colorSeleccionado);
                mostrarMascota(sel.getRutaImagen());
            }
        });

        JPanel contenedorColores = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        contenedorColores.add(comboColores);

        JLabel tituloColores = new JLabel("COLOR MASCOTA", SwingConstants.CENTER);
        tituloColores.setFont(fuenteTitulo);
        JPanel zonaColores = new JPanel(new BorderLayout());
        zonaColores.add(tituloColores, BorderLayout.NORTH);
        zonaColores.add(contenedorColores, BorderLayout.CENTER);

        // ---- ZONA DE ARRIBA ----
        JPanel panelArriba = new JPanel(new BorderLayout());

        // ACCESORIOS 
        JLabel tituloAccesorios = new JLabel("ACCESORIOS", SwingConstants.CENTER);
        tituloAccesorios.setFont(fuenteTitulo);

        JPanel panelBotones = new JPanel(new GridLayout(3, 3, 5, 5));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        for (int i = 0; i < cantidadBotones; i++) {
            JButton boton = new JButton();
            boton.setBackground(Color.white);
            boton.setBorder(BorderFactory.createLineBorder(Color.white));
            boton.setFocusPainted(false); //mirar un poco esto
            if (i< gorros.length) {
            	final String nombre = gorros[i];
                ImageIcon ic = new ImageIcon("gorros/" + nombre + ".png");
                if (ic.getIconWidth() > 0) {
                    // miniatura manteniendo proporcion
                    double f = Math.min(60.0 / ic.getIconWidth(), 60.0 / ic.getIconHeight());
                    boton.setIcon(new ImageIcon(ic.getImage().getScaledInstance(
                            (int) (ic.getIconWidth() * f),
                            (int) (ic.getIconHeight() * f),
                            Image.SCALE_SMOOTH)));
            }else {
            	boton.setText("?"); //porseacaso hemos puesto mal la ruta o lo que sea 
            }
                boton.addActionListener(e -> {
                    // si pulsas el que ya llevas puesto, se lo quitas
                    gorroActual = nombre.equals(gorroActual) ? null : nombre;
                    EstadoMascota.setGorro(gorroActual);
                    refrescarMascota();
                });}
            panelBotones.add(boton);
        }
        JPanel zonaBotones = new JPanel(new BorderLayout());
        zonaBotones.setPreferredSize(new Dimension(390, 300));
        zonaBotones.add(tituloAccesorios, BorderLayout.NORTH);
        zonaBotones.add(panelBotones, BorderLayout.CENTER);

       
        // MASCOTA

        // Provisional, para que el hueco no se colapse mientras no hay imagen:
        imagenMascota = new JLabel("", SwingConstants.CENTER);

        // ---- PANEL PRINCIPAL ----
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.add(panelArriba);
        panelPrincipal.add(zonaBotones);
        panelArriba.add(imagenMascota, BorderLayout.CENTER);   // luego hay que poner aqui la mascota.
        panelArriba.add(zonaColores, BorderLayout.SOUTH);

        this.add(panelPrincipal);
        this.setSize(430, 580);       
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        seleccionarColorEnCombo(colorSeleccionado);
    }
    
    private void refrescarMascota() {
		// TODO Auto-generated method stub
    	if (rutaMascotaActual == null) 
    		return;

        ImageIcon base = new ImageIcon(rutaMascotaActual);
        if (base.getIconWidth() <= 0) {
            imagenMascota.setIcon(null);
            imagenMascota.setText("falta " + rutaMascotaActual);
            return;
        }

        int w = base.getIconWidth();
        int h = base.getIconHeight();
        int margen = (int) (h*MARGEN_ARRIBA);

        //Ahora la zona la hemos hecho más grande y así el gorro no sale cortado IA Claude
        BufferedImage compuesta = new BufferedImage(w, h + margen, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = compuesta.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        // capa 1: la mascota
        g.drawImage(base.getImage(), 0, margen, null);

     // capa 2: el gorro, centrado y apoyado sobre la cabeza
        if (gorroActual != null) {
            ImageIcon gorro = new ImageIcon("gorros/" + gorroActual + ".png");
            if (gorro.getIconWidth() > 0) {
                int gw = (int) (w * ANCHO_GORRO);
                int gh = gorro.getIconHeight() * gw / gorro.getIconWidth(); // mantiene proporcion
                int x = (w - gw) / 2 + AJUSTE_X;
                int y = margen + (int) (h * ALTURA_CABEZA) - gh + AJUSTE_Y;
                g.drawImage(gorro.getImage(), x, y, gw, gh, null);
            }
        }
        g.dispose();

        // escalar para que quepa en el hueco
        double factor = Math.min(220.0 / w, 180.0 / h);
        Image escalada = compuesta.getScaledInstance(
                (int) (w * factor), (int) (h * factor), Image.SCALE_SMOOTH);
        imagenMascota.setIcon(new ImageIcon(escalada));
        imagenMascota.setText("");
    }
	

	private void seleccionarColorEnCombo(Color colorSeleccionado2) {
		// TODO Auto-generated method stub
		for (int i=0; i<comboColores.getItemCount(); i++) {
			if (comboColores.getItemAt(i).getColor().equals(colorSeleccionado2)) {
				comboColores.setSelectedIndex(i);
				return;
			}
		}
		comboColores.setSelectedIndex(0);
	}

	private void mostrarMascota(String rutaImagen) {
		// TODO Auto-generated method stub
		  rutaMascotaActual = rutaImagen;
		    refrescarMascota();
		
	}

	// ACCIONES DE LA PANTALLA, siempre que invoquemos un menu, vamos a tener que meter como actua cada opcion en esta clase.
    //Por que tal vez en el menu de la pantalla de inico tenemos que cambiar el ir a Inicio por no hacer nada, por que como ya estamos
    //ahi no vamos a querer que nos lo cambie. 

    private void cambiarUsuario() {
		// TODO Auto-generated method stub
    	 new PrimeraVentana().setVisible(true);
    	  this.dispose();
    	  //Mas adelante mirar si queremos que un usuario empiece sin gorro poner
    	  //EstadoMascota.setGorro(null);
	}

	/** Vuelve a la pantalla principal. dispose() cierra SOLO esta ventana. */
    private void irAInicio() {
    	    new Inicio(this.colorSeleccionado).setVisible(true);
    	    this.dispose();
    	
    }

    /** Cierra el juego entero, pidiendo confirmacion antes. */
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

    public Color getColorSeleccionado() { return colorSeleccionado; }

}
