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

    HashMap<String, Object> profile = null;
    HashMap<String, Object> language = null;
    HashMap<String, Object> config = null;
    String[] installedProfiles = Backend.list("profile");
    Label header, keybindSettings, mediaSettings, profileLabel, dropDown, movRight, movLeft, movDown, rotate, musicCB, volume, backgMusic, backgrounds;
    Button backButton;


    public void start(Stage primaryStage){
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

        backButton = new Button((String) language.get("startpage"));
        backButton.setStyle("-fx-background-color: #202020d8; -fx-text-fill: #ffff; -fx-pref-width: 250px; -fx-pref-height: 50px; -fx-font-size: 20px");
        Startpage startpage = new Startpage();
        
            backButton.setOnAction(e ->  {
                try {
                startpage.start(primaryStage);
            }catch (Exception ex){
                ex.printStackTrace();
            }});


        String[] profilesList = Backend.list("profile");
        
        header = new Label((String)language.get("options"));
        keybindSettings = new Label((String) language.get("keybinds"));
        mediaSettings = new Label((String) language.get("mediaSettings"));

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
        
        
        
        String settingFontSize = "-fx-font-size:20px";
        String headerFontSize = "-fx-font-size:40px";
        String textColor = "-fx-text-fill: #ffff";
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

        Button dropButton = new Button((String) profile.get("drop"));
        dropButton.setId("drop");
        Button mvLButton = new Button((String) profile.get("mvLeft"));
        mvLButton.setId("left");
        Button mvRButton = new Button((String) profile.get("mvRight"));
        mvRButton.setId("right");
        Button mvDButton = new Button((String) profile.get("mvDown"));
        mvDButton.setId("down");
        Button rotateButton = new Button((String) profile.get("rotate"));
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
            setLanguage();
            dropButton.setText((String) profile.get("drop"));
            mvLButton.setText((String) profile.get("mvLeft"));
            mvRButton.setText((String) profile.get("mvRight"));
            mvDButton.setText((String) profile.get("mvDown"));
            rotateButton.setText((String) profile.get("rotate"));
        });


        CheckBox musicCheckBox = new CheckBox();
        Slider volumeSlider = new Slider();
        ChoiceBox<String> backgroundChoice = new ChoiceBox<>();
        ChoiceBox<String> musicChoiceBox = new ChoiceBox<>();


        Image background = new Image(new File(Backend.getXdgUserDir("DOCUMENTS")+ "/myGames/Jtetris/textures/background.png").toURI().toString());
        ImageView backgroundImage = new ImageView();
        backgroundImage.setFitWidth(1920);
        backgroundImage.setFitHeight(1080);
        backgroundImage.setImage(background);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(40);



        //Create Layout
        //ScrollPane settingsMenu = new ScrollPane();
        GridPane settingsList = new GridPane();
        VBox settings = new VBox(20);
        HBox profileBox = new HBox(30);

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
        

               
        //Add everything together
        //settingsMenu.setContent(settingsList);
        profileBox.getChildren().addAll(profileLabel, profileSelect);
        settings.getChildren().addAll(header, profileBox, settingsList, backButton);
        settings.setAlignment(Pos.CENTER);
        
        settingsList.setStyle("-fx-background-color: #00000060; -fx-background: #00000060"); 
        settings.setStyle("-fx-background-color: #00000050; -fx-background: #00000050");

        backgroundImage.toBack();
        StackPane layout = new StackPane(backgroundImage, settings);
        layout.setMinWidth(Region.USE_PREF_SIZE);

        Scene scene = new Scene(layout, 1920, 1080);
        settingsList.prefWidthProperty().bind(scene.widthProperty());
        settings.prefHeightProperty().bind(scene.heightProperty());


        dropButton.setOnAction(e -> changeKeybind(dropButton, scene));
        mvRButton.setOnAction(e -> changeKeybind(mvRButton, scene));
        rotateButton.setOnAction(e -> changeKeybind(rotateButton, scene));
        mvDButton.setOnAction(e -> changeKeybind(mvDButton, scene));
        mvLButton.setOnAction(e -> changeKeybind(mvLButton, scene));


        primaryStage.setScene(scene);
        primaryStage.show();

        
    }

    
    public void changeKeybind(Button button, Scene scene){
        System.out.println(button.getText());
        switch(button.getId()){
            case "drop":
                System.out.println("drop");
                button.setText("> - <");
                scene.setOnKeyPressed(e -> {
                    KeyCode keyCode = e.getCode();
                    System.out.println(keyCode);
                    button.setText(keyCode.toString());
                });
                break;
            case "right":
                System.out.println("right");
                button.setText("> - <");
                scene.setOnKeyPressed(e -> {
                    KeyCode keyCode = e.getCode();
                    System.out.println(keyCode);
                    button.setText(keyCode.toString());
                });
                break;
            case "left":
                System.out.println("left");
                button.setText("> - <");
                scene.setOnKeyPressed(e -> {
                    KeyCode keyCode = e.getCode();
                    System.out.println(keyCode);
                    button.setText(keyCode.toString());
                });
                break;
            case "down":
                System.out.println("down");
                button.setText("> - <");
                scene.setOnKeyPressed(e -> {
                    KeyCode keyCode = e.getCode();
                    System.out.println(keyCode);
                    button.setText(keyCode.toString());
                });
                break;
            case "rotate":
                System.out.println("rotate");
                button.setText("> - <");
                scene.setOnKeyPressed(e -> {
                    KeyCode keyCode = e.getCode();
                    System.out.println(keyCode);
                    button.setText(keyCode.toString());
                });
                break;
            default:
                System.out.println("nothing");
                System.out.println(button.getId());
                break;
        }
    }
    

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
    }
}
