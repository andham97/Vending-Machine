package main;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import lejos.hardware.Sound;

/**
 *
 * @author Magnus C. Hyll <magnus@hyll.no>
 */
public class Music implements Runnable {
    
    private boolean isPlaying;
    private boolean startMusic;
    
    public Music() {
        isPlaying = false;
        startMusic = false;
    }
    
    public void startThread() {
        new Thread(this, "MusicThread").start();
    }
    
    public synchronized void makeItRain() {
        startMusic = true;
        notifyAll();
    }

    public void run() {
        File file = new File("rain.wav");
        
        while (Main.isRunning) {
            synchronized (this) {
                while (!startMusic) {
                    try {
                        wait();
                    } catch (InterruptedException ex) { }
                }
            }
            
            if (!isPlaying && startMusic) {
                int duration = Sound.playSample(file, 50);
                isPlaying = true;
                startMusic = false;
                try {
                    Thread.sleep(duration);
                } catch (InterruptedException ex) { }
                isPlaying = false;
            }
        }
    }
    
}
