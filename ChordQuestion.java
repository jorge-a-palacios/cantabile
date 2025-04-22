/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cantabilegui;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jorge
 */
public class ChordQuestion {
    // Initialization
    private Chord chord;
    private String correctAnswer;
    
    // Constructor
    public ChordQuestion(Chord chord) {
        this.chord = chord;
        this.correctAnswer = chord.getQuality();
    }
   
    
    
    public boolean checkAnswer(String chordQuality) {
        return chordQuality.equals(this.correctAnswer);
    }
    
    public Chord getChord() {
        return this.chord;
    }
    
    public String getAnswer() {
        return this.correctAnswer;
    }
}
