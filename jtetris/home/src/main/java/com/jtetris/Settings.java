package com.jtetris;

import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.stage.Popup;
import javafx.scene.shape.Rectangle;
import java.util.Locale;




public class Settings {

    boolean keyInputLocked = false;
    HashMap<String, Object> profile = null;
    HashMap<String, Object> language = null;
    HashMap<String, Object> config = null;
    String[] installedProfiles = Backend.list("profile");
    Label header, generalSettings, keybindSettings, mediaSettings, showGhost, profileLabel, dropDown, movRight, movLeft, movDown, rotate, musicCB, volume, backgMusic, backgrounds, languages, popupDescription;
    Button backButton, dropButton, mvLButton, mvRButton, mvDButton, rotateButton, saveButton, acceptButton, cancelButton, deleteProfile;
    ChoiceBox<String> languageChoiceBox, backgroundChoice, musicChoiceBox, profileSelect;
    CheckBox showGhostTetromino;
    String cButtonId;
    private Scene scene;
    Slider volumeSlider = null;
    double volumeInit;
    Popup newProfName = null;
    Image binIcon = new Image(new File(Backend.getXdgUserDir("DOCUMENTS") + "/myGames/Jtetris/textures/recyclebin.png").toURI().toString());
    ImageView binIconView = new ImageView(binIcon);
    ScrollPane scrollSettingsMenu;
    Insets settingsPadding;
    Rectangle coloredTrack;

    //some css presets
    String settingFontSize = "-fx-font-size:20px";
    String headerFontSize = "-fx-font-size:40px";
    String textColor = "-fx-text-fill: #ffff";



    public void start(Stage primaryStage){
        //read Data for HashMaps
        try {
            config = Backend.readConfig(false, null);
            profile = Backend.readJSON("profile", (String) config.get("profile"), null);
            language = Backend.readJSON("lang", (String) profile.get("lang"), null);
        } catch (IOException | InterruptedException e) {
            System.out.println("Something with files");
            e.printStackTrace();
        }
        //create back button to switch back to the main menu
        backButton = new Button((String) language.get("startpage"));
        Startpage startpage = new Startpage();
        
            backButton.setOnAction(e ->  {
                try {
                startpage.start(primaryStage);
            }catch (Exception ex){
                ex.printStackTrace();
            }});
        saveButton = new Button((String) language.get("save"));
               
        //create header texts
        header = new Label((String)language.get("options"));
        keybindSettings = new Label((String) language.get("keybinds"));
        generalSettings = new Label((String) language.get("general"));
        mediaSettings = new Label((String) language.get("mediaSettings"));
        showGhost = new Label((String) language.get("showGhost"));
        
        //create option to switch profiles
        profileLabel = new Label((String) language.get("profile"));
        profileSelect = new ChoiceBox<>();
        loadProfileSelector();    


        //general settings
        generalSettings.setStyle(headerFontSize);
        showGhost.setStyle(settingFontSize);
        showGhostTetromino = new CheckBox();
        if (profile.get("showGhost") == null) {
            profile.put("showGhost", true);
        }
        showGhostTetromino.setSelected((boolean) profile.get("showGhost"));
        
        

        newProfName = new Popup();
        StackPane popup = new StackPane();
        popupDescription = new Label((String) language.get("enterName"));
        TextField nameField = new TextField();
        acceptButton = new Button((String) language.get("accept"));
        cancelButton = new Button((String) language.get("cancel"));
        VBox popupVertStructure = new VBox(10);
        HBox buttons = new HBox(10);
        HBox textField = new HBox(10);
        CornerRadii popupCornerRadii = new CornerRadii(5);
        Insets popupPadding = new Insets(10);
        Border popupBorder = new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, popupCornerRadii, new BorderWidths(2)));

        popupVertStructure.setStyle("-fx-font-size: 20px");
        popupDescription.setStyle("-fx-text-fill: #ffff");
        popup.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        popup.setBorder(popupBorder);
        popup.setPadding(popupPadding);
        textField.getChildren().addAll(popupDescription, nameField);
        buttons.getChildren().addAll(cancelButton, acceptButton);
        buttons.setAlignment(Pos.CENTER);
        nameField.setStyle("-fx-background-color: #202020; -fx-text-fill: white;");
        popupVertStructure.getChildren().addAll(textField, buttons);
        popup.getChildren().add(popupVertStructure);
        newProfName.getContent().add(popup);

        cancelButton.setOnAction(e -> newProfName.hide());
        acceptButton.setOnAction(e -> {
            profile.put("profileName", nameField.getText());
            profileSelect.getItems().add((String) nameField.getText());
            profileSelect.setValue((String) nameField.getText());
            try {
                profile.put("highscore", 0);
                if (Backend.writeProfiles(profile, nameField.getText())){
                    System.out.println("Successfully added new Profile");
                } else {
                    System.out.println("Couldn't add the new Profile");
                }
            } catch (InterruptedException | IOException err) {
                err.printStackTrace();
            }
            newProfName.hide();
        });
                
        deleteProfile = new Button("",binIconView);
        deleteProfile.setMaxHeight(20);
        
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
        
        if (profile.get("volume") != null) {
            volumeInit = (double) profile.get("volume");
        } else {
            volumeInit = 20;
            profile.put("volume", 20);
        }

        // create Volume slider
        volumeSlider = new Slider(0, 100, (int) volumeInit);
        volumeSlider.setShowTickLabels(false);
        volumeSlider.setShowTickMarks(false);
        volumeSlider.setMajorTickUnit(25);
        volumeSlider.setMinorTickCount(4);
        volumeSlider.setBlockIncrement(10);

        volumeSlider.styleProperty().bind(
            Bindings.createStringBinding(() -> {
                double percentage = (volumeSlider.getValue() - volumeSlider.getMin()) / 
                                    (volumeSlider.getMax() - volumeSlider.getMin()) * 100;
                return String.format(Locale.ENGLISH,
                    "-fx-background-color: linear-gradient(to right, #727272 %.2f%%, #b3b3b3 %.2f%%); " +
                    "-fx-background-radius: 2px; -fx-pref-height: 2px;",
                    percentage, percentage
                );
            }, volumeSlider.valueProperty())
        );


        //Object creation for Media settings
        volume = new Label((String) language.get("volume") + ": " + (int) volumeSlider.getValue());
        volume.setStyle(settingFontSize + "; " + textColor);
        backgrounds = new Label((String) language.get("backgrounds"));
        backgrounds.setStyle(settingFontSize + "; " + textColor);
        backgMusic = new Label((String) language.get("backgMusic"));
        backgMusic.setStyle(settingFontSize + "; " + textColor);
        
        //add an eventlistener to the profile selector
        profileSelect.setOnAction(e -> {
            try {
                if (profileSelect.getValue().toString().equals((String) language.get("addProfile"))) {
                    if (!newProfName.isShowing()) {
                        newProfName.show(primaryStage);

                    } else {
                        System.out.println("Popup is already shown... Please close the old one first");
                    }
                } else {
                    profileSelect.getItems().remove((String) language.get("addProfile"));
                    profile = Backend.readJSON("profile", profileSelect.getValue().toString(), null);
                    language = Backend.readJSON("lang", (String) profile.get("lang"), null);
                    config.put("profile", (String)profile.get("profileName"));
                    config.put("lang", (String) profile.get("lang"));
                    volumeInit = (double) profile.get("volume");
                    volumeSlider.setValue((int) volumeInit);
                    showGhostTetromino.setSelected((boolean) profile.get("showGhost"));
                    //load keybinds 
                    dropButton.setText((String) profile.get("drop"));
                    mvLButton.setText((String) profile.get("mvLeft"));
                    mvRButton.setText((String) profile.get("mvRight"));
                    mvDButton.setText((String) profile.get("mvDown"));
                    rotateButton.setText((String) profile.get("rotate"));
                    musicChoiceBox.setValue((String) profile.get("backgroundMusic"));
                    backgroundChoice.setValue((String) profile.get("backgroundWallpaper"));
                    //update languages
                    setLanguage(primaryStage);
                }
                if (Backend.writeConfig(config)){
                    System.out.println("config updated");
                }
            } catch (Exception err) {
                err.printStackTrace();
            }

        });

        //create options for media settings
        backgroundChoice = new ChoiceBox<>();
        backgroundChoice.getItems().addAll("background1", "background2", "background3", "background4", "background5");
        backgroundChoice.setValue((String) profile.get("backgroundWallpaper"));
        musicChoiceBox = new ChoiceBox<>();
        musicChoiceBox.getItems().addAll("Tetris_TypeA", "Tetris_TypeB", "Tetris_TypeC", "Tetris_TypeD");
        musicChoiceBox.setValue((String) profile.get("backgroundMusic"));
        
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
        column1.setMinWidth(50);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(40);
        column2.setMinWidth(50);


        // configure the volume slider
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                volume.setText(language.get("volume") + ": "+ newValue.intValue());
            }
        });

        deleteProfile.setOnAction(e -> {
            String profileName = profileSelect.getValue();
            if (!profileSelect.getValue().equals("default") || !profileSelect.getValue().equals((String) language.get("addProfile"))){
                if (!Backend.deleteProfiles(profileSelect.getValue())) {
                    System.out.println("Couldn't delete the profile");
                } else {
                    System.out.println("Deleted profile successfully");
                    profileSelect.getItems().remove(profileName);
                }
            }
        });
        //add classes to some buttons
        saveButton.getStyleClass().addAll("button-style", "settings-button");
        backButton.getStyleClass().addAll("button-style", "settings-button");


        //Create Layout
        GridPane settingsList = new GridPane();
        GridPane generalSettingsItems = new GridPane();
        VBox settings = new VBox(20);
        HBox profileBox = new HBox(30);
        HBox saveAndBack = new HBox(20);
        VBox settingsCategories = new VBox();

        settingsList.getColumnConstraints().addAll(column1, column2);
        generalSettingsItems.getColumnConstraints().addAll(column1, column2);

        settingsCategories.getChildren().addAll(generalSettingsItems, settingsList);

        generalSettingsItems.setHgap(20);
        generalSettingsItems.setVgap(20);
        generalSettingsItems.setPadding(new Insets(20, 20, 20 ,20));
        generalSettingsItems.setAlignment(Pos.CENTER);


        //Define the structure of the GridPane
        settingsList.setHgap(20);
        settingsList.setVgap(20);
        settingsList.setPadding(new Insets(20, 20, 20 ,20));
        settingsList.setAlignment(Pos.CENTER);

        //add Items
        generalSettingsItems.add(generalSettings, 0, 0);
        generalSettingsItems.add(showGhost, 0, 1);
        generalSettingsItems.add(showGhostTetromino, 1, 1);
        generalSettingsItems.add(languages, 0, 2);
        generalSettingsItems.add(languageChoiceBox, 1, 2);

        settingsList.add(mediaSettings, 0, 0);
        settingsList.add(volume, 0, 1);
        settingsList.add(volumeSlider, 1, 1);
        settingsList.add(backgrounds, 0, 2);
        settingsList.add(backgroundChoice, 1, 2);
        settingsList.add(backgMusic, 0, 3);
        settingsList.add(musicChoiceBox, 1, 3);
        settingsList.add(keybindSettings, 0, 4);
        settingsList.add(movLeft, 0, 5);
        settingsList.add(mvLButton, 1, 5);
        settingsList.add(movRight, 0, 6);
        settingsList.add(mvRButton, 1, 6);
        settingsList.add(movDown, 0, 7);
        settingsList.add(mvDButton, 1, 7);
        settingsList.add(dropDown, 0, 8);
        settingsList.add(dropButton, 1, 8);
        settingsList.add(rotate, 0, 9);
        settingsList.add(rotateButton, 1, 9);
                
        scrollSettingsMenu = new ScrollPane();
        scrollSettingsMenu.setContent(settingsCategories);
        scrollSettingsMenu.setStyle("-fx-background-color: #00000060; -fx-background: #00000060");
        scrollSettingsMenu.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollSettingsMenu.setFitToWidth(true);
 
        saveAndBack.getChildren().addAll(backButton, saveButton);
        saveAndBack.setAlignment(Pos.CENTER); 
        //Add everything together
        //settingsMenu.setContent(settingsList);
        profileBox.getChildren().addAll(profileLabel, profileSelect, deleteProfile);
        settings.getChildren().addAll(header, profileBox, scrollSettingsMenu, saveAndBack);
        settings.setAlignment(Pos.CENTER);
        settingsPadding = new Insets(40, 0, 20, 0);
        settings.setPadding(settingsPadding);
        
        //settingsList.setStyle("-fx-background-color: #00000060; -fx-background: #00000060"); 
        settings.setStyle("-fx-background-color: #00000050; -fx-background: #00000050");

        backgroundImage.toBack();

       
        //add the support for backgrounds
        StackPane layout = new StackPane(backgroundImage, settings);
        layout.setMinWidth(Region.USE_PREF_SIZE);

        scene = new Scene(layout);
        
        scrollSettingsMenu.prefWidthProperty().bind(scene.widthProperty());
        settingsList.prefWidthProperty().bind(scene.widthProperty());
        settings.prefHeightProperty().bind(scene.heightProperty());
        
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());


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
        
        saveButton.setOnAction(e -> {
            profile.put("mvLeft", mvLButton.getText());
            profile.put("mvRight", mvRButton.getText());
            profile.put("mvDown", mvDButton.getText());
            profile.put("drop", dropButton.getText());
            profile.put("rotate", rotateButton.getText());
            profile.put("lang", languageChoiceBox.getValue());
            profile.put("volume", volumeSlider.getValue());
            profile.put("backgroundMusic", musicChoiceBox.getValue());
            profile.put("backgroundWallpaper", backgroundChoice.getValue());
            profile.put("showGhost", showGhostTetromino.isSelected());
            try {
                Backend.writeProfiles(profile, profileSelect.getValue());
            } catch (InterruptedException | IOException err){
                err.printStackTrace();
            }
    });

        
    }

    //add an action to every keybind setting
    public void changeKeybind(Button button, Scene scene){
        System.out.println(button.getText());
        switch(button.getId()){
            case "drop":
                if (!keyInputLocked){
                keyInputLocked = true;
                button.setText("> - <");
                cButtonId = "drop";
                scene.addEventFilter(KeyEvent.KEY_PRESSED, this::setBind);
                keyInputLocked = false;
                }
                break;
            case "mvRight":
                if (!keyInputLocked){
                keyInputLocked = true;
                button.setText("> - <");
                cButtonId = "mvRight";
                scene.addEventFilter(KeyEvent.KEY_PRESSED, this::setBind);
                keyInputLocked = false;
                }
                break;
            case "mvLeft":
                if (!keyInputLocked){
                keyInputLocked = true;
                button.setText("> - <");
                cButtonId = "mvLeft";
                scene.addEventFilter(KeyEvent.KEY_PRESSED, this::setBind);
                keyInputLocked = false;
                }
                break;
            case "mvDown":
                if (!keyInputLocked){
                keyInputLocked = true;
                button.setText("> - <");
                cButtonId = "mvDown";
                scene.addEventFilter(KeyEvent.KEY_PRESSED, this::setBind);
                keyInputLocked = false;
                }
                break;
            case "rotate":
                if (!keyInputLocked){
                keyInputLocked = true;
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
    private void setLanguage(Stage primaryStage){
        header.setText((String) language.get("options"));
        keybindSettings.setText((String) language.get("keybinds"));
        mediaSettings.setText((String) language.get("mediaSettings"));
        dropDown.setText((String) language.get("dropButton"));
        movDown.setText((String) language.get("mvDown"));
        movLeft.setText((String) language.get("mvLeft"));
        movRight.setText((String) language.get("mvRight"));
        rotate.setText((String) language.get("rotate"));
        volume.setText((String) language.get("volume") + ": " + (int) volumeSlider.getValue());
        backgrounds.setText((String) language.get("backgrounds"));
        backgMusic.setText((String) language.get("backgMusic"));
        backButton.setText((String) language.get("startpage"));
        languageChoiceBox.setValue((String) profile.get("lang"));
        languages.setText((String) language.get("languages"));
        saveButton.setText((String) language.get("save"));
        primaryStage.setTitle((String) language.get("options"));
        profileSelect.getItems().add((String) language.get("addProfile"));
        acceptButton.setText((String) language.get("accept"));
        cancelButton.setText((String) language.get("cancel"));
        popupDescription.setText((String) language.get("enterName"));
        showGhost.setText((String) language.get("showGhost"));
        generalSettings.setText((String) language.get("general"));
        profileLabel.setText((String) language.get("profile"));
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
    private void loadProfileSelector(){
        //get a String array with all installed profiles
        String[] profilesList = Backend.list("profile");

        for (String value : profilesList){
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
        profileSelect.getItems().add((String) language.get("addProfile"));
        profileSelect.setValue((String)config.get("profile"));
    }
    
}
