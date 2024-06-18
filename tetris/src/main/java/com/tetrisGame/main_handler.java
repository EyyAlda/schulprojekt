package com.tetrisGame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;


public class main_handler extends Application {

    String[][] grid = new String[20][10];
    String[] valid_tetrominos = {
        "I", "O", "T", "S", "Z", "L", "J"
    };

    public void output() {
        
        System.out.print("  _");
        for (int i = 0; i < grid.length; i++) {
            System.out.print("_");
        }
        System.out.println("  ");

        for (int y = 0; y < grid.length; y++) {
            System.out.print("<! ");
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x].equals("T1") || grid[y][x].equals("T2") || grid[y][x].equals("T3") || grid[y][x].equals("T4")) {

                } else {
                    System.out.print(". ");
                }
            }
            System.out.println("!>");
        }

        System.out.print("  ¯");
        for (int i = 0; i < grid.length; i++) {
            System.out.print("¯");
        }
        System.out.println("¯");
    }

    public void update() {
        output();
    }

    public static void main(String[] arg) {
        launch(arg);
    }

    private HashMap<String, Boolean> currentlyActiveKeys = new HashMap<>();

    private boolean removeActiveKey(String codeString) {
        Boolean isActive = currentlyActiveKeys.get(codeString);

        if (isActive != null && isActive) {
            currentlyActiveKeys.put(codeString, false);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void start(Stage primaryStage) {

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
               grid[y][x] = ". ";
            }
        }

        BorderPane pane = new BorderPane();

        Scene scene = new Scene(pane, 100, 100);

        primaryStage.setTitle("BorderPane");
        primaryStage.setScene(scene);

        output();

        scene.setOnKeyPressed(event -> {
            String codeString = event.getCode().toString();
            if (!currentlyActiveKeys.containsKey(codeString)) {
                currentlyActiveKeys.put(codeString, true);
            }
        });
        scene.setOnKeyReleased(event -> 
            currentlyActiveKeys.remove(event.getCode().toString())
        );

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (removeActiveKey("LEFT")) {
                    update();
                }

                if (removeActiveKey("RIGHT")) {
                    System.out.println("right");
                }

                if (removeActiveKey("UP")) {
                    System.out.println("up");
                }

                if (removeActiveKey("DOWN")) {
                    System.out.println("down");
                }
            }
        }.start();

        primaryStage.show();
    }   
}