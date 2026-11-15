package tamagotchi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

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
		this.setLocationRelativeTo(inicio);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		//Creación de los paneles
		JPanel panelPrincipal = new JPanel(new BorderLayout());
		JPanel panelPodio = new JPanel (new BorderLayout());
		JPanel panelTabla = new JPanel(new BorderLayout());
		
		//Añadir la imagen del podio
		ImageIcon imagenPodio = new ImageIcon("fotos/podio.png");
		JLabel labelImagen = new JLabel("");
		labelImagen.setIcon(imagenPodio); 
		labelImagen.setHorizontalAlignment(SwingConstants.CENTER);
		labelImagen.setVerticalAlignment(SwingConstants.CENTER); 
		panelPodio.add(labelImagen, BorderLayout.CENTER);
		panelPodio.setPreferredSize(new Dimension(400, 220));
		panelPrincipal.add(panelPodio, BorderLayout.NORTH);
		
		
		//Creación de la tabla
		JTable tabla = new JTable(new ModeloTablaRanking());
		tabla.setRowHeight(35);
		tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		//Creación del ScrollPane
		JScrollPane scrollPane = new JScrollPane(tabla);
		panelTabla.add(scrollPane, BorderLayout.CENTER);
		
		//Creación del renderizador
		DefaultTableCellRenderer renderizador = new DefaultTableCellRenderer() {
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				// TODO Auto-generated method stub
				JLabel resultado = new JLabel(value.toString());
				resultado.setHorizontalAlignment(JLabel.CENTER);
				resultado.setFont(new Font("Monospaced", Font.BOLD, 21));
                resultado.setForeground(Color.BLACK);
                resultado.setBackground(new Color(220, 220, 220));
                resultado.setOpaque(true);
				return resultado;
			}	
		};
		tabla.setDefaultRenderer(Object.class, renderizador);
		
		//Renderizador de la cabecera
		DefaultTableCellRenderer cabecera = new DefaultTableCellRenderer() {

		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value,
		            boolean isSelected, boolean hasFocus, int row, int column) {

		        JLabel resultado = new JLabel(value.toString());

		        resultado.setHorizontalAlignment(JLabel.CENTER);
		        resultado.setFont(new Font("Monospaced", Font.BOLD, 18));
		        resultado.setBackground(Color.LIGHT_GRAY);
		        resultado.setForeground(Color.BLACK);
		        resultado.setOpaque(true);

		        // Borde derecho e inferior de cada columna
		        resultado.setBorder(BorderFactory.createMatteBorder(
		                0, 0, 1, 1, Color.WHITE));

		        return resultado;
		    }
		};

		tabla.getTableHeader().setDefaultRenderer(cabecera);

		// Altura de la cabecera
		tabla.getTableHeader().setPreferredSize(new Dimension(0, 45));

		// Mostrar las líneas de separación
		tabla.getTableHeader().setReorderingAllowed(false);	
        
		//Añadir panel
        panelPrincipal.add(panelTabla, BorderLayout.CENTER);

        this.add(panelPrincipal);	
        
	}
}
