package tamagotchi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class Percha extends JFrame {

    private static final long serialVersionUID = 1L;

    private Font fuenteTitulo = new Font("Monospaced", Font.BOLD, 14);
    private int cantidadBotones = 9;

    private JComboBox<ColorMascota> comboColores;
    private Color colorSeleccionado = Color.white;

    // Cuando tengas la imagen, descomenta este campo:
    // private PanelMascota mascota;

    public Percha() {

        // MENU BAR (clase aparte, lo usamos en otras pantallas tb, si queremos poner el menu en otra pantalla ponemos esto
        // siempre vamos a tener que añadirlo ANTES de setSize(), porque la barra ocupa altura y como que si no no va.
        this.setJMenuBar(new MenuJuego(
                e -> irAInicio(),
                e -> cerrarJuego(),
                e -> cambiarUsuario()));

        // ---- ZONA DE COLORES CON COMBO ----
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
                // mascota.setColorMascota(colorSeleccionado);
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

        // ---- ACCESORIOS ----
        JLabel tituloAccesorios = new JLabel("ACCESORIOS", SwingConstants.CENTER);
        tituloAccesorios.setFont(fuenteTitulo);

        JPanel panelBotones = new JPanel(new GridLayout(3, 3, 5, 5));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        for (int i = 0; i < cantidadBotones; i++) {
            JButton boton = new JButton("l");
            boton.setBackground(Color.white);
            boton.setBorder(BorderFactory.createLineBorder(Color.white));
            panelBotones.add(boton);
        }
        JPanel zonaBotones = new JPanel(new BorderLayout());
        zonaBotones.add(tituloAccesorios, BorderLayout.NORTH);
        zonaBotones.add(panelBotones, BorderLayout.CENTER);

        // ---- BOTON INICIO ----
        JButton botonInicio = new JButton("⌂");
        botonInicio.setFont(new Font("SansSerif", Font.BOLD, 26));
        botonInicio.setPreferredSize(new Dimension(50, 50));
        botonInicio.setFocusPainted(false);
        botonInicio.addActionListener(e -> irAInicio());

        JLabel textoInicio = new JLabel("INICIO", SwingConstants.CENTER);
        textoInicio.setFont(new Font("Monospaced", Font.PLAIN, 10));

        JPanel zonaInicio = new JPanel();
        zonaInicio.setLayout(new BoxLayout(zonaInicio, BoxLayout.Y_AXIS));
        botonInicio.setAlignmentX(0.5f);
        textoInicio.setAlignmentX(0.5f);
        zonaInicio.add(botonInicio);
        zonaInicio.add(textoInicio);

        JPanel panelInicio = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelInicio.add(zonaInicio);

        // ---- MASCOTA ----
        // Por ahora no tenemos la mascota elegida y vamos a dejar esto comentado que si no da error.
        // mascota = new PanelMascota();
        // mascota.setColorMascota(colorSeleccionado);

        // Provisional, para que el hueco no se colapse mientras no hay imagen:
        JLabel huecoMascota = new JLabel("(aquí la mascota)", SwingConstants.CENTER);
        huecoMascota.setPreferredSize(new Dimension(200, 160));

        // ---- PANEL PRINCIPAL ----
        JPanel panelPrincipal = new JPanel(new GridLayout(2, 1));
        panelPrincipal.add(panelArriba);
        panelPrincipal.add(zonaBotones);
        panelArriba.add(panelInicio, BorderLayout.NORTH);
        panelArriba.add(huecoMascota, BorderLayout.CENTER);   // luego hay que poner aqui la mascota.
        panelArriba.add(zonaColores, BorderLayout.SOUTH);

        this.add(panelPrincipal);
        this.setSize(430, 580);       
        this.setResizable(false);
    }

    // ACCIONES DE LA PANTALLA, siempre que invoquemos un menu, vamos a tener que meter como actua cada opcion en esta clase.
    //Por que tal vez en el menu de la pantalla de inico tenemos que cambiar el ir a Inicio por no hacer nada, por que como ya estamos
    //ahi no vamos a querer que nos lo cambie. 

    private void cambiarUsuario() {
		// TODO Auto-generated method stub
    	 new PrimeraVentana().setVisible(true);
    	  this.dispose();
	}

	/** Vuelve a la pantalla principal. dispose() cierra SOLO esta ventana. */
    private void irAInicio() {
    	    new Inicio().setVisible(true);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Percha percha = new Percha();
            percha.setVisible(true);
        });
    }
}
