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

public class Pointsandlines{

    public static void display(String title, String message,int points,int lines_cleared){
    Stage window = new Stage();

    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle(title);
    window.setWidth(250);

    Label text = new Label();
    text.setText(message);
    Label pointsLabel = new Label();
    pointsLabel.setText("Points: " + points);
    Label linesclearedLabel = new Label();
    linesclearedLabel.setText("Lines cleared: "+ lines_cleared);

    Button closeButton = new Button("Close");
    closeButton.setOnAction(new EventHandler<ActionEvent>(){
        public void handle(ActionEvent event) {
            window.close();

        }

    });


    VBox layout = new VBox(10);
    layout.getChildren().addAll(text,pointsLabel,linesclearedLabel,closeButton);
    layout.setAlignment(Pos.CENTER);
    
    Scene scene = new Scene(layout);
    window.setScene(scene);
    window.show();


    
    }

}