package tamagotchi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class Inicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private boolean dormido = false;
	private JPanel oscurecedor;
	
	// Variable para guardar el color seleccionado (por defecto amarillo)
	private Color colorSeleccionado = new Color(255, 193, 7);

	public Inicio() {
		this.setTitle("TAMAGOCHI - LogIn");
		this.setSize(430, 550);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Creación de los paneles principales
		JPanel panelPrincipal = new JPanel(new BorderLayout());
		JPanel panelCentro = new JPanel(new GridLayout(2, 1, 0, 10));
		JPanel panelBotones = new JPanel(new GridLayout(1, 3, 10, 10));
		panelBotones.setPreferredSize(new Dimension(0, 75));
		panelBotones.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		// --- PANEL DE ESTADO Y SELECCIÓN DE COLOR ---
		JPanel panelTamagotchi = new JPanel(new BorderLayout(10, 10));
		panelTamagotchi.setBorder(BorderFactory.createTitledBorder("Estado"));

		// Lista de objetos ColorMascota que usará tu ColorRenderer
		ColorMascota[] coloresDisponibles = {
			new ColorMascota("Amarillo", new Color(255, 193, 7)),
			new ColorMascota("Verde", new Color(76, 175, 80)),
			new ColorMascota("Azul", new Color(63, 143, 208)),
			new ColorMascota("Rojo", new Color(229, 57, 53)),
			new ColorMascota("Morado", new Color(156, 39, 176))
		};

		// JComboBox con tu renderizador de cajas de colores
		JComboBox<ColorMascota> comboColores = new JComboBox<>(coloresDisponibles);
		comboColores.setRenderer(new ColorRenderer()); // <--- Renderiza los iconos de cajas
		comboColores.setPreferredSize(new Dimension(200, 32));

		// Actualizar la variable colorSeleccionado al elegir una opción
		comboColores.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ColorMascota cm = (ColorMascota) comboColores.getSelectedItem();
				if (cm != null) {
					colorSeleccionado = cm.getColor();
				}
			}
		});

		JPanel panelSelector = new JPanel();
		JLabel lblColor = new JLabel("Color de la mascota: ");
		lblColor.setFont(new Font("SansSerif", Font.BOLD, 12));
		panelSelector.add(lblColor);
		panelSelector.add(comboColores);

		panelTamagotchi.add(panelSelector, BorderLayout.CENTER);
		// ---------------------------------------------------

		// Panel de información de barras
		JPanel panelInfo = new JPanel(new GridLayout(3, 1, 0, 10));
		panelInfo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));

		panelInfo.add(crearFila("Hambre", "fotos/POLLO.png", 0, new Color(255, 152, 0)));
		panelInfo.add(crearFila("Energia", "fotos/RAYO.png", 0, new Color(76, 175, 80)));
		panelInfo.add(crearFila("Felicidad", "fotos/CORAZON.png", 0, new Color(63, 143, 208)));

		// Creación de botones inferiores
		BotonRedondo btnalimentar = new BotonRedondo("ALIMENTAR", new Color(76, 175, 80), new Color(46, 125, 50));
		btnalimentar.setToolTipText("COMIDA");
		BotonRedondo btnjugar = new BotonRedondo("JUGAR", new Color(63, 143, 208), new Color(31, 92, 143));
		btnjugar.setToolTipText("JUGAR");
		BotonRedondo btndormir = new BotonRedondo("DORMIR", new Color(255, 193, 7), new Color(199, 145, 0));
		btndormir.setToolTipText("DORMIR");

		panelBotones.add(btnalimentar);
		panelBotones.add(btnjugar);
		panelBotones.add(btndormir);

		// Eventos
		btnalimentar.addActionListener(e -> irAComida());
		btnjugar.addActionListener(e -> irAJuegos());

		panelCentro.add(panelTamagotchi);
		panelCentro.add(panelInfo);

		// Oscurecedor
		oscurecedor = new PanelOscuro(90);
		oscurecedor.setVisible(false);

		JLayeredPane capas = new JLayeredPane();
		capas.setLayout(null);
		capas.add(panelCentro, Integer.valueOf(0));
		capas.add(oscurecedor, Integer.valueOf(1));

		capas.addComponentListener(new java.awt.event.ComponentAdapter() {
			@Override
			public void componentResized(java.awt.event.ComponentEvent e) {
				panelCentro.setBounds(0, 0, capas.getWidth(), capas.getHeight());
				oscurecedor.setBounds(0, 0, capas.getWidth(), capas.getHeight());
			}
		});

		btndormir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (dormido) {
					dormido = false;
					oscurecedor.setVisible(false);
					btndormir.setText("DORMIR");
					btndormir.setToolTipText("DORMIR");
				} else {
					dormido = true;
					oscurecedor.setVisible(true);
					btndormir.setText("DESPERTAR");
					btndormir.setToolTipText("DESPERTAR");
				}
			}
		});

		panelPrincipal.add(capas, BorderLayout.CENTER);
		panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
		this.add(panelPrincipal);
	}

	private JPanel crearFila(String texto, String rutaIcono, int valor, Color color) {
		Image img = new ImageIcon(rutaIcono).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		JLabel etiqueta = new JLabel(texto, new ImageIcon(img), JLabel.LEFT);
		etiqueta.setHorizontalTextPosition(JLabel.LEFT);
		etiqueta.setPreferredSize(new Dimension(120, 0));

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

	private void irAComida() {
		new Comida().setVisible(true);
		this.dispose();
	}

	private void irAJuegos() {
		// Pasa el color seleccionado al constructor de JuegosClase
		new JuegosClase(colorSeleccionado).setVisible(true);
		this.dispose();
	}
	
	public void setColorSeleccionado(Color colorSeleccionado) {
	    this.colorSeleccionado = colorSeleccionado;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Inicio login = new Inicio();
			login.setVisible(true);
		});
	}
}