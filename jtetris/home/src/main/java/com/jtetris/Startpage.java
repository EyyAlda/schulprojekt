package com.jtetris;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.HashMap;

import java.io.File;
import java.io.IOException;

public class Startpage extends Application implements EventHandler<ActionEvent> {

    HashMap<String, Object> config = null;

    HashMap<String, Object> lang = null;

    // Scene labels
    Label tetris = new Label("Tetris");

    Stage startwindow;
    Scene startpagescene, optionsscene;
    Button game, Aboutus, Options, Startpageback, quitButton;
    Image optionspic, tetrispic, aboutuspic;

    private Stage primaryStage;

    Label keybindslabel, musicLabel, backroundLabel;

    public static void main(String[] args) {
        // Executes the Application setup and then starts the public void start.
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        showMainMenu();
    }

    public void change_language(Button game, Button quitButton, Button aboutus, Button options) {
        game.setText((String) lang.get("start"));
        quitButton.setText((String) lang.get("quit"));
        aboutus.setText((String) lang.get("about"));
        options.setText((String) lang.get("options"));
    }

   
    public void showMainMenu() throws Exception {

        config = Backend.readConfig(false, null);
        lang = Backend.readJSON("lang", (String)config.get("lang"), null);
        // Creates the stage for the Startpage
        primaryStage.setTitle("Startpage");
        startwindow = primaryStage;

        // Game start button
        // setOnAction calls for action on button click
        game = new Button();
        game.setText((String)lang.get("start"));
        game.setOnAction(this);

        //show language selector
        ChoiceBox<String> langChoiceBox = new ChoiceBox<>();
        langChoiceBox.getItems().addAll("en", "de", "es", "nl", "rs", "ru", "it", "pl");
        langChoiceBox.setValue((String)config.get("lang"));
        langChoiceBox.setOnAction(e -> {
            String selected = langChoiceBox.getValue();
            config.put("lang", selected);
            try {
                lang = Backend.readJSON("lang", selected, null);
                Backend.writeConfig(config);
                change_language(game, quitButton, Aboutus, Options);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        VBox chBox = new VBox(langChoiceBox);


        //QuitButton
        quitButton = new Button((String)lang.get("quit"));
        quitButton.applyCss();
        quitButton.setOnAction(e -> System.exit(0));

        // Trivia about us page.
        Aboutus = new Button((String)lang.get("about"));
        Aboutus.setOnAction(this);
        Aboutus.setStyle("-fx-background-color: #202020d8; -fx-text-fill: #ffff; -fx-pref-width: 70px; -fx-pref-height: 20px; -fx-font-size: 10px");

        // Options button
        Options = new Button((String)lang.get("options"));
        Options.setOnAction(this);

        Startpageback = new Button((String)lang.get("startpage"));
        Startpageback.setOnAction(this);

        // Options Future addons
        keybindslabel = new Label("We are planning on adding interchangeable keybinds for ease of play in future updates.");
        musicLabel = new Label("We are planning to add a music on and off button and changing the music into the options instead of it being a keybind in future updates. Later down the line even a volume slider.");
        backroundLabel = new Label("We are planning to make it so the backgrounds can be changed in the options menu.");

        // Gets screen data
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        Image background = new Image(new File(Backend.getXdgUserDir("DOCUMENTS")+ "/myGames/Jtetris/textures/background.png").toURI().toString());
        ImageView imageView = new ImageView();
        imageView.setImage(background);
        imageView.setFitHeight(1080);
        imageView.setFitWidth(1920);

        // Maximizes the window
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        // Creates borderpane layout for the start page
        BorderPane Startpagelayout = new BorderPane();

        // Adds the background image view
        Startpagelayout.getChildren().add(imageView);
        imageView.toBack();

        // Centers the start button
        VBox centerPane = new VBox(10);
        game.setStyle("-fx-background-color: #202020d8; -fx-text-fill: #ffff; -fx-pref-width: 500px; -fx-pref-height: 100px; -fx-font-size: 40px");
        quitButton.setStyle("-fx-background-color: #202020d8; -fx-text-fill: #ffff; -fx-pref-width: 500px; -fx-pref-height: 100px; -fx-font-size: 40px");
        Options.setStyle("-fx-background-color: #202020d8; -fx-text-fill: #ffff; -fx-pref-width: 500px; -fx-pref-height: 100px; -fx-font-size: 40px");
        centerPane.getChildren().addAll(game, Options, quitButton);
        centerPane.setLayoutX(950);
        centerPane.setLayoutY(550);
        centerPane.setAlignment(Pos.CENTER);

        // About us layout
        VBox bottomBox = new VBox(20);
        bottomBox.getChildren().addAll(Aboutus);
        bottomBox.setLayoutX(1220);
        bottomBox.setLayoutY(250);
        bottomBox.setAlignment(Pos.BOTTOM_RIGHT);

        // Options button
        VBox topBox = new VBox(20);
        topBox.getChildren().addAll(chBox);
        topBox.setLayoutX(1220);
        topBox.setLayoutY(250);
        topBox.setAlignment(Pos.TOP_RIGHT);
        
        

        // Options layout, more can be added
        VBox optionsBox = new VBox(20);
        optionsBox.getChildren().addAll(keybindslabel, musicLabel, backroundLabel, Startpageback);
        optionsBox.setAlignment(Pos.CENTER);

        // Options window
        BorderPane optionslayout = new BorderPane();
        optionslayout.setCenter(optionsBox);

        // Adds elements to start page layout
        Startpagelayout.setCenter(centerPane);
        Startpagelayout.setBottom(bottomBox);
        Startpagelayout.setTop(topBox);

        // Moves the image view to the back
        imageView.toBack();

        // Creates the scene and gives it the parameters
        startpagescene = new Scene(Startpagelayout, 1920, 1080);
        optionsscene = new Scene(optionslayout, 1920, 1080);

        // Delegates startpagescene as the scene that is shown in primaryStage
        primaryStage.setScene(startpagescene);
        primaryStage.show();
    }

    @Override
    // Handles what happens on action, in this case button click
    public void handle(ActionEvent event) {
        // getSource checks which source to give the correct output
        if (event.getSource() == game) {
            // Start game
          main_handler main_handler = new main_handler();
            main_handler.setStartpage(this); // Pass reference to startpage
            try {
                main_handler.start(startwindow);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (event.getSource() == Aboutus) {
            try {
                showAboutUs(primaryStage);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (event.getSource() == Options) {
            Settings settings = new Settings();
            settings.start(primaryStage);
            startwindow.setTitle((String)lang.get("options"));
        } else if (event.getSource() == Startpageback) {
            startwindow.setScene(startpagescene);
            startwindow.setTitle((String)lang.get("startpage"));
        }

        
    }
    public void showAboutUs(Stage stage) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Startpage.class.getResource("wew.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1096, 869);
        aboutPageController aboutPageController = fxmlLoader.getController();
        aboutPageController.setMainApp(this);
        stage.setTitle((String)lang.get("about"));
        stage.setScene(scene);
        stage.show();  
    }

    public Scene getStartPageScene() {
        return startpagescene;
    }
}