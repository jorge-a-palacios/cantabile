/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cantabilegui;

import java.util.Random;

/**
 *
 * @author jorge
 */
public abstract class Question {
    public static String[] questionNoteSet = {"C", "C#", "D", "Eb", "E", "F", "G", "Ab", "A"};
    
    // to be implemented for Intervals and Questions
    public abstract void createQuestion();
    
    // gets a random note from a user-friendly selection (to prevent unusual intervals/chords)
    public static Note randomNote() {
        Random gen = new Random();
        int randomIndex = gen.nextInt(questionNoteSet.length);
        return new Note(questionNoteSet[randomIndex]);
    }
}
