package com.tetris;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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

    private static Stage stage;

    @Override
    public void start(@SuppressWarnings("exports") Stage s) throws IOException {
        stage=s;
        setRoot("primary","");
    }

    static void setRoot(String fxml) throws IOException {
        setRoot(fxml,stage.getTitle());
    }

    static void setRoot(String fxml, String title) throws IOException {
        Scene scene = new Scene(loadFXML(fxml));
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/"+fxml + ".fxml"));
        return fxmlLoader.load();
    }


    public static void main(String[] args) {
        launch(args);
    }
}