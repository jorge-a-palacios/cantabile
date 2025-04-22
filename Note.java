/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cantabilegui;
import java.io.BufferedInputStream;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author jorge
 */
public class Note {
    // Initialization
    private String name;
    private boolean upperOctave;
    
    
    // Constructor
    public Note(String name) {
        this.name = name;
    }
    
    // Getter
    public String getName() {
        return this.name;
    }
    
    public void setUpperOctave(Boolean bool) {
        this.upperOctave = bool;
    }
    
    public void playSound() {
        String resource = null;
        /*
        I recognize that this is an absolutely TERRIBLE way to handle this.
        If I had more time, I would've gotten a way better solution
        */
        if (this.upperOctave == true) {
            resource = "/aiff/" + this.getName() + "-.aiff";
        } else {
            resource = "/aiff/"+this.getName()+".aiff";
        }
         try (
            InputStream is = Note.class.getResourceAsStream(resource);
            BufferedInputStream bis = new BufferedInputStream(is)
        ) {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(bis);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
            
             // Schedule stop without blocking
                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        clip.stop();
                        clip.close();
                    } catch (InterruptedException ignored) {}
                }).start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing the note: " + e.getMessage());
        }
    }
}
