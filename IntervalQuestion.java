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
public class IntervalQuestion {
    // Initialization
    private Interval interval;
    private String correctAnswer;
    
    // Constructor
    public IntervalQuestion(Interval interval) {
        this.interval = interval;
         this.correctAnswer = qualityMap.get(this.interval.getQuality()) + this.interval.getDistance();
    }
    
    private final static Map<String, String> qualityMap = new HashMap<String, String>() {
        {
            put("minor", "m");
            put("major", "M");
            put("diminished", "d");
            put("perfect", "P");
        }
    };
    
    public boolean checkAnswer(String intervalQuality) {
        return intervalQuality.equals(this.correctAnswer);
    }
    
    public Interval getInterval() {
        return this.interval;
    }
    
    public String getAnswer() {
        return this.correctAnswer;
    }
}
