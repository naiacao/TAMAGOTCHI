package tamagotchi;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Juego1 extends JDialog {

	private static final long serialVersionUID = 1L;

	public Juego1(JFrame padre) {
		super(padre, "Juego 1", true);
		this.setSize(380, 450);
		this.setResizable(false);
		this.setLocationRelativeTo(padre);

		JPanel panel = new JPanel(new BorderLayout(10, 15));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel lblTitulo = new JLabel("🎮 JUEGO 1", SwingConstants.CENTER);
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
		panel.add(lblTitulo, BorderLayout.NORTH);

		JLabel lblContenido = new JLabel("¡Espacio para la lógica de Juego 1!", SwingConstants.CENTER);
		panel.add(lblContenido, BorderLayout.CENTER);

		JButton btnSalir = new JButton("Salir");
		btnSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		panel.add(btnSalir, BorderLayout.SOUTH);

		this.add(panel);
	}
}