package tamagotchi;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class BotonRedondo extends JButton {

	private static final long serialVersionUID = 1L;
	private int radio = 30;
	private Color colorBase;
	private Color colorBorde;

	public BotonRedondo(String texto, Color colorBase, Color colorBorde) {
		super(texto);
		this.colorBase = colorBase;
		this.colorBorde = colorBorde;
		setContentAreaFilled(false);
		setFocusPainted(false);
		setBorderPainted(false);
		setOpaque(false);
		setRolloverEnabled(true);
		setFont(new Font("Arial", Font.BOLD, 16));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (getModel().isPressed()) {
			g2.setColor(colorBase.darker());
		} else if (getModel().isRollover()) {
			g2.setColor(colorBase.brighter());
		} else {
			g2.setColor(colorBase);
		}
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);

		// Borde
		g2.setColor(colorBorde);
		g2.setStroke(new java.awt.BasicStroke(2f));
		g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, radio, radio);

		g2.dispose();
		super.paintComponent(g);
	}
}