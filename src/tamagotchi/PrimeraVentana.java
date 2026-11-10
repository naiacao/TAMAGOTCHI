package tamagotchi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class PrimeraVentana extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PrimeraVentana () {
		// TODO Auto-generated constructor stub
		this.setTitle("TAMAGOCHI - LogIn");
		this.setSize(430, 550);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//Creación de los paneles
		JPanel panelPrincipal = new JPanel(new BorderLayout());
		JPanel panelCentro = new JPanel (new GridLayout(2, 1, 0, 10));
		JPanel panelBotones = new JPanel (new GridLayout(1, 2, 7, 0));
		panelBotones.setPreferredSize(new java.awt.Dimension(0, 75));
		panelBotones.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		JPanel panelTitulo = new JPanel();
		JPanel panelInfo = new JPanel(new GridLayout(2, 1));
		
		panelTitulo.setBorder(BorderFactory.createTitledBorder("Login"));
		panelInfo.setBorder(BorderFactory.createTitledBorder(""));
		
		//AÑADIR LA FOTO
		JLabel labelTitulo = new JLabel("");
		ImageIcon icono = new ImageIcon("fotos/logotipo.png");
		Image imagen = icono.getImage().getScaledInstance(390, 160, Image.SCALE_SMOOTH); //Para poder darle el tamaño adecuado
		labelTitulo.setIcon(new ImageIcon(imagen));
		panelTitulo.add(labelTitulo);
		
		//CREACIÓN PANELES INFO
		JPanel panelUsername = new JPanel(new BorderLayout()); //LA ZONA PARA LO DE USERNAME
		JPanel panelContraseña = new JPanel(new BorderLayout());  //LA ZONA PARA LO DE LA CONTRASEÑA
		JPanel panelPequeñoContraseña = new JPanel (new GridBagLayout());
		
		
		// PANEL USERNAME (igual que el de contraseña) 
		JPanel panelPequeñoUsername = new JPanel(new GridBagLayout());
		panelUsername.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); //Borde con la ventana
		
		//EL TEXTFIELD PARA METER EL USERNAME
		JTextField zonaUsername = new JTextField(); // campo de texto normal (no oculta caracteres)
		zonaUsername.setPreferredSize(new java.awt.Dimension(0, 20));
		zonaUsername.setFont(new Font("Arial", Font.PLAIN, 14));
		zonaUsername.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(135, 185, 210), 1),BorderFactory.createEmptyBorder(0, 8, 0, 8)));
		
		//EL TITULITO DE USERNAME
		JLabel tituloUsername = new JLabel("username");
		tituloUsername.setFont(new Font("Arial", Font.BOLD, 13));
		tituloUsername.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
		
		JPanel panelCampoUsername = new JPanel(new BorderLayout());
		panelCampoUsername.add(tituloUsername, BorderLayout.NORTH);
		panelCampoUsername.add(zonaUsername, BorderLayout.CENTER);

		
		//CREAR LA ZONA DE CONTRASEÑA
		JPasswordField zonaContraseña = new JPasswordField(); //MIRAR COMO PONER UN LIMITE DE CARACTERES!!
		panelContraseña.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); //Borde con la ventana
		zonaContraseña.setPreferredSize(new java.awt.Dimension(0, 20)); //Para que la zona de contraseña no nos quede tan alta
		zonaContraseña.setFont(new Font("Arial", Font.PLAIN, 14));      // letra algo mayor
		zonaContraseña.setBorder(BorderFactory.createCompoundBorder(    // borde fino + aire interior
				BorderFactory.createLineBorder(new Color(135, 185, 210), 1),
				BorderFactory.createEmptyBorder(0, 8, 0, 8)));
		
		//RESTO DE COSAS DE LA CONTRASEÑA
		JLabel titulo = new JLabel("password"); //El titulito de password
		titulo.setFont(new Font("Arial", Font.BOLD, 13));                 // NUEVO
		titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0)); 
		
		JPanel panelCampo = new JPanel(new BorderLayout());
		panelCampo.add(titulo, BorderLayout.NORTH);            
		panelCampo.add(zonaContraseña, BorderLayout.CENTER);   
		
		//Para regular bien el tamaño de la zona de contraseña (IA);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		panelPequeñoContraseña.add(panelCampo, gbc);

		panelContraseña.add(panelPequeñoContraseña, BorderLayout.CENTER);
		
		//Para regular bien el tamaño de la zona de username;
		GridBagConstraints gbcU = new GridBagConstraints();
		gbcU.fill = GridBagConstraints.HORIZONTAL;
		gbcU.weightx = 1.0;
		panelPequeñoUsername.add(panelCampoUsername, gbcU);

		panelUsername.add(panelPequeñoUsername, BorderLayout.CENTER);
		
		//FOTO DE LA IZQUIERDA DE USERNAME
		JLabel fotoUsername = new JLabel("AQUI FOTO"); // el icono de la izquierda
		panelUsername.add(fotoUsername, BorderLayout.WEST);
		fotoUsername.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
		
		//LA FOTO DE LA IZQUIERDA
		JLabel foto = new JLabel("AQUI FOTO");
		panelContraseña.add(foto, BorderLayout.WEST);
		foto.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); //Borde entre foto y password
		
		panelInfo.add(panelUsername);
		panelInfo.add(panelContraseña);
		
		
		
		//Creamos los botones
		BotonRedondo botonSalir = new BotonRedondo("SALIR", new Color(255, 192, 203), new Color(230, 150, 170) );
		BotonRedondo botonContinuar = new BotonRedondo("CONTINUAR", new Color(173, 216, 230), new Color(135, 185, 210));

		panelBotones.add(botonContinuar);
		panelBotones.add(botonSalir);
		
		//A los botones meterles los listeners
		ActionListener listenerSalir = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cerrarJuego();
			}
		};
		botonSalir.addActionListener(listenerSalir);
		
		ActionListener listenerContinuar = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Inicio();
			}
		};
			
		botonContinuar.addActionListener(listenerContinuar);
		
		
		//Añadir todos los paneles
		
		panelCentro.add(panelTitulo);
		panelCentro.add(panelInfo);
		panelPrincipal.add(panelCentro, BorderLayout.CENTER);
		panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
		this.add(panelPrincipal);
		
	}
	  private void cerrarJuego() {
	        int opcion = JOptionPane.showConfirmDialog(
	                this,
	                "¿Seguro que quieres cerrar el juego?",
	                "Cerrar juego",
	                JOptionPane.YES_NO_OPTION,
	                JOptionPane.QUESTION_MESSAGE);

	        if (opcion == JOptionPane.YES_OPTION) {
	            System.exit(0);         //ESTO SIRVE PARA CERRAR TODO EL PROCESO DEL JUEGO, CON EL DISPOSE NO NOS CERRARÍA EL JUEGO
	        }
	    }
	  
	  private void Inicio() {
		 new Inicio().setVisible(true);
  	    this.dispose();
	  }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(() -> {
          PrimeraVentana login = new PrimeraVentana();
            login.setVisible(true);
        });
	}

}
