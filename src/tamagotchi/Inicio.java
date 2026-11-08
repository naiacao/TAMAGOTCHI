package tamagotchi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import java.awt.Dimension;

public class Inicio extends JFrame{

	private static final long serialVersionUID = 1L;

	public Inicio() {
		// TODO Auto-generated constructor stub
		this.setTitle("TAMAGOCHI - LogIn");
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
		JPanel panelTamagotchi = new JPanel();
		JPanel panelInfo = new JPanel(new GridLayout(3, 1, 0, 10));
		
		panelInfo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		panelInfo.add(crearFila("Hambre", "fotos/POLLO.png", 0, new Color(255, 152, 0)));
		panelInfo.add(crearFila("Energia", "fotos/RAYO.png", 0, new Color(76, 175, 80)));
		panelInfo.add(crearFila("Felicidad", "fotos/CORAZON.png", 0, new Color(63, 143, 208)));
		
		panelTamagotchi.setBorder(BorderFactory.createTitledBorder("Estado"));
		panelInfo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(5, 10, 5, 10))); // IA para combinar
		
		//Creacion de botones
		BotonRedondo btnalimentar = new BotonRedondo("ALIMENTAR", new Color(76, 175, 80), new Color(46, 125, 50));
		BotonRedondo btnjugar = new BotonRedondo("JUGAR", new Color(63, 143, 208), new Color(31, 92, 143));
		BotonRedondo btndormir = new BotonRedondo("DORMIR", new Color(255, 193, 7), new Color(199, 145, 0));
		panelBotones.add(btnalimentar);
		panelBotones.add(btnjugar);
		panelBotones.add(btndormir);
		
		//Añadir todos los paneles
		
		panelCentro.add(panelTamagotchi);
		panelCentro.add(panelInfo);
		panelPrincipal.add(panelCentro, BorderLayout.CENTER);
		panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
		this.add(panelPrincipal);
	}
	
	private JPanel crearFila(String texto, String rutaIcono, int valor, Color color) {
		// Label con texto a la izquierda e icono a su derecha
		Image img = new ImageIcon(rutaIcono).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		JLabel etiqueta = new JLabel(texto, new ImageIcon(img), JLabel.LEFT);
		etiqueta.setHorizontalTextPosition(JLabel.LEFT);
		etiqueta.setPreferredSize(new Dimension(120, 0)); // ancho fijo para alinear las barras

		JProgressBar barra = new JProgressBar(0, 100);
		barra.setValue(valor);
		barra.setForeground(color);
		barra.setStringPainted(true);
		barra.setString(valor + " / 100");

		JPanel fila = new JPanel(new BorderLayout(10, 0));
		fila.add(etiqueta, BorderLayout.WEST);
		fila.add(barra, BorderLayout.CENTER);
		return fila;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(() -> {
	          Inicio login = new Inicio();
	            login.setVisible(true);
	        });
	}


}
