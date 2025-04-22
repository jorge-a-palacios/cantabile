/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cantabilegui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author jorge
 */
public class IntervalBuilder {
    // Every chromatic scale should have 12 notes
    private final static int CHROMATIC_SCALE_LENGTH = 12;
    
    // do NOT use the following as roots:
    // d# -- g# -- db
    // these will create pretty nasty intervals not worth dealing with at the moment
    
    private final static Map<String, String> map = new HashMap<String, String>() {{
        put("C#","Db");
        put("D#","Eb");
        put("E", "Fb");
        put("F", "E#");
        put("G#","Ab");
        put("A#","Bb");
        put("F#", "Gb");
        put("B", "Cb");
        put("C", "B#");
        put("A", "Bbb");
        put("D", "Ebb");
     }};
    
    private final static Map<String, String> qualityMap = new HashMap<String, String>() {{
        put("m", "minor");
        put("M", "major");
        put("d", "diminished");
        put("P", "perfect");
    }};
    
    // Initialize a chromatic scale for calculating interval
    private final static Note[] genericSharpChromaticScale = {
        new Note("C"), new Note("C#"),
        new Note("D"), new Note("D#"),
        new Note("E"),
        new Note("F"), new Note("F#"),
        new Note("G"), new Note("G#"),
        new Note("A"), new Note("A#"),
        new Note("B")
    };
    
    private final static Note[] genericFlatChromaticScale = {
        new Note("C"), new Note("Db"),
        new Note("D"), new Note("Eb"),
        new Note("E"),
        new Note("F"), new Note("Gb"),
        new Note("G"), new Note("Ab"),
        new Note("A"), new Note("Bb"),
        new Note("B")
    };
    
    public final static Note[] genericScale = {
        new Note("C"),
        new Note("D"),
        new Note("E"),
        new Note("F"),
        new Note("G"),
        new Note("A"),
        new Note("B"),
    };
    
    public static int getBasicDistance(Note rootNote, Note targetNote) {
        // get the note regardless of its altered state
        Note basicNote = new Note(rootNote.getName().substring(0, 1));
        Note basicTargetNote  = new Note(targetNote.getName().substring(0, 1));
        
        // Find the index of the rootNote
        int index = 0;
        for (Note scaleNote : genericScale) {
            if (basicNote.getName().equals(scaleNote.getName())) {
                break;
            }
            index++;
        }

        // Create an array for the new chromatic scale;
        Note[] basicScale = new Note[7];

        // Copy the elements from the right of the index
        int aIndex = 0;
        for (int i = index; i < genericScale.length; i++) {
            basicScale[aIndex++] = genericScale[i];
        }

        // Copy the elements from the left of the index
        for (int i = 0; i < index; i++) {
            basicScale[aIndex++] = genericScale[i];
        }
        
        // calculate the distance between the notes
        int distance = 0;
        for (Note note : basicScale) {
            distance++;
            if (note.getName().equals(basicTargetNote.getName())) {
                break;
            }
        }

        //System.out.println(distance);
        return distance;
    }
    
    public static Interval createInterval(Note rootNote, String quality, int distance) {
        // To calculate the distance, we need to shift the chromatic scale to the root note
        // This creates a copy of the chromatic scale starting from the rootNote
        // From here, we can do some operations on the scale
        
        // determine whether or not to use sharp/flat chromatic scale
        Note[] modifierScale = genericSharpChromaticScale;
        
        if (rootNote.getName().length() == 2 && rootNote.getName().substring(1,2).equals("b")) {
            modifierScale = genericFlatChromaticScale;
        }
        
        // special cases:
        if (rootNote.getName().equals("E#")) {
            Note[] scale = Arrays.copyOf(genericSharpChromaticScale, CHROMATIC_SCALE_LENGTH);
            scale[5] = rootNote;
            modifierScale = scale;
        } else if (rootNote.getName().equals("Cb")) {
            Note[] scale = Arrays.copyOf(genericFlatChromaticScale, CHROMATIC_SCALE_LENGTH);
            scale[11] = rootNote;
            modifierScale = scale;
        }
        
        // Find the index of the rootNote
        int index = 0;
        for (Note scaleNote : modifierScale) {
            if (rootNote.getName().equals(scaleNote.getName())) {
                break;
            }
            index++;
        }
        
        // Create an array for the new chromatic scale;
        Note[] rootNoteChromaticScale = new Note[CHROMATIC_SCALE_LENGTH];
        
        // Copy the elements from the right of the index
        int aIndex = 0;
        for (int i = index; i < modifierScale.length; i++) {
            rootNoteChromaticScale[aIndex++] = modifierScale[i];
        }
        
        ArrayList<String> shiftedNotes = new ArrayList<String>();
        // Copy the elements from the left of the index
        for (int i = 0; i < index; i++) {
            int leftIndex = aIndex++;
            rootNoteChromaticScale[leftIndex] = modifierScale[i];
            shiftedNotes.add(rootNoteChromaticScale[leftIndex].getName());
        }
        
        //System.out.println("The index of note " + rootNote.name + " is " + index);
        
        // To correctly calculate the interval, the distance will need to be converted to
        // half steps. The calculation will then vary on the base case and quality of the interval.
        int halfSteps = 0;
        switch (distance) {
            case 1: // Unison
                halfSteps = 0;
                break;
            case 2: // Base major 2nd
                halfSteps = 2;
                break;
            case 3: // Base major 3rd
                halfSteps = 4;
                break;
            case 4: // Base perfect 44th
                halfSteps = 5; 
                break;
            case 5: // Base perfect 5th
                halfSteps = 7;
                break;
            case 6: // Base major 6th
                halfSteps = 9;
                break;
            case 7: // Base major 7th
                halfSteps = 11;
                break;
        }
        
        // Determines the half step modification given the quality
        // This will work for (most) intervals. I'll need to
        // check for edge cases depending on the quality and rootNote.
        switch (quality) {
            case "major":
                break;
            case "minor":
                halfSteps--;
                break;
            case "perfect":
                break;
            // the only diminished interval will be d5, shown as "Tritone" to the user.
            case "diminished":
                halfSteps--;
                break;
            //case "augmented":
            //    halfSteps++;
            //    break;
        }
        
        // Check for an enharmonic mismatch
        Note intervalNote = rootNoteChromaticScale[halfSteps];
        // NOTE: the functon isnt setup for octaves (same note)
        if (distance != 8 && getBasicDistance(rootNote, intervalNote) != distance) {
            String note = map.get(intervalNote.getName());
            //System.out.println(intervalNote);
            if (note != null) {
                intervalNote = new Note(note);
            } else {
               // try looking for the key instead of the value
               System.out.println("There is an unresolved mismatch for" + distance);
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (intervalNote.getName().equals(key)) {
                        intervalNote = new Note(value);
                    }
                }
            }
        }
        
        // determines whether or not to use upper-octave for playback
        if (shiftedNotes.contains(intervalNote.getName())) {
            intervalNote.setUpperOctave(true);
        }
        
        // Return the Interval
        return new Interval(rootNote, intervalNote, quality, distance);
    }
    
    // Creates a random interval given an array of allowed intervals;
    public static Interval randomInterval(String... allowedIntervals) {
        
        // select a random interval from the array
        Random gen = new Random();
        int randomIndex = gen.nextInt(allowedIntervals.length);
        String interval = allowedIntervals[randomIndex];
        
        //map the shorthand quality to the regular form
        String quality = qualityMap.get(interval.substring(0, 1));
        int distance = Integer.parseInt(interval.substring(1, 2));
        Interval randomInterval = createInterval(Question.randomNote(), quality, distance);
        
        return randomInterval;
    }
}
