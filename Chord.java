/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cantabilegui;

import java.util.ArrayList;

/**
 *
 * @author jorge
 */
public class Chord {
    private ArrayList<Note> chordNotes = new ArrayList<Note>();
    private String quality;
    
    // a chord may contain 3-4 notes
    public Chord(String quality, Note... notes) {
        // add the notes to the chordNotes ArrayList
        for (Note note : notes) {
            chordNotes.add(note);
        }
        
        this.quality = quality;
    }
    
    // returns the notes of the chord
    public ArrayList<Note> getNotes() {
        return this.chordNotes;
    }
    
    public void playSound() {
        for (Note note : this.getNotes()) {
            note.playSound();
        }
    }
    
    // returns the quality of the chord
    public String getQuality() {
        return this.quality;
    }
    
    // returns the chord quality along with the notes of the chord
    public String toString() {
        ArrayList<Note> notes = this.getNotes();
        
        String chord = notes.get(0).getName() + " " + this.getQuality() + " chord: ";
        
        for (Note note : notes) {
            chord += note.getName() + " ";
        }
        
        return chord;
    }
}
