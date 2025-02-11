# _cantabile_
Jorge Palacios-Rodriguez

# Project Purpose
_cantabile_ will be a Java GUI application that helps users practice the following music theory concepts:
  + Interval Ear Training
  + Chord Ear Training
  + Melodic Dictation
  + Chord Staff Identification
  + Interval Staff Identification
  + Interval Construction

As a musician, the concepts above are important to master as they are some of the foundations for music. This program will help users drill these exercises. 
The user will be able to select what module they'd like to work on. Each module will be customizable to include or exclude certain intervals or qualities. Each module will also track the time spent and the amount of questions answered correctly and incorrectly.

By building this project, not only will I benefit from deepening my understanding of music theory, but I will also benefit from understanding how to apply object-oriented programming concepts to music theory.

# Initial UML Class Diagrams
Each class will contain information with respect to what music theory concept they're covering. The initial diagrams below show how I plan to create each class relationship, along with how they'll tie to the module exercise aspect of the program.

![UML Diagram 1](https://www.mermaidchart.com/raw/0ea6ece4-49a3-4e8b-8c82-1877893ba068?theme=light&version=v0.1&format=svg)
![UML Diagram 2](https://www.mermaidchart.com/raw/6105b4a3-364b-454d-8986-c9ef57bb5135?theme=light&version=v0.1&format=svg) 

# Plan and Estimate of Effort
In order for this application to be functional, it will be necessary to first work on the Interval and Note classes, as they will serve as the basis for each module. Afterwards, each module should be built around these two classes and contain specific methods for calculating information relative to the specific music theory concept. A system will then be built to generate and track questions for each modules, along with recording user responses. To make this application user-friendly, the GUI (or at least, its functionality) will be built towards the end as the interface will depend on how the overall program functions. The GUI will display the modules, questions, scores, time, and notes (an offset system for note positioning will be required here depending on the type of clef selected).
