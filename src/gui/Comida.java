package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import domain.EstadoMascota;
import domain.Mascota;

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
				{"Esparragos", "25", ""},
				{"Hamburguesa", "10", ""},
				{"Kebab", "5", ""},
				{"Manzana", "10", ""},
				{"Helado", "15", ""},
				{"Platano", "15", ""},
				{"Huevo", "20", ""},
				{"Nachos", "5", ""},
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
		JLabel imagenMascota = new JLabel ("", JLabel.CENTER); //Se puede poner swing constats tb que es lo mismo
		imagenMascota.setIcon(EstadoMascota.componerMascota(200, 200));
		parteMuñeco.add(imagenMascota, BorderLayout.CENTER);
		imagenMascota.setLayout(new BorderLayout());
		
		//comida en la boca
		JPanel panelBoca = new JPanel(new GridLayout(1,3));
		panelBoca.setOpaque(false);
		JLabel hueco = new JLabel("");
		JLabel comidaBoca = new JLabel("", JLabel.CENTER);
		comidaBoca.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
		panelBoca.add(hueco);
		panelBoca.add(comidaBoca);
		
		imagenMascota.add(panelBoca, BorderLayout.CENTER);
		parteMuñeco.add(imagenMascota, BorderLayout.CENTER);
		
		// texto ñam
		JPanel panelNam = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 75));
		panelNam.setOpaque(false);
		JLabel nam = new JLabel("");
		nam.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
		nam.setBackground(Color.white);
		nam.setOpaque(false);
		panelNam.add(nam);
		panelBoca.add(panelNam);
		
		
		//parte abajo comida
		JPanel borderComida = new JPanel(new BorderLayout());
		JLabel titulo = new JLabel("ALIMENTOS");
		titulo.setHorizontalAlignment(JLabel.CENTER);
		borderComida.add(titulo, BorderLayout.NORTH);
		
		JPanel gridComida = new JPanel(new GridLayout(5, 3, 10, 10));
		gridComida.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20)); //arriba, izq, abajo, der
		
		for(int i=0; i<alimentos.length; i++ ) {			
			String comida = alimentos[i][0];
			String puntosComida = alimentos[i][1];
			String foto = alimentos[i][2];
			
			
			
			//IA 106-110 "Gemini"
			JButton boton = new JButton("<html><center><b>" 
			+ comida + "</b><br><small>+" 
			+ puntosComida + "</small></center></html>");
			boton.setBackground(Color.white);
			boton.setFocusable(false);
			
			
			ImageIcon ft = new ImageIcon("comida/" + comida + ".png");
			java.awt.Image ftBien = ft.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH); //esta linea esta hecha con IA Gemini
			boton.setIcon(new ImageIcon(ftBien));
			
			
			//colocar la foto en su sitio
			boton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			boton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
				
						
			boton.addActionListener(e -> {
				if (EstadoMascota.getHambre() >= 100) {
				    JOptionPane.showMessageDialog(
				            Comida.this,
				            "Tu mascota está llena, no puede comer más.",
				            "Llena",
				            JOptionPane.INFORMATION_MESSAGE);
				    return;
				}
				
		        int puntos = Integer.parseInt(puntosComida); // Convierte "10" a int 10
		        Mascota.alimentar(puntos); // Suma los puntos a la mascota
		        EstadoMascota.setHambre(EstadoMascota.getHambre() + puntos);
		        EstadoMascota.setEnergia(EstadoMascota.getEnergia() + puntos / 2);
		        
		        comidaBoca.setIcon(new ImageIcon(ftBien)); //poner la imagen en la boca
		        if(comida.equals("Cerveza") | comida.equals("Agua") |comida.equals("Zumo")) {
		        	nam.setText("GLU, GLU !!");
		        }else {
		        	nam.setText("ÑAM, ÑAM !!");
		        }
		        	
		       
		        nam.setOpaque(true);
		        nam.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		        
		        // la imagen se va al pasar un tiempo
		        Timer temporizador = new Timer(500, ev -> {
		            comidaBoca.setIcon(null); //quitar la foto: ponerla en null y como el fondo es transparente, desaparece
		            nam.setText(""); //quitamos el texto ñam
		            nam.setOpaque(false);
		            nam.setBorder(null);
		        });
		        temporizador.setRepeats(false);
		        temporizador.start();
			});
			
			
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
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
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
	
	

}
