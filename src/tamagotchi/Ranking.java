package tamagotchi;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class Ranking extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Ranking(JFrame inicio) {
		// TODO Auto-generated constructor stub
		super(inicio, "Tamagochi - Ranking", true); // lo de true, hace que se bloquee la ventana principal, en este caso no nos dejaría usar
		//la de inicio mientras  tenemos el ranking abierto.
		this.setResizable(false);
		this.setSize(400, 500);
		
		//Creación de los paneles
		JPanel panelPrincipal = new JPanel(new GridLayout(2, 1));
		JPanel panelPodio = new JPanel (new BorderLayout());
		JPanel panelTabla = new JPanel(new BorderLayout());
		
		//Añadir la imagen del podio
		ImageIcon imagenPodio = new ImageIcon("fotos/podio.png");
		JLabel labelImagen = new JLabel("");
		labelImagen.setIcon(imagenPodio); 
		labelImagen.setHorizontalAlignment(SwingConstants.CENTER);
		labelImagen.setVerticalAlignment(SwingConstants.CENTER); 
		panelPodio.add(labelImagen, BorderLayout.CENTER);
		panelPrincipal.add(panelPodio);
		
		//Creación de la tabla
		JTable tabla = new JTable(new ModeloTablaRanking());
		panelTabla.add(tabla, BorderLayout.CENTER);
		panelPrincipal.add(panelTabla);
		//Añadir panel
		this.add(panelPrincipal);
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Inicio inicio = new Inicio();
			Ranking login = new Ranking(inicio);
			login.setVisible(true);
		});
	}
}
