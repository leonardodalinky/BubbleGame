package game;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.*;
import java.awt.*;

public class SFXManager{
	
	private static SFXManager instance;
	
	Thread t;
	Clip clip;
	
	
	private SFXManager() {
		

	}
	
	public static SFXManager getInstance() {
		if (instance == null)
			instance = new SFXManager();
		return instance;
	}
	
	public void loop() {
		t = (new Thread() {
			@Override
			public void run() {
				try {
					// create AudioInputStream object 
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("./sound/bgm.wav"));
					AudioInputStream audioInputStream =  
					        AudioSystem.getAudioInputStream(in);
					
			        // create clip reference 
			        clip = AudioSystem.getClip(); 
			          
			        // open audioInputStream to the clip 
			        clip.open(audioInputStream); 
				} catch (UnsupportedAudioFileException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			}
		});
		t.start();
	}

	@Override()
	public void paint(Graphics graphics) {
		throw new UnsupportedOperationException();
	}

}