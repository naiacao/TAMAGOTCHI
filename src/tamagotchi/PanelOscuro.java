package tamagotchi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class PanelOscuro extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int cantidad;
	public PanelOscuro(int cantidad) {
		// TODO Auto-generated constructor stub
		this.cantidad = cantidad;
		setOpaque(false);
		
	}
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		 Graphics2D g2 = (Graphics2D) g.create();
	        g2.setColor(new Color(0, 0, 0, cantidad));
	        g2.fillRect(0, 0, getWidth(), getHeight());
	        g2.dispose();
	}
	/**
	 * Vale, lo que he puesto arriba es un metodo de la clase JPanel. Al panel le vamos a pasar un número
	 * Que va a ser el nivel del oscurecimiento que queremos (si no me equivoco lo normal es 90 (cuanto más alto mas oscuro)
	 *  o asi pero eso lo pongo en la otra clase.
	 * lo que se hace es a g, que es como la parte grafica CREO, hacerlo de un color 000 que es negro, y con cantidad que lo pasamos por 
	 * Podemos hacer eso por que el graphics2D nos da más funciones sobre los paneles. 
	 * g2.fillRect(0, 0, getWidth(), getHeight()); rellena un rectángulo desde la esquina superior izquierda (0,0) hasta el ancho 
	 * y alto actuales del panel. Es decir, pinta todo el panel. g2.dispose(); cierra la copia del pincel.
	 * 
	 */
	

}
