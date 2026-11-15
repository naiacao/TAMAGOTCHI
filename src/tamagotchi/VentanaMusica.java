package tamagotchi;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class VentanaMusica extends JDialog {

	public VentanaMusica(JFrame ventanaPadre, Musica musica) {
		// TODO Auto-generated constructor stub
		super(ventanaPadre, "Volumen", true); //el true para bloquear la vetnana de atras
		 JLabel lblVolumen = new JLabel("Volumen: " + musica.getVolumen(), SwingConstants.CENTER);

	        JSlider slider = new JSlider(0, 100, musica.getVolumen());
	        slider.setMajorTickSpacing(25);
	        slider.setPaintTicks(true);
	        slider.setPaintLabels(true);
	        slider.addChangeListener(e -> {
	            musica.setVolumen(slider.getValue());
	            lblVolumen.setText("Volumen: " + slider.getValue());
	        });
	        JButton btnCerrar = new JButton("Cerrar");
	        btnCerrar.addActionListener(e -> dispose());

	        JPanel panel = new JPanel(new BorderLayout(8, 8));
	        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
	        panel.add(lblVolumen, BorderLayout.NORTH);
	        panel.add(slider, BorderLayout.CENTER);
	        panel.add(btnCerrar, BorderLayout.SOUTH);

	        setContentPane(panel);
	        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	        setSize(300, 180);
	        setResizable(false);
	        setLocationRelativeTo(ventanaPadre);
	}

}
