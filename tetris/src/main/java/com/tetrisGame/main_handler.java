package com.tetrisGame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class main_handler extends Application {

    int[][] grid = new int[20][10];

    public void output() {
        System.out.print("  _");
        for (int i = 0; i < grid.length; i++) {
            System.out.print("_");
        }
        System.out.println("  ");

        for (int i = 0; i < grid.length; i++) {
            System.out.print("<! ");
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(". ");
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

    @Override
    public void start(Stage primaryStage) {

        BorderPane pane = new BorderPane();

        Scene scene = new Scene(pane, 800, 300);

        primaryStage.setTitle("BorderPane");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}