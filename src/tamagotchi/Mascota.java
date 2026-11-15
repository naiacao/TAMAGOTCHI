package tamagotchi;

public class Mascota {
    // Variables de estado (0 a 100)
    public static int hambre = 50;
    public static int energia = 80;
    public static int felicidad = 70;

    // Método para sumar comida (máximo 100)
    public static void alimentar(int puntos) {
        hambre += puntos;
        if (hambre > 100) {
            hambre = 100; // No puede superar 100
        }
    }

    // Método para restar comida con el paso del tiempo (mínimo 0)
    public static void reducirHambre(int puntos) {
        hambre -= puntos;
        if (hambre < 0) {
            hambre = 0; // No puede bajar de 0
        }
    }
}
