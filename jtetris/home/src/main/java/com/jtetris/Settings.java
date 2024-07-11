package com.jtetris;

import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Settings {

    HashMap<String, Object> profile = null;
    HashMap<String, Object> language = null;
    HashMap<String, Object> config = null;
    String[] installedProfiles = Backend.profilesList();



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

        Button backButton = new Button((String) language.get("startpage"));
        Startpage startpage = new Startpage();
        
            backButton.setOnAction(e ->  {
                try {
                startpage.start(primaryStage);
            }catch (Exception ex){
                ex.printStackTrace();
            }});
        
        Label header = new Label((String)language.get("options"));
        Label keybindSettings = new Label((String) language.get("keybinds"));
        Label mediaSettings = new Label((String) language.get("mediaSettings"));

        Label profileLabel = new Label((String) language.get("profile"));
        ChoiceBox<String> profileSelect = new ChoiceBox<>();
        
        
        

        //Object creation for movement keybinds
        Label dropDown = new Label((String) language.get("dropButton"));
        Label movLeft = new Label((String) language.get("mvLeft"));
        Label movRight = new Label((String) language.get("mvRight"));
        Label movDown = new Label((String) language.get("mvDown"));
        Label rotate = new Label((String) language.get("rotate"));

        Button dropButton = new Button((String) profile.get("drop"));
        Button mvLButton = new Button((String) profile.get("mvLeft"));
        Button mvRButton = new Button((String) profile.get("mvRight"));
        Button mvDButton = new Button((String) profile.get("mvDown"));
        Button rotateButton = new Button((String) profile.get("rotate"));

        //Object creation for Media settings
        Label musicCB = new Label((String) language.get("music"));
        Label volume = new Label((String) language.get("volume"));
        Label backgrounds = new Label((String) language.get("backgrounds"));
        Label backgMusic = new Label((String) language.get("backgMusic"));

        CheckBox musicCheckBox = new CheckBox();
        Slider volumeSlider = new Slider();
        ChoiceBox<String> backgroundChoice = new ChoiceBox<>();
        ChoiceBox<String> musicChoiceBox = new ChoiceBox<>();


        //Create Layout
        ScrollPane settingsMenu = new ScrollPane();
        VBox settingsList = new VBox(10);
        HBox[] settingsItems = new HBox[11];
        VBox settings = new VBox();
        HBox profileBox = new HBox();
        HBox test = new HBox();

        //add HBox entries
        
        settingsItems[0].getChildren().addAll(mediaSettings, profileLabel);
        settingsItems[1].getChildren().addAll(musicCB, musicCheckBox);
        settingsItems[2].getChildren().addAll(volume, volumeSlider);
        settingsItems[3].getChildren().addAll(backgMusic, musicChoiceBox);
        settingsItems[4].getChildren().addAll(backgrounds, backgroundChoice);
        settingsItems[5].getChildren().add(keybindSettings);
        settingsItems[6].getChildren().addAll(movLeft, mvLButton);
        settingsItems[7].getChildren().addAll(movRight, mvRButton);
        settingsItems[8].getChildren().addAll(movDown, mvDButton);
        settingsItems[9].getChildren().addAll(dropDown, dropButton);
        settingsItems[10].getChildren().addAll(rotate, rotateButton);

        //Add everything together
        settingsList.getChildren().addAll(settingsItems);
        settingsMenu.setContent(settingsList);
        profileBox.getChildren().addAll(profileLabel, profileSelect);
        settings.getChildren().addAll(header, profileBox, settingsMenu, backButton);
        
        

        BorderPane layout = new BorderPane(settings);

        Scene scene = new Scene(layout, 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.show();

        
    }
    
}
