/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cantabilegui;

/**
 *
 * @author jorge
 */
public class Tests {
    public static void run() {
        // The following prints all intervals and qualities for all possible notes
        // see IntervalBuilder for excluded roots
        //String[] notes = {"C", "C#", "D", "Eb", "E", "F", "G", "A", "B", "Bb", "F#", "Ab"};
       String[] notes = {"C", "D", "E", "F"};
        
        String[] qualities = {"perfect", "major", "minor", "diminished"};
        int[] mM_distances = {2, 3, 6, 7}; // includes major and minor
        int[] p_distances = {1, 4, 5, 8};
        //int[] a_distances = {4, 5};
        int[] d_distances = {5}; //,6, 7};

        for (String note : notes) {
            Note root = new Note(note);

            for (String quality : qualities) {
                if (quality.equals("major") || quality.equals("minor")) {
                    for (int distance : mM_distances) {
                        Interval interval = IntervalBuilder.createInterval(root, quality, distance);
                        //System.out.println(interval.toString());
                        //IntervalBuilder.getBasicDistance(root, interval.getIntervalNote());
                    }
                }

                if (quality.equals("perfect")) {
                    for (int distance : p_distances) {
                        Interval interval = IntervalBuilder.createInterval(root, quality, distance);
                        //System.out.println(interval.toString());
                        //IntervalBuilder.getBasicDistance(root, interval.getIntervalNote());
                    }
                }

                /*if (quality.equals("augmented")) {
                for (int distance : a_distances) {
                    Interval interval = IntervalBuilder.createInterval(root, quality, distance);
                    System.out.println("[" + quality + distance + "] " + interval.toString());
                }
            }*/
                if (quality.equals("diminished")) {
                    for (int distance : d_distances) {
                        Interval interval = IntervalBuilder.createInterval(root, quality, distance);
                        //System.out.println(interval.toString());
                    }
                }
            }

            String[] chordQualities = {"major", "minor", "diminished", "major7", "minor7", "dom7"};

            for (String quality : chordQualities) {
                Chord c_M = ChordBuilder.createChord(root, quality);
                System.out.println(c_M.toString());
            }
        }
        
        // generate a set of random intervals using random notes
        for (int i = 0; i < 10; i++) {
            Interval interval = IntervalBuilder.randomInterval("m2", "M2", "m3", "M3", "P4");
            //System.out.println(interval.toString());
        }
        
        Note a = new Note("C");
        a.playSound();
        
        System.out.println("OK");
    }
}
