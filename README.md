# _cantabile_

# Project Purpose
_cantabile_ is a JavaFX application that helps users practice the following music theory concepts:
  + Interval Ear Training
  + Chord Ear Training
  + Melodic Dictation
  + Chord Staff Identification
  + Interval Staff Identification

This program will help users drill these exercises. The user will be able to select what module they'd like to work on. Each module will be customizable to include or exclude certain intervals or qualities. Each module also tracks the amount of questions answered correctly and incorrectly.

# User Manual
To use this application, start the program and select any of the available modules for practice by pressing the "Launch" button.
<img width="712" alt="image" src="https://github.com/user-attachments/assets/58799efb-f428-4da4-a6af-9970c8507355" />

Upon selecting a module, you will be presented with a configuration screen to configure the module to include or exclude certain values.
Once you configure the module to your liking, you can begin practicing by pressing the "Begin Module" button.
![image](https://github.com/user-attachments/assets/0b36db15-27cd-43ae-8e54-a04dce01dab6)

Each module is similar for simplicity and will display a question along with possible answers.
The gear icon is located at the top right of the window if changes to the module configuration are needed.
![image](https://github.com/user-attachments/assets/e8136f40-d428-461f-a8fe-57abb1868449)

Once an answer has been selected, you will be presented with a popup to inform you of your results.
To continue, press the "Ok" button. The modules last indefinitely until the user exits. You can see your progress at the top of the module.
<img width="597" alt="image" src="https://github.com/user-attachments/assets/dacf881b-3e94-4f02-9c4a-c0d6fb6840da" />

To exit the module and return to the main menu, simply press the "X" window icon and you will be directed back.
<img width="712" alt="image" src="https://github.com/user-attachments/assets/58799efb-f428-4da4-a6af-9970c8507355" />

# Implementation Manual
These music theory concepts build upon one another, with the "root" of the program being the Note class. I used aggregation for this project, which helped greatly reduce the amount of code and allowed the reuse of multiple components. The Interval class is constructed with two Notes. The Chord class is constructed with either a Note stacked on top of an Interval, or two Intervals stacked on top of one another. For the sake of simplicity, the Module class is depicted as a single class in the UML diagram below; however, each module has its own customizable configuration settings along with private instance data to help the program flow smoothly and keep track of user data. The IntervalQuestion and ChordQuestion classes build upon the Question class, which provides common helper functions for the module exercises, as well as a way to keep track of question answers and methods to check user answers. The IntervalBuilder and ChordBuilder classes facilitate the creation of Intervals Chords to ensure proper logic is being followed, along with a function to randomly generate the chord. For the ear training exercises, since the "root" of the program is the Note class, the rest of the classes (Interval and Chord) have a playSound method that call the respective playSound method for their Note objects. The IntervalBuilder class does most of the heavy work here, as the entire program relies on accurate information and classes constructed from this class.

![UML Diagram](https://www.mermaidchart.com/raw/0ea6ece4-49a3-4e8b-8c82-1877893ba068?theme=light&version=v0.1&format=svg)


