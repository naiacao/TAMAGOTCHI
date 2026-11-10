package tamagotchi;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ColorRenderer extends JLabel implements ListCellRenderer {

    private static final long serialVersionUID = 1L;
    private static final int TAM_CUADRADO = 18;

    public ColorRenderer() {
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        setIconTextGap(8);
    }

    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object valor,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        if (!(valor instanceof ColorMascota)) {
            setText("");
            setIcon(null);
            return this;
        }

        ColorMascota cm = (ColorMascota) valor;
        setText(cm.getNombre());
        setIcon(new IconoColor(cm.getColor(), TAM_CUADRADO));

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }

    /** Icono que dibuja un cuadrado relleno del color indicado. */
    public static class IconoColor implements Icon {
        private final Color color;
        private final int lado;

        public IconoColor(Color color, int lado) {
            this.color = color;
            this.lado = lado;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillRoundRect(x, y, lado, lado, 4, 4);
            g2.setColor(color.darker());
            g2.drawRoundRect(x, y, lado, lado, 4, 4);
            g2.dispose();
        }

        @Override public int getIconWidth()  { return lado; }
        @Override public int getIconHeight() { return lado; }
    }
}
