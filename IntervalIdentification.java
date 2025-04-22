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

public class IntervalIdentification extends Application {
    private Stage mainStage;
    private Scene moduleScene;
    private Scene configScene;
    private IntervalQuestion currentQuestion;
    private ArrayList<CheckBox> userIntervals = new ArrayList<CheckBox>();
    private int totalQuestions = 0;
    private int correctQuestions = 0;
    private CheckBox playSound;
    
    @Override
    public void start(Stage stage) {
        mainStage = stage;
        
        String[] clefs = {"treble", "bass"};
        String[] presets = {"all", "simple", "custom"};
        String[] intervals = {"m2", "M2", "m3", "M3", "P4", "d5", "P5", "m6", "M6", "m7", "M7"};
        
        VBox configRoot = new VBox();
        Text configTitle = new Text("Interval Identification Module Config");
        configRoot.setAlignment(Pos.CENTER);
        // clef options
        /*Text clefOptionsText = new Text("Clefs:");
        HBox clefOptions = new HBox(clefOptionsText);
        clefOptions.setAlignment(Pos.CENTER);
        clefOptions.setSpacing(10);
        
        for (String clef : clefs) {
            CheckBox c = new CheckBox(clef);
            clefOptions.getChildren().add(c); 
        }
        
        // preset options
        Text presetOptionsText = new Text("Preset: ");
        ComboBox presetSelection = new ComboBox(FXCollections.observableArrayList(presets));
        presetSelection.getSelectionModel().selectFirst();
        HBox presetOptions = new HBox(presetOptionsText, presetSelection);
        presetOptions.setAlignment(Pos.CENTER);
        presetOptions.setSpacing(10);*/

        //Text playSoundText = new Text("Play with sound: ");
        CheckBox playWithSound = new CheckBox("Play with sound");
        playWithSound.setSelected(false);
        HBox playSoundView = new HBox(playWithSound);
        playSound = playWithSound;
        playSoundView.setAlignment(Pos.CENTER);
        
        // interval options
        Text intervalOptionsText = new Text("Intervals: ");
        HBox intervalOptions = new HBox(intervalOptionsText);
        intervalOptions.setAlignment(Pos.CENTER);

        for (String interval : intervals) {
            CheckBox c = new CheckBox(interval);
            c.setSelected(true);
            intervalOptions.getChildren().add(c);
            userIntervals.add(c);
        }
        
        // begin questions
        Button initializeModuleButton = new Button("Begin Module");
        initializeModuleButton.setOnAction(this::processModuleInitialization);
        
        configRoot.setSpacing(25);
        configRoot.getChildren().addAll(configTitle, intervalOptions, playSoundView, initializeModuleButton);
        
        // Set scene with centered layout
        Scene scene = new Scene(configRoot, 600, 600);
        configScene = scene;
        
        stage.setTitle("cantabile");
        stage.setScene(scene);
        stage.show();
    }
    
    public void processModuleInitialization(ActionEvent event) {
        // check if the user changed any settings
        ArrayList<String> intervals = new ArrayList<String>();
        
        for (CheckBox c : userIntervals) {
            if (c.isSelected()) {
                intervals.add(c.getText());
            }
        }
        
        
         
        // ====================== Main Module ================================
        
        
        // Top section: Module and score
        Text moduleText = new Text("Interval Identification Module");
        Text scoreText = new Text(correctQuestions + " / " + totalQuestions + "  correct");
        VBox topSection = new VBox(5, moduleText, scoreText);
        topSection.setAlignment(Pos.CENTER);

        // Middle section: Question text
        Text questionText = new Text("Identify the interval shown.");
        questionText.setStyle("-fx-font-size: 16px;");

        // This section for generating the clef and note image
        StackPane notationSection = new StackPane();
        
        // Static image
        ImageView staff = new ImageView(getClass().getResource("/new.png").toExternalForm());
        staff.setFitWidth(300);
        staff.setPreserveRatio(true);
        
        // clef
        ImageView clef = new ImageView(getClass().getResource("/treble.png").toExternalForm());
        clef.setFitHeight(175);
        clef.setTranslateY(10);
        clef.setTranslateX(-120);
        clef.setPreserveRatio(true);
        
        // determine the position of the two notes
        // the lowest a note can go is middle C (notes with ledger lines will be calculated)
        //Interval minterval = IntervalBuilder.createInterval(new Note("Ab"), "diminished", 5);
        int len = intervals.size();
        String[] USER = new String[len];
        for (int i = 0; i < len; i++) {
            USER[i] = intervals.get(i);
        }
        Interval minterval = IntervalBuilder.randomInterval(USER);
        IntervalQuestion question = new IntervalQuestion(minterval);
        currentQuestion = question;
        if (playSound.isSelected()) {
            minterval.playSound();
        }
        //System.out.println(minterval.toString());
        String img = "/note.png";
        //SPECIAL CASE: for c, use ledger ling
        if (minterval.getRootNote().getName().substring(0,1).equals("C")) {
            img = "/ledger_note.png";
        }
        
        ImageView rootNote = new ImageView(getClass().getResource(img).toExternalForm());
        // all note positions will be calculated based on the distance from middle C
        // the distance between each space/line on this staff is about 15.2
        // to calculate the position of the root note, multiply 15.21 by the basicDistance from C and sub from 90.
        int rootBasicDistance = IntervalBuilder.getBasicDistance(new Note("C"), minterval.getRootNote());
        double rootPosition = 90 - (15.21 * (rootBasicDistance - 1));
        rootNote.setFitHeight(55);
        rootNote.setTranslateY(rootPosition);
        rootNote.setPreserveRatio(true);
        // check for accidentals
        if (minterval.getRootNote().getName().length() >= 2) {
            String accidental = minterval.getRootNote().getName().substring(1);
            //System.out.println(accidental);
            String accidentalImg = null;
            int yOffset = 0;
            int xOffset = 0;
            int fitHeight = 0;
            int fitWidth = 0;
            switch (accidental) {
                case "b":
                    accidentalImg = "/flat.png";
                    yOffset = -11;
                    xOffset = -30;
                    fitHeight = 50;
                    fitWidth = 20;
                    break;
                case "#":
                    accidentalImg = "/sharp.png";
                    xOffset = -35;
                    fitHeight = 50;
                    fitWidth = 30;
                    break;
                case "bb":
                    accidentalImg = "/dflat.png";
                    break;
            }
            ImageView accidentalView = new ImageView(getClass().getResource(accidentalImg).toExternalForm());
            accidentalView.setFitHeight(fitHeight);
            accidentalView.setFitWidth(fitWidth);
            accidentalView.setTranslateY(rootPosition+yOffset);
            accidentalView.setTranslateX(xOffset);
            notationSection.getChildren().add(accidentalView);
        }
        
        ImageView intervalNote = new ImageView(getClass().getResource("/note.png").toExternalForm());
        // to calculate the position of the interval note, multiply 15.21 by the basicDistance from root note and sub from rootPosition.
        int intervalBasicDistance = IntervalBuilder.getBasicDistance(minterval.getRootNote(), minterval.getIntervalNote());
        double intervalPosition = rootPosition - (15.21 * (intervalBasicDistance - 1));
        intervalNote.setFitHeight(55); 
        intervalNote.setTranslateY(intervalPosition);
        // SPECIAL CASE: if the interval is a 2nd, move the note to the right to prevent them from overlapping
        if (intervalBasicDistance == 2) {
            intervalNote.setTranslateX(35);
        }
        if (minterval.getIntervalNote().getName().length() >= 2) {
            String accidental = minterval.getIntervalNote().getName().substring(1);
            //ut.println(accidental);
            String accidentalImg = null;
            int yOffset = 0;
            int xOffset = 0;
            int fitHeight = 0;
            int fitWidth = 0;
            switch (accidental) {
                case "b":
                    accidentalImg = "/flat.png";
                    yOffset = -11;
                    xOffset = -30;
                    fitHeight = 50;
                    fitWidth = 20;
                    break;
                case "#":
                    accidentalImg = "/sharp.png";
                    xOffset = -35;
                    fitHeight = 50;
                    fitWidth = 30;
                    break;
                case "bb":
                    accidentalImg = "/dflat.png";
                    fitHeight = 50;
                    fitWidth = 30;
                    yOffset = -10;
                    xOffset = -40;
                    break;
            }
            ImageView accidentalView = new ImageView(getClass().getResource(accidentalImg).toExternalForm());
            accidentalView.setFitHeight(fitHeight);
            accidentalView.setFitWidth(fitWidth);
            accidentalView.setTranslateY(intervalPosition+yOffset);
            accidentalView.setTranslateX(xOffset);
            notationSection.getChildren().add(accidentalView);
        }
        
        intervalNote.setPreserveRatio(true);
        
        //notationSection.setAlignment(Pos.CENTER);
        notationSection.getChildren().addAll(staff, clef, rootNote, intervalNote);
        
         // to repeat
        Button repeatButton = new Button("Replay Sound");
        repeatButton.setOnAction(this::processSoundButton);
        repeatButton.setDisable(true);
        HBox repeatButtonRow = new HBox();
        repeatButtonRow.setAlignment(Pos.CENTER);
        if (playSound.isSelected()) {
            repeatButton.setDisable(false);
        }
        
        // use only the user-specified intervals
        HBox buttonRow = new HBox(10);
        for (String interval : intervals) {
            Button button = new Button(interval);
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
        currentQuestion.getInterval().playSound();
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
