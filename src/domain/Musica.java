package domain;

import java.net.URL;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Musica {
	private static final String RUTA = "musica/musica_tamagochi_alegre.wav";
	private static Musica instancia;
	private Clip clip;
	private FloatControl volumen;
	private int volumenActual = 50;

	public static Musica getInstance() {
		// TODO Auto-generated method stub
		if (instancia == null) {
			instancia = new Musica();
		}
		return instancia;
	}
	private Musica() {
		cargar(RUTA);
		setVolumen(volumenActual);
	}
	public void cargar(String ruta) { //el método que carga el audio 
		//Ayuda de IAG (Claude, para aprender a cargar archivos de música
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(new File(ruta));
			clip = AudioSystem.getClip();
			clip.open(audio);
			if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumen = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }
		}catch(Exception e) {
			System.out.println("No se pudo cargar la música: " + e.getMessage());
		}
	}
	public void reproducir() {
		if (clip != null && !clip.isRunning()) {
		clip.loop(Clip.LOOP_CONTINUOUSLY);} //para que no pare nunca
	}
	public void pausar() {
		if (clip != null) {
		clip.stop();}
	}

	public int getVolumen() { 
		return volumenActual; 
		}

	public void setVolumen(int valor) {
	    volumenActual = valor;   //
	    if (volumen == null) return;
	    float db = (valor == 0)
	        ? volumen.getMinimum()
	        : (float) (20 * Math.log10(valor / 100.0));
	    volumen.setValue(Math.max(db, volumen.getMinimum()));
	}
	



}
