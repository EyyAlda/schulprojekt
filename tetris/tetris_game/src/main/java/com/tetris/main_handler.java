package com.tetris.tetris_game;



import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

import java.lang.Math;
import java.util.*;


public class main_handler extends Application {

    String[][] grid = new String[20][10];
    String[] valid_tetrominos = {
        "O","I", "T", "S", "Z", "L", "J"
    };
    String tetromino = "O";

    int X1, X2, X3, X4, Y1, Y2, Y3, Y4 = 0;


    // Function that returns a grid in the output
    public void output() {
        
        System.out.print("  _");
        for (int i = 0; i < grid.length; i++) {
            System.out.print("_");
        }
        System.out.println("  ");

        for (int y = 0; y < grid.length; y++) {
            System.out.print("<!");
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x].equals("T1") || grid[y][x].equals("T2") || grid[y][x].equals("T3") || grid[y][x].equals("T4") || grid[y][x].equals("P")) {
                    System.out.print("[]");
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
        System.out.println(" (" + tetromino + ")");
    }
    
    // Creates a tetromino at positions X = 3 - 5 and Y = 0 - 4
    public void create_tetromino() {
        tetromino = valid_tetrominos[(int) (valid_tetrominos.length * Math.random())];

        clear_grid();

        if (tetromino.equals("O")) {
            X1 = 3; Y1 = 0;
            X2 = 4; Y2 = 0;
            X3 = 3; Y3 = 1;
            X4 = 4; Y4 = 1;
        } else if (tetromino.equals("L")) {
            X1 = 3; Y1 = 0;
            X2 = 3; Y2 = 1;
            X3 = 3; Y3 = 2;
            X4 = 4; Y4 = 2;
        } else if (tetromino.equals("T")) {
            X1 = 4; Y1 = 0;
            X2 = 4; Y2 = 1;
            X3 = 3; Y3 = 1;
            X4 = 5; Y4 = 1;
        } else if (tetromino.equals("J")) {
            X1 = 4; Y1 = 0;
            X2 = 4; Y2 = 1;
            X3 = 4; Y3 = 2;
            X4 = 3; Y4 = 2;
        } else if (tetromino.equals("Z")) {
            X1 = 4; Y1 = 0;
            X2 = 4; Y2 = 1;
            X3 = 3; Y3 = 1;
            X4 = 3; Y4 = 2;
        } else if (tetromino.equals("S")) {
            X1 = 3; Y1 = 0;
            X2 = 3; Y2 = 1;
            X3 = 4; Y3 = 1;
            X4 = 4; Y4 = 2;
        } else if (tetromino.equals("I")) {
            X1 = 3; Y1 = 0;
            X2 = 3; Y2 = 1;
            X3 = 3; Y3 = 2;
            X4 = 3; Y4 = 3;
        }
    }

    // Clears the grid
    public void clear_grid() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (!grid[y][x].equals("P")) {
                    grid[y][x] = ". ";
                }
            }
        }
    }

    // Updates the tetromino position
    public void set_tetromino_coords() {
        clear_grid();
        grid[Y1][X1] = "T1";
        grid[Y2][X2] = "T2";
        grid[Y3][X3] = "T3";
        grid[Y4][X4] = "T4";

    }

    // Converts the moving "T" values to still "P" values
    public void convert() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x].equals("T1") || grid[y][x].equals("T2") || grid[y][x].equals("T3") || grid[y][x].equals("T4")) {
                    grid[y][x] = "P";
                }
            }
        }
    }

    // Lowers the Y values of the 4 tetromino tiles
    public void lower_tetromino() {

        if (Y1 < grid.length - 1 && Y2 < grid.length - 1 && Y3 < grid.length - 1 && Y4 < grid.length - 1) {

            boolean can_go_down = true;

            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[y].length; x++) {
                    if (grid[y][x].equals("T1") || grid[y][x].equals("T2") || grid[y][x].equals("T3") || grid[y][x].equals("T4")) {
                        if (grid[y + 1][x].equals("P")) {

                            convert();
                            can_go_down = false;
                            create_tetromino();
                            break;
                        }
                    }
                }
            }

            if (can_go_down) {
                Y1+=1; Y2+=1; Y3+=1; Y4+=1;
            }
        } else {
           convert();
           create_tetromino();
        }
        set_tetromino_coords();
        output();
    }

    // Moves the tetromino left and right
    public void move_tetromino(String direction) {
        if (direction.equalsIgnoreCase("RIGHT")) {
            if (X1 < grid[0].length - 1 && X2 < grid[0].length - 1 && X3 < grid[0].length - 1 && X4 < grid[0].length - 1) {

                boolean can_go_down = true;

                for (int y = 0; y < grid.length; y++) {
                    for (int x = 0; x < grid[y].length; x++) {
                        if (grid[y][x].equals("T1") || grid[y][x].equals("T2") || grid[y][x].equals("T3") || grid[y][x].equals("T4")) {
                            if (grid[y][x + 1].equals("P")) {
    
                                convert();
                                can_go_down = false;
                                create_tetromino();
                                break;
                            }
                        }
                    }
                }

                if (can_go_down) {
                    X1++; X2++; X3++; X4++;
                }
            }
        }

        if (direction.equalsIgnoreCase("LEFT")) {
            if (X1 > 0 && X2 > 0 && X3 > 0 && X4 > 0) {

                boolean can_go_down = true;

                for (int y = 0; y < grid.length; y++) {
                    for (int x = 0; x < grid[y].length; x++) {
                        if (grid[y][x].equals("T1") || grid[y][x].equals("T2") || grid[y][x].equals("T3") || grid[y][x].equals("T4")) {
                            if (grid[y][x - 1].equals("P")) {
    
                                convert();
                                can_go_down = false;
                                create_tetromino();
                                break;
                            }
                        }
                    }
                }

                if (can_go_down) {
                    X1++; X2++; X3++; X4++;
                }

                X1--; X2--; X3--; X4--;
            }
        }

        set_tetromino_coords();
        output();
    }

    // Drops the tetromino
    public void drop() {
        
    }

    // Prepares the game when loaded
    public void prepare() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                grid[y][x] = ". ";
            }
        }
        create_tetromino();
        set_tetromino_coords();
        output();

    }

    // Updater
    public void update() {
        lower_tetromino();
        set_tetromino_coords();
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

        prepare();

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
                    move_tetromino("LEFT");
                }

                if (removeActiveKey("RIGHT")) {
                    move_tetromino("RIGHT");
                }

                if (removeActiveKey("UP")) {
                    drop();
                }

                if (removeActiveKey("DOWN")) {
                    lower_tetromino();
                }
            }
        }.start();

        primaryStage.show();
    }   
}