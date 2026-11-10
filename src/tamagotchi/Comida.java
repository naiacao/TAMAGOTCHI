package tamagotchi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class Comida extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Comida() {
		// TODO Auto-generated constructor stub
		String[][] alimentos = {
				{"Pizza", "10", ""},
				{"Brocoli", "30", ""},
				{"Esparrago", "25", ""},
				{"Hamburguesa", "10", ""},
				{"Kebab", "5", ""},
				{"Manzana", "10", ""},
				{"Helado", "15", ""},
				{"Platano", "15", ""},
				{"Huevo", "20", ""},
				{"Patatas fritas", "5", ""},
				{"Sandia", "20", ""},
				{"Chocolate", "5", ""},
				{"Cerveza", "5", ""},
				{"Agua", "10", ""},
				{"Zumo", "15", ""}
		};
		
		
		//Añadir el menu
		   this.setJMenuBar(new MenuJuego(
	                e -> irAInicio(),
	                e -> cerrarJuego(),
	                e -> cambiarUsuario()));
		
		
		JPanel panelPrincipal = new JPanel(new GridLayout(2, 1));
		
		
		
		//parte arriba con muñeco
		JPanel parteMuñeco = new JPanel(new BorderLayout());
		
		
		//parte aajo comida
		JPanel borderComida = new JPanel(new BorderLayout());
		JLabel titulo = new JLabel("ALIMENTOS");
		titulo.setHorizontalAlignment(JLabel.CENTER);
		borderComida.add(titulo, BorderLayout.NORTH);
		
		JPanel gridComida = new JPanel(new GridLayout(5, 3, 10, 10));
		gridComida.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20)); //arriba, izq, abajo, der
		
		for(int i=0; i<alimentos.length; i++ ) {
			String comida = alimentos[i][0];
			String puntosComida = alimentos[i][1];
			String icono = alimentos[i][2];
			
			
			//IA 62-66
			JButton boton = new JButton("<html><center>"
					+ "<img src='" + icono + "'><br>"
					+ "<b>" + comida + "</b><br>"
					+ "<small>" + puntosComida + "</small>"
					+ "</center></html>");
			boton.setBackground(Color.white);
			gridComida.add(boton);
			
						
			
		}
		
		borderComida.add(gridComida, BorderLayout.CENTER);
		JScrollPane scrollPane = new JScrollPane(gridComida);
		scrollPane.setBorder(null);
		borderComida.add(scrollPane, BorderLayout.CENTER);

		
		panelPrincipal.add(parteMuñeco);
		panelPrincipal.add(borderComida);		
		
		
		//Panel principal
		this.add(panelPrincipal);
		this.setSize(430,550);
		this.setVisible(true);
		this.setResizable(false);
		this.setTitle("TAMAGOCHI - comida");
		this.setLocationRelativeTo(null);
	}
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
	
	public static void main(String[] args) {

		// TODO Auto-generated method stub

		SwingUtilities.invokeLater(() -> {

         Comida comida = new Comida();

            comida.setVisible(true);

        });

	}
	

}
