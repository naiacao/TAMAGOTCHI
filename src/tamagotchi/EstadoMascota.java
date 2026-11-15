package tamagotchi;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class EstadoMascota {
	private static Color color = Color.white;
	private static String gorro = null;
	
	public static Color getColor() {
		return color;
	}
	public static void setColor(Color c) {
		if (c!=null) {
		color=c;}
	}

	public static String getGorro() { 
		return gorro; 
	}
    public static void setGorro(String g) { 
    	gorro = g;
    }
    public static String nombreArchivoColor (Color c) {
    	   if (c.equals(Color.white))             return "Blanco";
           if (c.equals(Color.yellow))            return "Amarillo";
           if (c.equals(Color.orange))            return "Naranja";
           if (c.equals(Color.pink))              return "Rosa";
           if (c.equals(Color.red))               return "Rojo";
           if (c.equals(new Color(105, 12, 181))) return "Morado";
           if (c.equals(Color.blue))              return "Azul";
           if (c.equals(new Color(15, 245, 231))) return "Mentoso";
           if (c.equals(Color.green))             return "Verde";
           if (c.equals(Color.black))             return "Negro";
           return "Blanco";
    }
    /** Devuelve la mascota (con gorro si lo hay) escalada para caber en anchoMax x altoMax. */
    public static ImageIcon componerMascota(int anchoMax, int altoMax) {
        ImageIcon base = new ImageIcon("fotos/" + nombreArchivoColor(color) + ".png");
        if (base.getIconWidth() <= 0) return null;

        int w = base.getIconWidth();
        int h = base.getIconHeight();
        int margen = (int) (h * 0.30);

        BufferedImage compuesta = new BufferedImage(w, h + margen, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = compuesta.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(base.getImage(), 0, margen, null);

        if (gorro != null) {
            ImageIcon ig = new ImageIcon("gorros/" + gorro + ".png");
            if (ig.getIconWidth() > 0) {
                int gw = (int) (w * 0.42);
                int gh = ig.getIconHeight() * gw / ig.getIconWidth();
                int x = (w - gw) / 2;
                int y = margen + (int) (h * 0.12) - gh;
                g.drawImage(ig.getImage(), x, y, gw, gh, null);
            }
        }
        g.dispose();

        double factor = Math.min((double) anchoMax / w, (double) altoMax / (h + margen));
        return new ImageIcon(compuesta.getScaledInstance(
                (int) (w * factor), (int) ((h + margen) * factor), Image.SCALE_SMOOTH));
    }

	public EstadoMascota() {
		// TODO Auto-generated constructor stub
		
	}

}
