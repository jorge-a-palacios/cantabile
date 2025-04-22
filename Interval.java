/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cantabilegui;

/**
 *
 * @author jorge
 */
public class Interval {
    // Initialization
    private Note rootNote;
    private Note intervalNote;
    private String quality; 
    private int distance; 
    
    // Constructor
    public Interval(Note rootNote, Note intervalNote, String quality, int distance) {
        this.rootNote = rootNote;
        this.intervalNote = intervalNote;
        this.quality = quality;
        this.distance = distance;
    }
    
    // Getters
    public Note getRootNote() {
        return this.rootNote;
    }
    
    public Note getIntervalNote() {
        return this.intervalNote;
    }
    
    public String getQuality() {
        return this.quality;
    }
    
    public int getDistance() {
        return this.distance;
    }
    
    public void playSound() {
        this.rootNote.playSound();
        this.intervalNote.playSound();
    }
    
    // Returns string representation of the interval
    public String toString() {
        return "[" + this.getQuality() + this.getDistance() + "] Root Note: " + rootNote.getName() + " || Interval Note: " + intervalNote.getName();
    }
}
