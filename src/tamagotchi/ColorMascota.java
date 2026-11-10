package tamagotchi;

import java.awt.Color;

public class ColorMascota { 
    private final String nombre;
    private final Color color;

    public ColorMascota(String nombre, Color color) { //Le metemos al constructor el color de la mascota que queremos
        this.nombre = nombre;
        this.color = color;
    }

    public String getNombre() { return nombre; }
    public Color getColor()   { return color; }

    @Override
    public String toString() { return nombre; }
}