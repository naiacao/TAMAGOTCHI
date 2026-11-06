package tamagotchi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
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
		
		//CREACIÓN PANELES INFO
		JPanel panelUsername = new JPanel(new BorderLayout());
		JPanel panelContraseña = new JPanel(new BorderLayout());
		JPanel panelPequeñoContraseña = new JPanel (new GridBagLayout());
		
		panelContraseña.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); //Borde con la ventana
		
		JPasswordField zonaContraseña = new JPasswordField(); //MIRAR COMO PONER UN LIMITE DE CARACTERES!!
		zonaContraseña.setPreferredSize(new java.awt.Dimension(0, 20)); //Para que la zona de contraseña no nos quede tan alta
		zonaContraseña.setFont(new Font("Arial", Font.PLAIN, 14));      // NUEVO: letra algo mayor
		zonaContraseña.setBorder(BorderFactory.createCompoundBorder(    // NUEVO: borde fino + aire interior
				BorderFactory.createLineBorder(new Color(135, 185, 210), 1),
				BorderFactory.createEmptyBorder(0, 8, 0, 8)));
		
		JLabel titulo = new JLabel("password"); //El titulito de password
		titulo.setFont(new Font("Arial", Font.BOLD, 13));                 // NUEVO
		titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0)); 
		
		JPanel panelCampo = new JPanel(new BorderLayout());
		panelCampo.add(titulo, BorderLayout.NORTH);            // NUEVO (antes iba en panelPequeñoContraseña)
		panelCampo.add(zonaContraseña, BorderLayout.CENTER);   // NUEVO (antes iba en panelPequeñoContraseña)
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		panelPequeñoContraseña.add(panelCampo, gbc);

		panelContraseña.add(panelPequeñoContraseña, BorderLayout.CENTER);
		
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
		
		//Añadir todos los paneles
		
		panelCentro.add(panelTitulo);
		panelCentro.add(panelInfo);
		panelPrincipal.add(panelCentro, BorderLayout.CENTER);
		panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
		this.add(panelPrincipal);
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(() -> {
          PrimeraVentana login = new PrimeraVentana();
            login.setVisible(true);
        });
	}

}
