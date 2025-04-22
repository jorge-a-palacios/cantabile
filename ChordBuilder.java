/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cantabilegui;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author jorge
 */
public class ChordBuilder {
    public static String[] qualities = {"major", "minor", "diminished", "major7", "minor7"};
    
    public static Chord createChord(Note rootNote, String quality) {
        Chord chord = null;
        
        switch (quality) {
            case "major":
                {
                    // a major chord is an M3 interval with an m3 stacked from the interval note
                    Interval rootInterval = IntervalBuilder.createInterval(rootNote, quality, 3);
                    // get the last note
                    //System.out.println(rootInterval.getIntervalNote().getName().toString());
                    Interval noteInterval = IntervalBuilder.createInterval(rootInterval.getIntervalNote(), "minor", 3);
                    // create the chord
                    chord = new Chord(quality, rootInterval.getRootNote(), rootInterval.getIntervalNote(), noteInterval.getIntervalNote());
                    break;
                }
            case "minor":
                {
                    // a minor chord is an m3 interval with an M3 interval stacked from the interval note
                    Interval rootInterval = IntervalBuilder.createInterval(rootNote, quality, 3);
                    // get the last note
                    Interval noteInterval = IntervalBuilder.createInterval(rootInterval.getIntervalNote(), "major", 3);
                    // create the chord
                    chord = new Chord(quality, rootInterval.getRootNote(), rootInterval.getIntervalNote(), noteInterval.getIntervalNote());
                    break;
                }
            case "diminished":
                {
                    // a minor chord is an m3 interval with an M3 interval stacked from the interval note
                    Interval rootInterval = IntervalBuilder.createInterval(rootNote, quality, 3);
                    // get the last note
                    Interval noteInterval = IntervalBuilder.createInterval(rootInterval.getIntervalNote(), "minor", 3);
                    // create the chord
                    chord = new Chord(quality, rootInterval.getRootNote(), rootInterval.getIntervalNote(), noteInterval.getIntervalNote());
                    break;
                }
            case "major7": {
                // a major7 chord is a major with M3 stacked on top
                Chord majorChord = createChord(rootNote, "major");
                ArrayList<Note> majorChordNotes = majorChord.getNotes();
                Note[] chordNotes = new Note[3];
                for (int i = 0; i < 3; i++) {
                    chordNotes[i] = majorChordNotes.get(i);
                }
                Interval noteInterval = IntervalBuilder.createInterval(chordNotes[2], "major", 3);
                // create the chord
                chord = new Chord(quality, chordNotes[0], chordNotes[1], chordNotes[2], noteInterval.getIntervalNote());
                break;
            }
            case "minor7": {
                // a minor chord with m3 stacked on top
                Chord majorChord = createChord(rootNote, "minor");
                ArrayList<Note> majorChordNotes = majorChord.getNotes();
                Note[] chordNotes = new Note[3];
                for (int i = 0; i < 3; i++) {
                    chordNotes[i] = majorChordNotes.get(i);
                }
                Interval noteInterval = IntervalBuilder.createInterval(chordNotes[2], "minor", 3);
                // create the chord
                chord = new Chord(quality, chordNotes[0], chordNotes[1], chordNotes[2], noteInterval.getIntervalNote());
                break;
            }
            case "dom7": {
                // a major chord with a m3 stacked on top
                // a minor chord with m3 stacked on top
                Chord majorChord = createChord(rootNote, "major");
                ArrayList<Note> majorChordNotes = majorChord.getNotes();
                Note[] chordNotes = new Note[3];
                for (int i = 0; i < 3; i++) {
                    chordNotes[i] = majorChordNotes.get(i);
                }
                Interval noteInterval = IntervalBuilder.createInterval(chordNotes[2], "minor", 3);
                // create the chord
                chord = new Chord(quality, chordNotes[0], chordNotes[1], chordNotes[2], noteInterval.getIntervalNote());
                break;
            }
            default:
                break;
        }
        
                   
        return chord;
    }
    
        // Creates a random chord given an array of allowed qualities;
    public static Chord randomChord(ArrayList<String> allowedQualities) {
        
        // select a random chord from the array
        Random gen = new Random();
        int randomIndex = gen.nextInt(allowedQualities.size());
        String quality = allowedQualities.get(randomIndex);
        
        Chord randomChord = createChord(Question.randomNote(), quality);
        
        return randomChord;
    }
}
