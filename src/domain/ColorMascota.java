package domain;

import java.awt.Color;

public class ColorMascota { 
    private final String nombre;
    private final Color color;
    private String rutaImagen;

    public ColorMascota(String nombre, Color color) {
        this(nombre, color, "fotos/" + nombre + ".png"); // por defecto: fotos/Blanco.png
    }
    
    public ColorMascota(String nombre, Color color, String rutaImagen) { //Le metemos al constructor el color de la mascota que queremos
        this.nombre = nombre;
        this.color = color;
        this.rutaImagen = ("fotos/"+nombre+".png");
    }

    public String getNombre() { return nombre; }
    public Color getColor()   { return color; }
    public String getRutaImagen() {return rutaImagen;}

    @Override
    public String toString() { return nombre; }
}