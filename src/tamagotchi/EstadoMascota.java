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
	private static int hambre = 0; // 0 = sin hambre, 100 = muerta de hambre
	private static int energia = 100;
	private static int felicidad = 0;
	private static long ultimoJuego = System.currentTimeMillis();
	
	public static Color getColor() {
		return color;
	}
	public static void setColor(Color c) {
		if (c != null) {
			color = c;
		}
	}

	public static int getHambre() {
		return hambre;
	}
	public static void setHambre(int hambre) {
		EstadoMascota.hambre = hambre;
	}
	public static String getGorro() { 
		return gorro; 
	}
	public static void setGorro(String g) { 
		gorro = g;
	}

	public static synchronized int getEnergia() {
		return energia;
	}

	public static synchronized void setEnergia(int e) {
		energia = Math.max(0, Math.min(100, e));
	}

	public static synchronized int getFelicidad() {
		return felicidad;
	}

	public static synchronized void setFelicidad(int f) {
		felicidad = Math.max(0, Math.min(100, f));
	}

	public static synchronized void sumarFelicidad(int cantidad) {
		if (cantidad > 0) {
			felicidad = Math.min(100, felicidad + cantidad);
		}
		ultimoJuego = System.currentTimeMillis();
	}

	public static synchronized void registrarActividadJuego() {
		ultimoJuego = System.currentTimeMillis();
	}

	public static synchronized long getUltimoJuego() {
		return ultimoJuego;
	}

	public static String nombreArchivoColor(Color c) {
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
	
	public static void sumarFelicidadGastandoEnergia(int cantidad) {
		int antes = getFelicidad();
		sumarFelicidad(cantidad);
		int ganada = getFelicidad() - antes;

		if (ganada > 0) {
			setEnergia(Math.max(0, getEnergia() - ganada));
			setHambre(getHambre() + ganada); // la energía baja y el hambre sube lo mismo
		}
	}

	public EstadoMascota() {
	}
}