package tamagotchi;

import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MenuJuego extends JMenuBar {

    private static final long serialVersionUID = 1L;

    public MenuJuego(ActionListener accionInicio, ActionListener accionCerrar, ActionListener accionCambiarUsuario) {

        JMenu menu = new JMenu("Menu");
        menu.setMnemonic('M');

        JMenuItem itemInicio = new JMenuItem("Pantalla de inicio");
        itemInicio.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK)); 
        /**
         * Esto son combinaciones, el KeyStroke.getKeyStrocke hace que podamos clikar el control con el I en este caso
         * y asi se va a ejecutar esa opcion tambien. ES decir, que no vamos a tener la necesidad de asbrir el menu. Con darle a la letra i 
         * y a cntr a la vez nos va a elegir esa opcion.
         */
        itemInicio.addActionListener(accionInicio);

        JMenuItem itemSalir = new JMenuItem("Cerrar juego");
        itemSalir.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        itemSalir.addActionListener(accionCerrar);
        
        JMenuItem itemCambiarUsuario = new JMenuItem("Cambiar de Usuario");
        itemCambiarUsuario.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK));
        itemCambiarUsuario.addActionListener(accionCambiarUsuario);
      
        //Añadir al menu las 3 opciones que hemos creado
        menu.add(itemInicio);
        menu.add(itemCambiarUsuario);
        menu.addSeparator();
        menu.add(itemSalir);

        add(menu);
    }
}
