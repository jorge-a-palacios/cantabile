package com.mycompany.cantabilegui;

import java.util.ArrayList;
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

public class ChordTraining extends Application {
    private Stage mainStage;
    private Scene moduleScene;
    private Scene configScene;
    private ChordQuestion currentQuestion;
    private ArrayList<CheckBox> userQualities = new ArrayList<CheckBox>();
    private int totalQuestions;
    private int correctQuestions;
    
    @Override
    public void start(Stage stage) {
        mainStage = stage;
        
        String[] qualities = {"major", "minor", "diminished", "major7", "minor7", "dom7"};
        
        VBox configRoot = new VBox();
        Text configTitle = new Text("Chord Ear Training Module Config");
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
        Text chordOptionsText = new Text("Chords: ");
        HBox chordOptions = new HBox(chordOptionsText);
        chordOptions.setAlignment(Pos.CENTER);

        for (String interval : qualities) {
            CheckBox c = new CheckBox(interval);
            c.setSelected(true);
            chordOptions.getChildren().add(c);
            userQualities.add(c);
        }
        
        // begin questions
        Button initializeModuleButton = new Button("Begin Module");
        initializeModuleButton.setOnAction(this::processModuleInitialization);
        
        
        configRoot.setSpacing(25);

        configRoot.getChildren().addAll(configTitle, chordOptions, initializeModuleButton);
        
        // Set scene with centered layout
        Scene scene = new Scene(configRoot, 600, 600);
        configScene = scene;
        
        stage.setTitle("cantabile");
        stage.setScene(scene);
        stage.show();
    }
    
    public void processModuleInitialization(ActionEvent event) {
        // check if the user changed any settings
        ArrayList<String> chords = new ArrayList<String>();
        
        for (CheckBox c : userQualities) {
            if (c.isSelected()) {
                chords.add(c.getText());
            }
        }
        
        
        // Top section: Module and score
        Text moduleText = new Text("Chord Ear Training Module");
        Text scoreText = new Text(correctQuestions + " / " + totalQuestions + "  correct");
        VBox topSection = new VBox(5, moduleText, scoreText);
        topSection.setAlignment(Pos.CENTER);

        // Middle section: Question text
        Text questionText = new Text("Identify the chord played.");
        questionText.setStyle("-fx-font-size: 16px;");

        Chord minterval = ChordBuilder.randomChord(chords);
        ChordQuestion question = new ChordQuestion(minterval);
        minterval.playSound();
        currentQuestion = question;
        
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
        
        
        // use only the user-specified qualities
        HBox buttonRow = new HBox(10);
        for (String quality : chords) {
            Button button = new Button(quality);
            button.setOnAction(this::processUserInput);
            buttonRow.getChildren().add(button);
        }
        buttonRow.setAlignment(Pos.CENTER);
        
        // config icon for nav
        ImageView gearIcon = new ImageView(new Image(getClass().getResource("/gear.png").toExternalForm()));
        gearIcon.setFitWidth(50);  
        gearIcon.setFitHeight(50);
        gearIcon.setOnMouseClicked(this::processConfigInitialization);
          // Main VBox layout (content)
        VBox mainLayout = new VBox(15, topSection, questionText, notationSection, repeatButton, buttonRow);
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
    }
    
    public void processConfigInitialization(MouseEvent event) {
        mainStage.setScene(configScene);
    }
    
    public void processSoundButton(ActionEvent event) {
        currentQuestion.getChord().playSound();
    }
    
    public void processUserInput(ActionEvent event) {
      //Creating a dialog
      Dialog<String> dialog = new Dialog<String>();
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
      processModuleInitialization(event);
    }
}
