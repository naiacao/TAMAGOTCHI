package tamagotchi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class BotonCircular extends JButton {

    private static final long serialVersionUID = 1L;
    private Image imagen;
    private Color colorBorde = Color.DARK_GRAY;
    private Color colorPulsado = new Color(0, 0, 0, 60);

    public BotonCircular(String rutaImagen, int diametro) {
        this.imagen = new ImageIcon(rutaImagen).getImage();
        setPreferredSize(new Dimension(diametro, diametro));
        setContentAreaFilled(false); // quitamos el fondo rectangular
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int d = Math.min(getWidth(), getHeight()) - 2;
        int x = (getWidth() - d) / 2;
        int y = (getHeight() - d) / 2;

        // Recortar en forma de círculo
        g2.setClip(new Ellipse2D.Float(x, y, d, d));

        // Fondo blanco para que la foto quede limpia
        g2.setColor(Color.WHITE);
        g2.fillOval(x, y, d, d);

        // Foto reducida (70% del círculo) y con su proporción original
        int max = (int) (d * 0.70);
        int iw = imagen.getWidth(this);
        int ih = imagen.getHeight(this);
        double escala = Math.min((double) max / iw, (double) max / ih);
        int w = (int) (iw * escala);
        int h = (int) (ih * escala);
        int ix = x + (d - w) / 2;
        int iy = y + (d - h) / 2;
        g2.drawImage(imagen, ix, iy, w, h, this);

        // Oscurecer un poco al pulsar
        if (getModel().isArmed()) {
            g2.setColor(colorPulsado);
            g2.fillOval(x, y, d, d);
        }

        // Borde (quitamos el recorte para que no se corte)
        g2.setClip(null);
        g2.setColor(colorBorde);
        g2.drawOval(x, y, d, d);

        g2.dispose();
    }

    // Para que solo reaccione al click dentro del círculo
    @Override
    public boolean contains(int x, int y) {
        int d = Math.min(getWidth(), getHeight());
        return new Ellipse2D.Float((getWidth() - d) / 2f, (getHeight() - d) / 2f, d, d).contains(x, y);
    }
}