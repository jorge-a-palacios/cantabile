package com.mycompany.cantabilegui;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class MelodicDictation extends Application {
    private Stage mainStage;
    private Scene moduleScene;
    private Scene configScene;
    private ChordQuestion currentQuestion;
    private ArrayList<CheckBox> userNotes = new ArrayList<CheckBox>();
    private ArrayList<Button> options = new ArrayList<Button>();
    private Note[] moduleNotes = new Note[4];
    private int totalQuestions;
    private int correctQuestions;
    private Text userNotesText;
    private Button globalSubmit;
    private int userNotesInput;
    private String[] userNotesAnswer = new String[4];
    private CheckBox playSound;
    
    @Override
    public void start(Stage stage) {
        mainStage = stage;
        
        String[] notes = {"C", "D", "Eb", "F", "Gb", "G", "A", "Bb", "C"};
        
        VBox configRoot = new VBox();
        Text configTitle = new Text("Melodic Dictation Module Config");
        configRoot.setAlignment(Pos.CENTER);
        // clef options
        /*Text clefOptionsText = new Text("Clefs:");
        HBox clefOptions = new HBox(clefOptionsText);
        clefOptions.setAlignment(Pos.CENTER);
        clefOptions.setSpacing(10);
        
        for (String clef : clefs) {
            CheckBox c = new CheckBox(clef);
            clefOptions.getChildren().add(c); 
        }*/
        
        // preset options
        /*Text presetOptionsText = new Text("Preset: ");
        ComboBox presetSelection = new ComboBox(FXCollections.observableArrayList(presets));
        presetSelection.getSelectionModel().selectFirst();
        HBox presetOptions = new HBox(presetOptionsText, presetSelection);
        presetOptions.setAlignment(Pos.CENTER);
        presetOptions.setSpacing(10);*/
        

        // chord options
        Text notesOptionsText = new Text("Notes: ");
        HBox notesOptions = new HBox(notesOptionsText);
        notesOptions.setAlignment(Pos.CENTER);

        for (String note : notes) {
            CheckBox c = new CheckBox(note);
            c.setSelected(true);
            notesOptions.getChildren().add(c);
            userNotes.add(c);
        }
        
        CheckBox playWithSound = new CheckBox("Instant playback");
        playWithSound.setSelected(false);
        HBox playSoundView = new HBox(playWithSound);
        playSoundView.setAlignment(Pos.CENTER);
        playSound = playWithSound;
        
        // begin questions
        Button initializeModuleButton = new Button("Begin Module");
        initializeModuleButton.setOnAction(this::processModuleInitialization);
        
        
        configRoot.setSpacing(25);

        configRoot.getChildren().addAll(configTitle, notesOptions, playSoundView, initializeModuleButton);
        
        // Set scene with centered layout
        Scene scene = new Scene(configRoot, 600, 600);
        configScene = scene;
        
        stage.setTitle("cantabile");
        stage.setScene(scene);
        stage.show();
    }
    
    public void processModuleInitialization(ActionEvent event) {
        // check if the user changed any settings
        ArrayList<String> notes = new ArrayList<String>();
        
        for (CheckBox c : userNotes) {
            if (c.isSelected()) {
                notes.add(c.getText());
            }
        }
        
        
        // Top section: Module and score
        Text moduleText = new Text("Melodic Dictation Module");
        Text scoreText = new Text(correctQuestions + " / " + totalQuestions + "  correct");
        VBox topSection = new VBox(5, moduleText, scoreText);
        topSection.setAlignment(Pos.CENTER);

        // Middle section: Question text
        Text questionText = new Text("Identify the notes played.");
        questionText.setStyle("-fx-font-size: 16px;");

        Note[] randomNotes = new Note[4];
        // Generate a set of 4 random notes from the user input
        for (int i = 0; i < 4; i++) {
            Random random = new Random();
            int randomIndex = random.nextInt(notes.size());
            randomNotes[i] = new Note(notes.get(randomIndex));
        }
        moduleNotes = randomNotes;
        
        
        //Chord minterval = ChordBuilder.randomChord(chords);
        //ChordQuestion question = new ChordQuestion(minterval);
        //minterval.playSound();
        //currentQuestion = question;
        
        // This section for generating the clef and note image
        StackPane notationSection = new StackPane();
        
        ImageView audio = new ImageView(getClass().getResource("/audio.png").toExternalForm());
        audio.setFitHeight(175);
        notationSection.getChildren().addAll(audio);
           // to repeat
        Button repeatButton = new Button("Replay Sound");
        repeatButton.setOnAction(this::processSoundButton);
        HBox repeatButtonRow = new HBox();
        repeatButtonRow.setAlignment(Pos.CENTER);
        
        // reset notes
        Button resetNotes = new Button("Reset Notes");
        resetNotes.setOnAction(this::processResetNotes);
        
        // use only the user-specified qualities
        HBox buttonRow = new HBox(10);
        for (String note : notes) {
            Button button = new Button(note);
            button.setOnAction(this::processUserInput);
            buttonRow.getChildren().add(button);
            options.add(button);
        }
        buttonRow.setAlignment(Pos.CENTER);
        
        // config icon for nav
        ImageView gearIcon = new ImageView(new Image(getClass().getResource("/gear.png").toExternalForm()));
        gearIcon.setFitWidth(50);  
        gearIcon.setFitHeight(50);
        gearIcon.setOnMouseClicked(this::processConfigInitialization);
        
        Text userResponse = new Text("Notes Played: ");
        userNotesText = userResponse;
        
        Button submit = new Button("Submit Answer");
        submit.setOnAction(this::processAnswer);
        submit.setDisable(true);
        globalSubmit = submit;
        
          // Main VBox layout (content)
        VBox mainLayout = new VBox(15, topSection, questionText, notationSection, repeatButton, buttonRow, resetNotes, userResponse, submit);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setTranslateY(-50);  // Move the content slightly upward
        
                // StackPane for centered content
        StackPane contentPane = new StackPane(mainLayout);
        //contentPane.setStyle("-fx-background-color: purple;");
            // BorderPane for top-right positioning of the gear icon
        BorderPane root = new BorderPane();
        root.setCenter(contentPane);
        root.setTop(gearIcon);
        BorderPane.setAlignment(gearIcon, Pos.TOP_RIGHT);
        BorderPane.setMargin(gearIcon, new javafx.geometry.Insets(10, 10, 0, 0));  // Add margin from the corner

        Scene moduleSceneA = new Scene(root, 600, 600);
        moduleScene = moduleSceneA;
        
        mainStage.setScene(moduleScene);
        
               
        // this wont block the thread        
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            try {
            //Thread.sleep(2000);
            
            for (int i = 0; i < 4;i++) {
                randomNotes[i].playSound();
                TimeUnit.SECONDS.sleep(2);
            }
            
            scheduler.shutdown(); 
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        }, 1, TimeUnit.SECONDS);



    }
    
    public void processConfigInitialization(MouseEvent event) {
        mainStage.setScene(configScene);
    }
    
    public void processResetNotes(ActionEvent event) {
        userNotesInput = 0;
        userNotesAnswer = new String[4];
        userNotesText.setText("Notes Played: ");
        for (Button button : options) {
            button.setDisable(false);
        }
        globalSubmit.setDisable(true);
        
    }
    
    public void processAnswer(ActionEvent event) {
        boolean correctAnswer = true;
        // compare corredct with user input;
        for (int i = 0; i < 4; i++) {
            if (!moduleNotes[i].getName().equals(userNotesAnswer[i])) {
                correctAnswer = false;
                break;
            }
        }
        
        String[] finalAnswer = new String[4];
        for (int i = 0; i < 4; i++) {
            finalAnswer[i] = moduleNotes[i].getName();
        }
        
        Dialog<String> dialog = new Dialog<String>();
      //Setting the title
      dialog.setTitle("Dialog");
      ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
      //Setting the content of the dialog
      String answer = String.join(", ", finalAnswer);
      String dialogText = "";
      if (correctAnswer) {
          dialogText = "Correct! The answer is " + answer;
          correctQuestions++;
      } else {
          dialogText = "Incorrect! The answer is " + answer;
      }
      totalQuestions++;
      dialog.setContentText(dialogText);
      //Adding buttons to the dialog pane
      dialog.getDialogPane().getButtonTypes().add(type);
      processResetNotes(event);
      dialog.showAndWait();
      processModuleInitialization(event);
    }
    
    public void processSoundButton(ActionEvent event) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            try {
            //Thread.sleep(2000);
            
            for (int i = 0; i < 4;i++) {
                moduleNotes[i].playSound();
                TimeUnit.SECONDS.sleep(2);
            }
            
            scheduler.shutdown(); 
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        }, 1, TimeUnit.SECONDS);
    }
    
    public void processUserInput(ActionEvent event) {
  
      
        String userInput = ((Button)event.getSource()).getText();
        userNotesText.setText(userNotesText.getText() + " | " + userInput);
        userNotesInput +=1;
        userNotesAnswer[userNotesInput-1]= userInput;
        
        if (playSound.isSelected()) {
            Note note = new Note(userInput);
            note.playSound();
        }
        
        if (userNotesInput == 4) {
            // disable the buttons
            for (Button button : options) {
                button.setDisable(true);
            }
            
            globalSubmit.setDisable(false);
        }
      //Creating a dialog
      /*Dialog<String> dialog = new Dialog<String>();
      //Setting the title
      dialog.setTitle("Dialog");
      ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
      //Setting the content of the dialog
      String answer = ((Button)event.getSource()).getText();
      String dialogText = "";
      if (currentQuestion.checkAnswer(answer)) {
          dialogText = "Correct! The answer is " + currentQuestion.getAnswer();
          correctQuestions++;
      } else {
          dialogText = "Incorrect! The answer is " + currentQuestion.getAnswer();
      }
      totalQuestions++;
      dialog.setContentText(dialogText);
      //Adding buttons to the dialog pane
      dialog.getDialogPane().getButtonTypes().add(type);
      dialog.showAndWait();
      processModuleInitialization(event);*/
    }
}
