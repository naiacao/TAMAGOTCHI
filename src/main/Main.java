package main;

import javax.swing.SwingUtilities;

import gui.PrimeraVentana;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PrimeraVentana().setVisible(true);
        });
    }
}