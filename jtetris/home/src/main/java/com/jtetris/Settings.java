package com.jtetris;

import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;




public class Settings {

    boolean keyInputLocked = false;
    HashMap<String, Object> profile = null;
    HashMap<String, Object> language = null;
    HashMap<String, Object> config = null;
    String[] installedProfiles = Backend.list("profile");
    Label header, keybindSettings, mediaSettings, profileLabel, dropDown, movRight, movLeft, movDown, rotate, musicCB, volume, backgMusic, backgrounds, languages;
    Button backButton, dropButton, mvLButton, mvRButton, mvDButton, rotateButton, saveButton;
    ChoiceBox<String> languageChoiceBox;
    String cButtonId;
    private Scene scene;


    public void start(Stage primaryStage){
        //read Data for HashMaps
        try {
            config = Backend.readConfig(false, null);
            System.out.println(config.get("profile"));
            profile = Backend.readJSON("profile", (String) config.get("profile"), null);
            System.out.println(profile.get("lang"));
            language = Backend.readJSON("lang", (String) profile.get("lang"), null);
            System.out.println(language.get("lang"));
        } catch (IOException | InterruptedException e) {
            System.out.println("Something with files");
            e.printStackTrace();
        }
        //create back button to switch back to the main menu
        backButton = new Button((String) language.get("startpage"));
        backButton.setStyle("-fx-background-color: #202020d8; -fx-text-fill: #ffff; -fx-pref-width: 250px; -fx-pref-height: 50px; -fx-font-size: 20px");
        Startpage startpage = new Startpage();
        
            backButton.setOnAction(e ->  {
                try {
                startpage.start(primaryStage);
            }catch (Exception ex){
                ex.printStackTrace();
            }});
        saveButton = new Button((String) language.get("save"));
        saveButton.setStyle("-fx-background-color: #202020d8; -fx-text-fill: #ffff; -fx-pref-width: 250px; -fx-pref-height: 50px; -fx-font-size: 20px");
        //get a String array with all installed profiles
        String[] profilesList = Backend.list("profile");
        
        //create header texts
        header = new Label((String)language.get("options"));
        keybindSettings = new Label((String) language.get("keybinds"));
        mediaSettings = new Label((String) language.get("mediaSettings"));
        
        //create option to switch profiles
        profileLabel = new Label((String) language.get("profile"));
        ChoiceBox<String> profileSelect = new ChoiceBox<>();
        for (String value : profilesList){
            System.out.println(value);
            HashMap<String, Object> profileName = null;
            try {
                profileName = Backend.readJSON("custom", null, value);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            if (profileName != null){
                profileSelect.getItems().add((String) profileName.get("profileName"));
            } else {
                profileSelect.getItems().add("Could not load profile "+ value);
            }
        }
        profileSelect.setValue((String)config.get("profile"));
        
        
        saveButton.setOnAction(e -> {
            profile.put("mvLeft", mvLButton.getText());
            profile.put("mvRight", mvRButton.getText());
            profile.put("mvDown", mvDButton.getText());
            profile.put("drop", dropButton.getText());
            profile.put("rotate", rotateButton.getText());
            profile.put("lang", languageChoiceBox.getValue());
            try {
                Backend.writeProfiles(profile, profileSelect.getValue());
            } catch (InterruptedException | IOException err){
                err.printStackTrace();
            }
    });
        

        //some css presets
        String settingFontSize = "-fx-font-size:20px";
        String headerFontSize = "-fx-font-size:40px";
        String textColor = "-fx-text-fill: #ffff";

        //add presets to the headlines
        header.setStyle(headerFontSize + "; " + textColor); 
        keybindSettings.setStyle(headerFontSize + "; " + textColor);
        mediaSettings.setStyle(headerFontSize + "; " + textColor);

        //Object creation for movement keybinds
        dropDown = new Label((String) language.get("dropButton"));
        dropDown.setStyle(settingFontSize + "; " + textColor);
        movLeft = new Label((String) language.get("mvLeft"));
        movLeft.setStyle(settingFontSize + "; " + textColor);
        movRight = new Label((String) language.get("mvRight"));
        movRight.setStyle(settingFontSize + "; " + textColor);
        movDown = new Label((String) language.get("mvDown"));
        movDown.setStyle(settingFontSize + "; " + textColor);
        rotate = new Label((String) language.get("rotate"));
        rotate.setStyle(settingFontSize + "; " + textColor);

        profileLabel.setStyle(settingFontSize + "; " + textColor);

        dropButton = new Button((String) profile.get("drop"));
        dropButton.setId("drop");
        mvLButton = new Button((String) profile.get("mvLeft"));
        mvLButton.setId("mvLeft");
        mvRButton = new Button((String) profile.get("mvRight"));
        mvRButton.setId("mvRight");
        mvDButton = new Button((String) profile.get("mvDown"));
        mvDButton.setId("mvDown");
        rotateButton = new Button((String) profile.get("rotate"));
        rotateButton.setId("rotate");

        //Object creation for Media settings
        musicCB = new Label((String) language.get("music"));
        musicCB.setStyle(settingFontSize + "; " + textColor);
        volume = new Label((String) language.get("volume"));
        volume.setStyle(settingFontSize + "; " + textColor);
        backgrounds = new Label((String) language.get("backgrounds"));
        backgrounds.setStyle(settingFontSize + "; " + textColor);
        backgMusic = new Label((String) language.get("backgMusic"));
        backgMusic.setStyle(settingFontSize + "; " + textColor);
        
        //add an eventlistener to the profile selector
        profileSelect.setOnAction(e -> {
            try {
                profile = Backend.readJSON("profile", profileSelect.getValue().toString(), null);
                language = Backend.readJSON("lang", (String) profile.get("lang"), null);
                config.put("profile", (String)profile.get("profileName"));
                config.put("lang", (String) profile.get("lang"));
                if (Backend.writeConfig(config)){
                    System.out.println("config updated");
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
            //update languages
            setLanguage();
            //load keybinds 
            dropButton.setText((String) profile.get("drop"));
            mvLButton.setText((String) profile.get("mvLeft"));
            mvRButton.setText((String) profile.get("mvRight"));
            mvDButton.setText((String) profile.get("mvDown"));
            rotateButton.setText((String) profile.get("rotate"));
        });

        //create options for media settings
        CheckBox musicCheckBox = new CheckBox();
        Slider volumeSlider = new Slider();
        ChoiceBox<String> backgroundChoice = new ChoiceBox<>();
        ChoiceBox<String> musicChoiceBox = new ChoiceBox<>();
        
        //add a language option
        languages = new Label((String) language.get("languages"));
        languages.setStyle(settingFontSize + "; " + textColor);
        languageChoiceBox = new ChoiceBox<String>();

        //add all installed languages to the choicebox
        String[] langList = Backend.list("lang");
        for (String value : langList){
            System.out.println(value);
            HashMap<String, Object> langName = null;
            try {
                langName = Backend.readJSON("custom", null, value);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            if (langName != null){
                languageChoiceBox.getItems().add((String) langName.get("lang"));
            } else {
                languageChoiceBox.getItems().add("Could not load language "+ value);
            }
        }
        languageChoiceBox.setValue((String) profile.get("lang"));

        //get a background image
        Image background = new Image(new File(Backend.getXdgUserDir("DOCUMENTS")+ "/myGames/Jtetris/textures/background.png").toURI().toString());
        ImageView backgroundImage = new ImageView();
        backgroundImage.setFitWidth(1920);
        backgroundImage.setFitHeight(1080);
        backgroundImage.setImage(background);
        
        //make the 2 columns stretch across the whole screen
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(40);



        //Create Layout
        GridPane settingsList = new GridPane();
        VBox settings = new VBox(20);
        HBox profileBox = new HBox(30);
        HBox saveAndBack = new HBox(20);

        settingsList.getColumnConstraints().addAll(column1, column2);
        

        //Define the structure of the GridPane
        settingsList.setHgap(20);
        settingsList.setVgap(20);
        settingsList.setPadding(new Insets(20, 20, 20 ,20));
        settingsList.setAlignment(Pos.CENTER);

        //add Items
        settingsList.add(mediaSettings, 0, 0);
        settingsList.add(musicCB, 0, 1);
        settingsList.add(musicCheckBox, 1, 1);
        settingsList.add(volume, 0, 2);
        settingsList.add(volumeSlider, 1, 2);
        settingsList.add(backgrounds, 0, 3);
        settingsList.add(backgroundChoice, 1, 3);
        settingsList.add(backgMusic, 0, 4);
        settingsList.add(musicChoiceBox, 1, 4);
        settingsList.add(keybindSettings, 0, 5);
        settingsList.add(movLeft, 0, 6);
        settingsList.add(mvLButton, 1, 6);
        settingsList.add(movRight, 0, 7);
        settingsList.add(mvRButton, 1, 7);
        settingsList.add(movDown, 0, 8);
        settingsList.add(mvDButton, 1, 8);
        settingsList.add(dropDown, 0, 9);
        settingsList.add(dropButton, 1, 9);
        settingsList.add(rotate, 0, 10);
        settingsList.add(rotateButton, 1, 10);
        settingsList.add(languages, 0, 11);
        settingsList.add(languageChoiceBox, 1, 11);
        
        saveAndBack.getChildren().addAll(backButton, saveButton);
        saveAndBack.setAlignment(Pos.CENTER); 
        //Add everything together
        //settingsMenu.setContent(settingsList);
        profileBox.getChildren().addAll(profileLabel, profileSelect);
        settings.getChildren().addAll(header, profileBox, settingsList, saveAndBack);
        settings.setAlignment(Pos.CENTER);
        
        settingsList.setStyle("-fx-background-color: #00000060; -fx-background: #00000060"); 
        settings.setStyle("-fx-background-color: #00000050; -fx-background: #00000050");

        backgroundImage.toBack();

        //add the support for backgrounds
        StackPane layout = new StackPane(backgroundImage, settings);
        layout.setMinWidth(Region.USE_PREF_SIZE);

        scene = new Scene(layout, 1920, 1080);
        settingsList.prefWidthProperty().bind(scene.widthProperty());
        settings.prefHeightProperty().bind(scene.heightProperty());
    
        //add actions to the keybind buttons
        dropButton.setOnAction(e -> changeKeybind(dropButton, scene));
        mvRButton.setOnAction(e -> changeKeybind(mvRButton, scene));
        rotateButton.setOnAction(e -> changeKeybind(rotateButton, scene));
        mvDButton.setOnAction(e -> changeKeybind(mvDButton, scene));
        mvLButton.setOnAction(e -> changeKeybind(mvLButton, scene));
        
        languageChoiceBox.setOnAction(e -> {
            profile.put("lang", languageChoiceBox.getValue());
        });
        

        primaryStage.setScene(scene);
        primaryStage.show();

        
    }

    //add an action to every keybind setting
    public void changeKeybind(Button button, Scene scene){
        System.out.println(button.getText());
        switch(button.getId()){
            case "drop":
                if (!keyInputLocked){
                keyInputLocked = true;
                System.out.println("drop");
                button.setText("> - <");
                cButtonId = "drop";
                scene.addEventFilter(KeyEvent.KEY_PRESSED, this::setBind);
                keyInputLocked = false;
                }
                break;
            case "mvRight":
                if (!keyInputLocked){
                keyInputLocked = true;
                System.out.println("right");
                button.setText("> - <");
                cButtonId = "mvRight";
                scene.addEventFilter(KeyEvent.KEY_PRESSED, this::setBind);
                keyInputLocked = false;
                }
                break;
            case "mvLeft":
                if (!keyInputLocked){
                keyInputLocked = true;
                System.out.println("left");
                button.setText("> - <");
                cButtonId = "mvLeft";
                scene.addEventFilter(KeyEvent.KEY_PRESSED, this::setBind);
                keyInputLocked = false;
                }
                break;
            case "mvDown":
                if (!keyInputLocked){
                keyInputLocked = true;
                System.out.println("down");
                button.setText("> - <");
                cButtonId = "mvDown";
                scene.addEventFilter(KeyEvent.KEY_PRESSED, this::setBind);
                keyInputLocked = false;
                }
                break;
            case "rotate":
                if (!keyInputLocked){
                keyInputLocked = true;
                System.out.println("rotate");
                button.setText("> - <");
                cButtonId = "rotate";
                scene.addEventFilter(KeyEvent.KEY_PRESSED, this::setBind);
                keyInputLocked = false;
                }
                break;
            default:
                System.out.println("nothing");
                System.out.println(button.getId());
                break;
        }
    }
    
    //function for changing languages of all elements in one go
    private void setLanguage(){
        header.setText((String) language.get("options"));
        keybindSettings.setText((String) language.get("keybinds"));
        mediaSettings.setText((String) language.get("mediaSettings"));
        dropDown.setText((String) language.get("dropButton"));
        movDown.setText((String) language.get("mvDown"));
        movLeft.setText((String) language.get("mvLeft"));
        movRight.setText((String) language.get("mvRight"));
        rotate.setText((String) language.get("rotate"));
        musicCB.setText((String) language.get("music"));
        volume.setText((String) language.get("volume"));
        backgrounds.setText((String) language.get("backgrounds"));
        backgMusic.setText((String) language.get("backgMusic"));
        backButton.setText((String) language.get("startpage"));
        languageChoiceBox.setValue((String) profile.get("lang"));
        languages.setText((String) language.get("languages"));
    }

    //handle the events created by the changeKeybind function
    private void setBind(KeyEvent event){
        KeyCode keyCode = event.getCode();
        Button button = (Button) scene.lookup("#"+cButtonId);
        switch(keyCode){
            case UP:
            case DOWN:
            case RIGHT:
            case LEFT:
            case SPACE:
                event.consume();
                button.setText(keyCode.toString());
                break;
            case ESCAPE:
                event.consume();
                button.setText((String) profile.get(button.getId()));
                break;
            default:
                button.setText(keyCode.toString());
                break;
        }

    }
    
}
