package tamagotchi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


public class Percha extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Color blanco = Color.white;
	private Color amarillo = Color.yellow;
	private Color naranja = Color.orange;
	private Color rojo = Color.red;
	private Color rosa = Color.pink;
	private Color morado = new Color(105, 12, 181);
	private Color azul = Color.BLUE;
	private Color mentoso = new Color(15, 245, 231);
	private Color verde = Color.green;
	private Color negro = Color.black;
	private int cantidadBotones = 9;
	private Font fuenteTitulo = new Font("Monospaced", Font.BOLD, 14);
	
	private List<Color> listaColores = new ArrayList<Color>();
	
	public Percha() {
		// TODO Auto-generated constructor stub
		
		//AÑADIR LOS COLORES A LA LISTA PARA PODER RECORRERLA LUEGO
		listaColores.add(blanco);
		listaColores.add(amarillo);
		listaColores.add(naranja);
		listaColores.add(rosa);
		listaColores.add(rojo);
		listaColores.add(morado);
		listaColores.add(azul);
		listaColores.add(mentoso);
		listaColores.add(verde);
		listaColores.add(negro);
		
		JPanel panelColores = new JPanel(new GridLayout(1, listaColores.size(), 1, 0));
		panelColores.setBorder(BorderFactory.createLineBorder(negro));
		panelColores.setPreferredSize(new Dimension(350, 30));
		for (Color color: listaColores) {
			JLabel labelColor = new JLabel(" ");
			labelColor.setOpaque(true);
			labelColor.setBackground(color);
			panelColores.add(labelColor);
		}
		JPanel contenedorColores = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
		contenedorColores.add(panelColores);
		
		//tituluito
		JLabel tituloColores = new JLabel("COLOR MASCOTA", SwingConstants.CENTER);
		tituloColores.setFont(fuenteTitulo);
		JPanel zonaColores = new JPanel(new BorderLayout());
		zonaColores.add(tituloColores, BorderLayout.NORTH);
		zonaColores.add(contenedorColores, BorderLayout.CENTER); 
		
		//ZONA DE ARRIBA (COLOR Y TAMAGOTCHI)
		JPanel panelArriba = new JPanel(new BorderLayout());
		
		//RELACIONADO CON BOTONES
		JLabel tituloAccesorios = new JLabel("ACCESORIOS", SwingConstants.CENTER);
		tituloAccesorios.setFont(fuenteTitulo);
		
		JPanel panelBotones = new JPanel(new GridLayout(3, 3, 5, 5));
		panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20)); //arriba, izquierda, abajo, derecha
		for (int i=0; i<cantidadBotones; i++) {
			JButton boton = new JButton("l");
			boton.setBackground(blanco);
			boton.setBorder(BorderFactory.createLineBorder(blanco));
			panelBotones.add(boton);
		}
		JPanel zonaBotones = new JPanel(new BorderLayout());
		zonaBotones.add(tituloAccesorios, BorderLayout.NORTH);
		zonaBotones.add(panelBotones, BorderLayout.CENTER);
		
		//BOTON PARA IR A PANTALLA PRINCIPAL 
		JButton botonInicio = new JButton("⌂");
		botonInicio.setFont(new Font("SansSerif", Font.BOLD, 26));
		botonInicio.setPreferredSize(new Dimension(50, 50));
		botonInicio.setFocusPainted(false);
		botonInicio.addActionListener(e -> {
		    // aqui tendriamos que abrir la pantalla principal
		    // new PantallaPrincipal().setVisible(true);
		    // this.dispose();
		});
		
		JLabel textoInicio = new JLabel("INICIO", SwingConstants.CENTER);
		textoInicio.setFont(new Font("Monospaced", Font.PLAIN, 10));
		
		//usamos un box layout para poner alinear a la izquierda el boton de salir 
		JPanel zonaInicio = new JPanel();
		zonaInicio.setLayout(new BoxLayout(zonaInicio, BoxLayout.Y_AXIS));
		botonInicio.setAlignmentX(0.5f);
		textoInicio.setAlignmentX(0.5f);
		zonaInicio.add(botonInicio);
		zonaInicio.add(textoInicio);

		JPanel panelInicio = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		panelInicio.add(zonaInicio);
		
		//PARA LA MASCOTA
		JLabel mascota = new JLabel("(aquí la mascota)", SwingConstants.CENTER);
		
		//EDICION PANEL PRINCIPAL;
		JPanel panelPrincipal = new JPanel(new GridLayout(2, 1));
		panelPrincipal.add(panelArriba);
		panelPrincipal.add(zonaBotones);
		panelArriba.add(panelInicio, BorderLayout.NORTH);
		panelArriba.add(mascota, BorderLayout.CENTER);
		panelArriba.add(zonaColores, BorderLayout.SOUTH);
		
		//MAIN PANEL
		this.add(panelPrincipal);
		this.setSize(430, 550);
		this.setResizable(false);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(() -> {
         Percha percha = new Percha();
            percha.setVisible(true);
        });
	}

}