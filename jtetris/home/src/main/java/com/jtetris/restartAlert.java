package com.jtetris;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

public class restartAlert {

    public static void Alert(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Info");
        window.setWidth(1000);
        window.setHeight(500);
        
        Label text = new Label();
        text.setText("You have to restart the game for the changes to take effect!");

        Button closeButton = new Button("Restart later");
        closeButton.setOnAction(new EventHandler<ActionEvent>(){
        public void handle(ActionEvent event) {
            window.close();
            }
        });

        Button now = new Button("Restart now");
        now.setOnAction(e -> System.exit(0));

        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(closeButton, now);

        VBox screen = new VBox(20);
        screen.getChildren().addAll(text, buttons);

        Scene scene = new Scene(screen);
        window.setScene(scene);
        window.show();


    }
    
}
