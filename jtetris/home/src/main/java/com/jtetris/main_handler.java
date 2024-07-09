package com.jtetris;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.io.File;
import java.lang.Math;
import java.util.*;

public class main_handler extends Application {

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    Scanner in = new Scanner(System.in);

    String[][] grid = new String[20][10];
    String[] valid_tetrominos = {
        "O","I", "T", "S", "Z", "L", "J"
    };

    HashMap<String, Object> config = null;
    HashMap<String, Object> lang = null;

    private Startpage startpage;
   
    int current_background = 1;

    Label paused_label = new Label();
    Button quit_button = new Button();
    Label points_earnt_game_over = new Label();
    Label lines_cleared_game_over = new Label();

    // Textures
    Image background = new Image(new File(Backend.getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/textures/background"+current_background+".gif").toURI().toString());
    Image s_texture = new Image(new File(Backend.getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/textures/s.png").toURI().toString());
    Image t_texture = new Image(new File(Backend.getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/textures/t.png").toURI().toString());
    Image i_texture = new Image(new File(Backend.getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/textures/i.png").toURI().toString());
    Image l_texture = new Image(new File(Backend.getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/textures/l.png").toURI().toString());
    Image z_texture = new Image(new File(Backend.getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/textures/z.png").toURI().toString());
    Image o_texture = new Image(new File(Backend.getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/textures/o.png").toURI().toString());
    Image j_texture = new Image(new File(Backend.getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/textures/j.png").toURI().toString());

    Image j_sussy = new Image(new File(Backend.getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/textures/j_sussy.png").toURI().toString());

    Image ghost_texture = new Image(new File(Backend.getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/textures/ghost.png").toURI().toString());
    Image null_texture = new Image(new File(Backend.getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/textures/null.png").toURI().toString());

    // Audio
    MediaPlayer main_theme = new MediaPlayer(new Media(new File(Backend.getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/audio/Tetris_TypeA.wav").toURI().toString()));
    MediaPlayer horosho = new MediaPlayer(new Media(new File(Backend.getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/audio/horosho_louder.wav").toURI().toString()));
    MediaPlayer sussy = new MediaPlayer(new Media(new File(Backend.getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/audio/sussy_baka.wav").toURI().toString()));
    MediaPlayer line_clear = new MediaPlayer(new Media(new File(Backend.getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/audio/line_clear.wav").toURI().toString()));
    MediaPlayer line_clear_4 = new MediaPlayer(new Media(new File(Backend.getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/audio/line_clear_4.wav").toURI().toString()));
    MediaPlayer move = new MediaPlayer(new Media(new File(Backend.getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/audio/move.wav").toURI().toString()));
    
    // UI Elements
    private static final int CELL_SIZE = 48; // 50 looks the best
    private StackPane[][] cells = new StackPane[grid.length][grid[0].length];
    private StackPane[][] nextTetrominoCells = new StackPane[4][4];

    String tetromino = "O";
    String next_tetromino = valid_tetrominos[(int) (valid_tetrominos.length * Math.random())];;
    boolean holding_down = false;
    boolean game_paused = false;
    boolean game_in_progress = false;
    boolean drop_pressed = false;
    boolean deleting = false;
    boolean show_ghost_tetrominos = true;
    boolean game_over_row_deleted = false;
    int rotation = 1;
    int points = 0;
    int lines_cleared = 0;
    int language = 0;

    VBox game_over_screen = new VBox();
    Label points_label = new Label();
    Label lines_cleared_label = new Label();

    int X1, X2, X3, X4, Y1, Y2, Y3, Y4 = 0;
    
    // Function that returns a grid in the output
    public void output() {
        
        for (int y = 0; y < cells.length; y++) {
            for (int x = 0; x < cells[y].length; x++) {
                if (grid[y][x].equals("T1") || grid[y][x].equals("T2") || grid[y][x].equals("T3") || grid[y][x].equals("T4")) {
                    if (tetromino.equalsIgnoreCase("I")) {
                        set_texture_of_cell(y, x, i_texture, cells);
                    } else if (tetromino.equalsIgnoreCase("T")) {
                        set_texture_of_cell(y, x, t_texture, cells);
                    } else if (tetromino.equalsIgnoreCase("L")) {
                        set_texture_of_cell(y, x, l_texture, cells);
                    } else if (tetromino.equalsIgnoreCase("J")) {
                        set_texture_of_cell(y, x, j_texture, cells);
                    } else if (tetromino.equalsIgnoreCase("JA")) {
                        set_texture_of_cell(y, x, j_sussy, cells);
                    } else if (tetromino.equalsIgnoreCase("S")) {
                        set_texture_of_cell(y, x, s_texture, cells);
                    } else if (tetromino.equalsIgnoreCase("Z")) {
                        set_texture_of_cell(y, x, z_texture, cells);
                    } else if (tetromino.equalsIgnoreCase("O")) {
                        set_texture_of_cell(y, x, o_texture, cells);
                    }
                } else if (grid[y][x].contains("P")) {
                    if (grid[y][x].equalsIgnoreCase("PI")) {
                        set_texture_of_cell(y, x, i_texture, cells);
                    } else if (grid[y][x].equalsIgnoreCase("PT")) {
                        set_texture_of_cell(y, x, t_texture, cells);
                    } else if (grid[y][x].equalsIgnoreCase("PL")) {
                        set_texture_of_cell(y, x, l_texture, cells);
                    } else if (grid[y][x].equalsIgnoreCase("PJA")) {
                        set_texture_of_cell(y, x, j_sussy, cells);
                    } else if (grid[y][x].equalsIgnoreCase("PJ")) {
                        set_texture_of_cell(y, x, j_texture, cells);
                    } else if (grid[y][x].equalsIgnoreCase("PS")) {
                        set_texture_of_cell(y, x, s_texture, cells);
                    } else if (grid[y][x].equalsIgnoreCase("PZ")) {
                        set_texture_of_cell(y, x, z_texture, cells);
                    } else if (grid[y][x].equalsIgnoreCase("PO")) {
                        set_texture_of_cell(y, x, o_texture, cells);
                    }
                } else if (grid[y][x].equalsIgnoreCase("G")) {
                    set_texture_of_cell(y, x, ghost_texture, cells);
                } else {
                    set_texture_of_cell(y, x, null_texture, cells);
                }
            }
        }

        points_label.setText((String)lang.get("points")+": " + points);
        lines_cleared_label.setText((String)lang.get("linesCleared")+": " + lines_cleared);
    }
    
    // Creates a tetromino at positions X = 3 - 5 and Y = 0 - 4
    public void create_tetromino(Stage primaryStage) {

        drop_pressed = false;
        rotation = 1;
        tetromino = next_tetromino;

        next_tetromino = valid_tetrominos[(int) (valid_tetrominos.length * Math.random())];
        next_tetromino = valid_tetrominos[(int) (valid_tetrominos.length * Math.random())];

        if (next_tetromino.equals("J")) {
            if ((int) (100 * Math.random()) < 2) {
                next_tetromino = "JA";
                sussy.stop();
                sussy.play();
            }
        }

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
        } else if (tetromino.equals("J") || tetromino.equals("JA")) {
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

        for (int y = 0; y < nextTetrominoCells.length; y++) {
            for (int x = 0; x < nextTetrominoCells[0].length; x++) {
                set_texture_of_cell(x, y, null_texture, nextTetrominoCells);
            }
        }

        if (next_tetromino.equalsIgnoreCase("I")) {
            set_texture_of_cell(0, 2, i_texture, nextTetrominoCells);
            set_texture_of_cell(1, 2, i_texture, nextTetrominoCells);
            set_texture_of_cell(2, 2, i_texture, nextTetrominoCells);
            set_texture_of_cell(3, 2, i_texture, nextTetrominoCells);
        } else if (next_tetromino.equalsIgnoreCase("T")) {
            set_texture_of_cell(1, 1, t_texture, nextTetrominoCells);
            set_texture_of_cell(2, 0, t_texture, nextTetrominoCells);
            set_texture_of_cell(2, 1, t_texture, nextTetrominoCells);
            set_texture_of_cell(2, 2, t_texture, nextTetrominoCells);
        } else if (next_tetromino.equalsIgnoreCase("L")) {
            set_texture_of_cell(0, 1, l_texture, nextTetrominoCells);
            set_texture_of_cell(1, 1, l_texture, nextTetrominoCells);
            set_texture_of_cell(2, 1, l_texture, nextTetrominoCells);
            set_texture_of_cell(2, 2, l_texture, nextTetrominoCells);
        } else if (next_tetromino.equalsIgnoreCase("J")) {
            set_texture_of_cell(0, 2, j_texture, nextTetrominoCells);
            set_texture_of_cell(1, 2, j_texture, nextTetrominoCells);
            set_texture_of_cell(2, 2, j_texture, nextTetrominoCells);
            set_texture_of_cell(2, 1, j_texture, nextTetrominoCells);
        } else if (next_tetromino.equalsIgnoreCase("JA")) {
            set_texture_of_cell(0, 2, j_sussy, nextTetrominoCells);
            set_texture_of_cell(1, 2, j_sussy, nextTetrominoCells);
            set_texture_of_cell(2, 2, j_sussy, nextTetrominoCells);
            set_texture_of_cell(2, 1, j_sussy, nextTetrominoCells);
        } else if (next_tetromino.equalsIgnoreCase("S")) {
            set_texture_of_cell(0, 1, s_texture, nextTetrominoCells);
            set_texture_of_cell(1, 1, s_texture, nextTetrominoCells);
            set_texture_of_cell(1, 2, s_texture, nextTetrominoCells);
            set_texture_of_cell(2, 2, s_texture, nextTetrominoCells);
        } else if (next_tetromino.equalsIgnoreCase("Z")) {
            set_texture_of_cell(0, 2, z_texture , nextTetrominoCells);
            set_texture_of_cell(1, 2, z_texture, nextTetrominoCells);
            set_texture_of_cell(1, 1, z_texture, nextTetrominoCells);
            set_texture_of_cell(2, 1, z_texture, nextTetrominoCells);
        } else if (next_tetromino.equalsIgnoreCase("O")) {
            set_texture_of_cell(1, 1, o_texture, nextTetrominoCells);
            set_texture_of_cell(1, 2, o_texture, nextTetrominoCells);
            set_texture_of_cell(2, 1, o_texture, nextTetrominoCells);
            set_texture_of_cell(2, 2, o_texture, nextTetrominoCells);
        }

        if (grid[Y1][X1].contains("P") || grid[Y2][X2].contains("P") || grid[Y3][X3].contains("P") || grid[Y4][X4].contains("P")) {

            main_theme.stop();
            scheduler.shutdown();

             // Create a new scheduler for row deletion
            scheduler = Executors.newScheduledThreadPool(1);
            deleteRowsSequentially(primaryStage);
        }
    }

    private void deleteRowsSequentially(Stage primaryStage) {
        deleteRow(0, primaryStage);
    }

    private void deleteRow(int row, Stage primaryStage) {
        game_in_progress = false;
        if (row < grid.length) {
            for (int x = 0; x < grid[0].length; x++) {
                grid[grid.length - 1 - row][x] = ". ";
            }
            output(); // Assuming this updates the display

            scheduler.schedule(() -> {
                deleteRow(row + 1, primaryStage);
            }, 100, TimeUnit.MILLISECONDS);
        } else {
            scheduler.shutdown();
            // All rows deleted, handle game over screen transition
            Platform.runLater(() -> {
                ScheduledExecutorService delay = Executors.newScheduledThreadPool(1);
                game_over_screen.setVisible(true);
                lines_cleared_game_over.setVisible(false);
                points_earnt_game_over.setText((String) lang.get("pointsAcquired") + ": " + points);
                delay.schedule(() -> {
                    lines_cleared_game_over.setText((String) lang.get("linesCleared") + ": " + lines_cleared);
                    lines_cleared_game_over.setVisible(true);
                }, 1, TimeUnit.SECONDS);

                delay.schedule(() -> {
                    paused_label.setText((String) lang.get("gameFin"));
                    paused_label.setVisible(true);
                    quit_button.setVisible(true);
                    game_over_screen.setVisible(false);
                }, 4, TimeUnit.SECONDS);
            });
        }
    }

    public void ghost_tetromino() {
        int ghostY1 = Y1, ghostY2 = Y2, ghostY3 = Y3, ghostY4 = Y4;
        int ghostX1 = X1, ghostX2 = X2, ghostX3 = X3, ghostX4 = X4;

        while (ghostY1 < grid.length - 1 && ghostY2 < grid.length - 1 && ghostY3 < grid.length - 1 && ghostY4 < grid.length - 1 && !grid[ghostY1 + 1][ghostX1].contains("P") && !grid[ghostY2 + 1][ghostX2].contains("P") && !grid[ghostY3 + 1][ghostX3].contains("P") && !grid[ghostY4 + 1][ghostX4].contains("P")) {
            ghostY1++;
            ghostY2++;
            ghostY3++;
            ghostY4++;
        }

        if (!grid[ghostY1][ghostX1].contains("T") && !grid[ghostY2][ghostX2].contains("T") && !grid[ghostY3][ghostX3].contains("T") && !grid[ghostY4][ghostX4].contains("T")) {
            grid[ghostY1][ghostX1] = "G";
            grid[ghostY2][ghostX2] = "G";
            grid[ghostY3][ghostX3] = "G";
            grid[ghostY4][ghostX4] = "G";
        }
    }

    public void rotate() {

        int NewX1 = X1, NewX3 = X3, NewX4 = X4, NewY1 = Y1, NewY3 = Y3, NewY4 = Y4;

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x].equals("T2")) {
                    if (x > 0 && x < grid[y].length - 1 && y < grid.length - 1) {

                        rotation += 1;
                        if (rotation > 4) {
                            rotation = 1;
                        }

                        for (int i = 0; i < grid.length; i++) {
                            for (int j = 0; j < grid[i].length; j++) {

                                if (tetromino.equalsIgnoreCase("L")) {
                                    if (rotation == 1) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 -= 1;
                                            NewX1 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 += 1;
                                            NewX3 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewX4 += 2;
                                        }
                                        ;
                                    } else if (rotation == 2) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 += 1;
                                            NewX1 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 -= 1;
                                            NewX3 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewY4 -= 2;
                                        }
                                        ;
                                    } else if (rotation == 3) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 += 1;
                                            NewX1 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 -= 1;
                                            NewX3 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewX4 -= 2;
                                        }
                                        ;
                                    } else if (rotation == 4) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 -= 1;
                                            NewX1 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 += 1;
                                            NewX3 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewY4 += 2;
                                        }
                                        ;
                                    }
                                } else if (tetromino.equalsIgnoreCase("J") || tetromino.equalsIgnoreCase("JA")) {
                                    if (rotation == 1) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 -= 1;
                                            NewX1 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 += 1;
                                            NewX3 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewY4 += 2;
                                        }
                                        ;
                                    } else if (rotation == 2) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 += 1;
                                            NewX1 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 -= 1;
                                            NewX3 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewX4 += 2;
                                        }
                                        ;
                                    } else if (rotation == 3) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 += 1;
                                            NewX1 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 -= 1;
                                            NewX3 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewY4 -= 2;
                                        }
                                        ;
                                    } else if (rotation == 4) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 -= 1;
                                            NewX1 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 += 1;
                                            NewX3 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewX4 -= 2;
                                        }
                                        ;
                                    }
                                } else if (tetromino.equalsIgnoreCase("S")) {
                                    if (rotation == 1) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 -= 1;
                                            NewX1 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 += 1;
                                            NewX3 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewY4 += 2;
                                        }
                                        ;
                                    } else if (rotation == 2) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 += 1;
                                            NewX1 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 -= 1;
                                            NewX3 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewY4 -= 2;
                                        }
                                        ;
                                    } else if (rotation == 3) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 -= 1;
                                            NewX1 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 += 1;
                                            NewX3 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewY4 += 2;
                                        }
                                        ;
                                    } else if (rotation == 4) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 += 1;
                                            NewX1 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 -= 1;
                                            NewX3 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewY4 -= 2;
                                        }
                                        ;
                                    }
                                } else if (tetromino.equalsIgnoreCase("Z")) {
                                    if (rotation == 1) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 -= 1;
                                            NewX1 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 -= 1;
                                            NewX3 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewX4 -= 2;
                                        }
                                        ;
                                    } else if (rotation == 2) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 += 1;
                                            NewX1 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 += 1;
                                            NewX3 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewX4 += 2;
                                        }
                                        ;
                                    } else if (rotation == 3) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 -= 1;
                                            NewX1 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 -= 1;
                                            NewX3 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewX4 -= 2;
                                        }
                                        ;
                                    } else if (rotation == 4) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 += 1;
                                            NewX1 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 += 1;
                                            NewX3 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewX4 += 2;
                                        }
                                        ;
                                    }
                                } else if (tetromino.equalsIgnoreCase("T")) {
                                    if (rotation == 1) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 -= 1;
                                            NewX1 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 += 1;
                                            NewX3 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewY4 -= 1;
                                            NewX4 += 1;
                                        }
                                        ;
                                    } else if (rotation == 2) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 += 1;
                                            NewX1 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 += 1;
                                            NewX3 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewY4 -= 1;
                                            NewX4 -= 1;
                                        }
                                        ;
                                    } else if (rotation == 3) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 += 1;
                                            NewX1 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 -= 1;
                                            NewX3 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewY4 += 1;
                                            NewX4 -= 1;
                                        }
                                        ;
                                    } else if (rotation == 4) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 -= 1;
                                            NewX1 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 -= 1;
                                            NewX3 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewY4 += 1;
                                            NewX4 += 1;
                                        }
                                        ;
                                    }
                                } else if (tetromino.equalsIgnoreCase("I")) {
                                    if (rotation == 1) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 -= 1;
                                            NewX1 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 += 1;
                                            NewX3 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewY4 += 2;
                                            NewX4 += 2;
                                        }
                                        ;
                                    } else if (rotation == 2) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 += 1;
                                            NewX1 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 -= 1;
                                            NewX3 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewY4 -= 2;
                                            NewX4 += 2;
                                        }
                                        ;
                                    } else if (rotation == 3) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 += 1;
                                            NewX1 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 -= 1;
                                            NewX3 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewY4 -= 2;
                                            NewX4 -= 2;
                                        }
                                        ;
                                    } else if (rotation == 4) {
                                        if (grid[i][j].equalsIgnoreCase("T1")) {
                                            NewY1 -= 1;
                                            NewX1 += 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T3")) {
                                            NewY3 += 1;
                                            NewX3 -= 1;
                                        } else if (grid[i][j].equalsIgnoreCase("T4")) {
                                            NewY4 += 2;
                                            NewX4 -= 2;
                                        }
                                        ;
                                    }
                                } else {
                                    return;
                                }
                            }
                        }

                        if ((NewX1 >= 0 && NewX1 < grid[y].length) && (NewX3 >= 0 && NewX3 < grid[y].length) && (NewX4 >= 0 && NewX4 < grid[y].length)) {
                            if (NewY1 < grid.length - 1 && NewY3 < grid.length - 1 && NewY4 < grid.length - 1) {
                                if (!grid[NewY1][NewX1].contains("P") && !grid[NewY3][NewX3].contains("P") && !grid[NewY4][NewX4].contains("P")) {
                                    X1 = NewX1;
                                    Y1 = NewY1;
                                    X3 = NewX3;
                                    Y3 = NewY3;
                                    X4 = NewX4;
                                    Y4 = NewY4;
                                } else {
                                    rotation -= 1;
                                    if (rotation == 0) {
                                        rotation = 4;
                                    }
                                    return;
                                }
                            } else {
                                rotation -= 1;
                                if (rotation == 0) {
                                    rotation = 4;
                                }
                                return;
                            }
                        } else {
                            rotation -= 1;
                            if (rotation == 0) {
                                rotation = 4;
                            }
                            return;
                        }
                    }
                }
            }

            set_tetromino_coords();
            ghost_tetromino();
            output();
        }
    }

    // Clears the grid
    public void clear_grid() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (!grid[y][x].contains("P")) {
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
                    grid[y][x] = "P" + tetromino;

                }
            }
        }

        check_row();
    }

    // Lowers the Y values of the 4 tetromino tiles
    public void lower_tetromino(boolean is_dropping, Stage primaryStage) {

        if (Y1 < grid.length - 1 && Y2 < grid.length - 1 && Y3 < grid.length - 1 && Y4 < grid.length - 1) {

            boolean can_go_down = true;

            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[y].length; x++) {
                    if (grid[y][x].equals("T1") || grid[y][x].equals("T2") || grid[y][x].equals("T3") || grid[y][x].equals("T4")) {
                        if (grid[y + 1][x].contains("P")) {
                            convert();
                            can_go_down = false;
                            create_tetromino(primaryStage);
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
           create_tetromino(primaryStage);
        }
        set_tetromino_coords();

        if (!is_dropping) {
            ghost_tetromino();
            output();
        }
    }

    // Moves the tetromino left and right
    public void move_tetromino(String direction) {
        if (direction.equalsIgnoreCase("RIGHT")) {
            if (X1 < grid[0].length - 1 && X2 < grid[0].length - 1 && X3 < grid[0].length - 1 && X4 < grid[0].length - 1) {

                boolean can_go_down = true;

                for (int y = 0; y < grid.length; y++) {
                    for (int x = 0; x < grid[y].length; x++) {
                        if (grid[y][x].equals("T1") || grid[y][x].equals("T2") || grid[y][x].equals("T3") || grid[y][x].equals("T4")) {
                            if (grid[y][x + 1].contains("P")) {
                                can_go_down = false;
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
                            if (grid[y][x - 1].contains("P")) {
                                can_go_down = false;
                                break;
                            }
                        }
                    }
                }

                if (can_go_down) {
                    X1--; X2--; X3--; X4--;
                }
            }
        }

        set_tetromino_coords();
        ghost_tetromino();
        output();
    }

    // Drops the tetromino
    public void drop(Stage primaryStage) {
        drop_pressed = true;
        while (drop_pressed) {
            lower_tetromino(true, primaryStage);
        }
        ghost_tetromino();
        output();
    }

    public void check_row() {

        int total_count = 0;

        // DELETE ROWS
        for (int y = grid.length - 1; y > 0; y--) {

            // Checks if a row is full by counting the amount of occupied ". " spaces
            int counter = 0;

            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x].contains("P")) {
                    counter += 1;
                }
            }

            // If all spaces are occupied
            if (counter == grid[y].length) {
                total_count += counter;
                deleting = true;

                // Delete the row
                for (int x = 0; x < grid[y].length; x++) {
                    grid[y][x] = ". ";
                }

                // Bring all rows above one row down
                for (int row = y; row > 0; row--) {
                    for (int x = 0; x < grid[row].length; x++) {
                        grid[row][x] = grid[row - 1][x]; 
                    }
                }

                // The first row will always be empty after a row has been deleted
                for (int column = 0; column < grid[0].length; column++) {
                    grid[0][column] = ". "; 
                }


                lines_cleared += 1;

                // Check the row again
                y++;
            }
        }

        line_clear.stop();
        line_clear_4.stop();
        if (total_count == 10) {
            points += 40;
            line_clear.play();
        } else if (total_count == 20) {
            points += 100;
            line_clear.play();
        } else if (total_count == 30) {
            points += 300;
            line_clear.play();
        } else if (total_count == 40) {
            points += 1200;
            line_clear_4.play();
            horosho.stop();
            horosho.play();
        }
    
        deleting = false;
    }

    public void pause(boolean pause) {
        game_paused = pause;
        quit_button.setVisible(game_paused);
        paused_label.setVisible(game_paused);

        if (game_paused) {
            main_theme.pause();
            for (int y = 0; y < cells.length; y++) {
                for (int x = 0; x < cells[y].length; x++) {
                    set_texture_of_cell(y, x, null_texture, cells);
                }
            }
        } else {
            main_theme.play();
            ghost_tetromino();
            output();
        }
    }

    // Updater
    public void update(Stage primaryStage) {
        if (!deleting) {
            lower_tetromino(false, primaryStage);
            set_tetromino_coords();
            ghost_tetromino();
        }
    }

     // Prepares the game when loaded
     public void prepare(Stage primaryStage) {
        main_theme.play();
        main_theme.setVolume(0.3);
        horosho.setVolume(1);
        sussy.setVolume(2);
        line_clear.setVolume(2);
        line_clear_4.setVolume(4);
        //loading_screen();
        game_in_progress = true;
        game_paused = false;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                grid[y][x] = ". ";
            }
        }
        create_tetromino(primaryStage);
        set_tetromino_coords();
        ghost_tetromino();
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

    private void set_texture_of_cell(int row, int col, Image texture, StackPane[][] cell_grid) {
        if (row >= 0 && row < grid.length && col >= 0 && col < grid[0].length) {
            StackPane cell = cell_grid[row][col];
            if (cell != null) {
                ImageView img = (ImageView) cell.getChildren().get(0);
                img.setImage(texture);
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {

        config = Backend.readConfig(false, null);
        lang = Backend.readJSON("lang", (String) config.get("lang"), null);

        paused_label = new Label((String) lang.get("paused"));
        quit_button = new Button((String) lang.get("quit"));

        prepare(primaryStage);

        GridPane grid_pane = new GridPane();
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                // Create a StackPane for each cell
                StackPane cell = new StackPane();
                ImageView cell_texture = new ImageView();
            
                // Add the rectangle to the StackPane
                cell.getChildren().add(cell_texture);

                cell_texture.setFitWidth(CELL_SIZE);
                cell_texture.setFitHeight(CELL_SIZE);

                // Add the StackPane to the grid_pane
                grid_pane.add(cell, x, y);

                // Store the reference to the cell
                cells[y][x] = cell;
            }
        }

        // Create a GridPane for the next tetromino display
        GridPane next_tetromino_grid = new GridPane();
        for (int y = 0; y < nextTetrominoCells.length; y++) {
            for (int x = 0; x < nextTetrominoCells[0].length; x++) {
                // Create a StackPane for each cell
                StackPane cell = new StackPane();
                ImageView cell_texture = new ImageView();
            
                // Add the rectangle to the StackPane
                cell.getChildren().add(cell_texture);

                cell_texture.setFitWidth(CELL_SIZE);
                cell_texture.setFitHeight(CELL_SIZE);

                // Add the StackPane to the next_tetromino_grid
                next_tetromino_grid.add(cell, x, y);

                // Store the reference to the cell
                nextTetrominoCells[y][x] = cell;
            }
        }

       // Create a BorderPane to center the GridPane
        BorderPane borderPane = new BorderPane();

        // Paused label
        paused_label.setVisible(false);
        paused_label.setStyle("-fx-font-size: 40px; -fx-text-fill: red; -fx-font-family: serif");
        paused_label.setAlignment(Pos.CENTER);

        // Quit button
        quit_button.setVisible(false);
        quit_button.setStyle("-fx-font-size: 20px; -fx-text-fill: black; -fx-font-family: serif");
        quit_button.setMinSize(230, 40);
        quit_button.setLayoutX(10000);
        quit_button.setLayoutY(500);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        StackPane game_pane = new StackPane();
        game_pane.getChildren().addAll(grid_pane, paused_label);
        StackPane button_pane = new StackPane();
        button_pane.getChildren().addAll(quit_button);
        button_pane.setLayoutX(950);
        button_pane.setLayoutY(550);

        VBox statistics = new VBox();
        statistics.setLayoutX(1220);
        statistics.setLayoutY(250);
        statistics.getChildren().addAll(points_label, lines_cleared_label); 

        game_over_screen.setMinSize(grid[0].length * CELL_SIZE, grid.length * CELL_SIZE);
        game_over_screen.setVisible(false);
        game_over_screen.setLayoutX(770);
        game_over_screen.setLayoutY(500);
        game_over_screen.toFront();

        points_earnt_game_over.setText((String) lang.get("pointsAcquired"));
        points_earnt_game_over.setStyle("-fx-font-size: 30px; -fx-text-fill: white; -fx-font-family: serif");
        points_earnt_game_over.setMinSize(points_label.USE_PREF_SIZE, points_label.USE_PREF_SIZE);

        lines_cleared_game_over.setText((String) lang.get("linesCleared"));
        lines_cleared_game_over.setStyle("-fx-font-size: 30px; -fx-text-fill: white; -fx-font-family: serif");
        lines_cleared_game_over.setMinSize(points_label.USE_PREF_SIZE, points_label.USE_PREF_SIZE);

        game_over_screen.getChildren().addAll(points_earnt_game_over, lines_cleared_game_over);

        // Grid pane
        grid_pane.setAlignment(Pos.CENTER);

        // Points label
        points_label.setVisible(true);
        points_label.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-family: serif");
        points_label.setMinSize(points_label.USE_PREF_SIZE, points_label.USE_PREF_SIZE);

        // Lines cleared label
        lines_cleared_label.setVisible(true);
        lines_cleared_label.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-family: serif");
        lines_cleared_label.setMinSize(lines_cleared_label.USE_PREF_SIZE, lines_cleared_label.USE_PREF_SIZE);

        // Next tetromino grid 
        next_tetromino_grid.setLayoutX(1220);
        next_tetromino_grid.setLayoutY(10);

        ImageView imageView = new ImageView();
        imageView.setImage(background);
        imageView.setFitHeight(1080);
        imageView.setFitWidth(1920);  // Change size

        // Adding everything to the border pane
        borderPane.setCenter(game_pane);
        borderPane.getChildren().addAll(statistics, next_tetromino_grid, imageView, button_pane, game_over_screen);
        imageView.toBack();

        // Create a Scene with the BorderPane
        Scene scene = new Scene(borderPane, grid[0].length * CELL_SIZE + nextTetrominoCells[0].length * CELL_SIZE, grid.length * CELL_SIZE);

        // Maximizes the window
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        // Names and sets the scene
        primaryStage.setTitle("JTetris");
        primaryStage.setScene(scene);

        // Scheduler: runs every second
        scheduler.scheduleAtFixedRate(() -> {
            if (game_in_progress && !game_paused) {
                update(primaryStage);
            }
        }, 0, 1, TimeUnit.SECONDS);

        borderPane.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                pause(true);
            }
        });
        borderPane.setFocusTraversable(true);

        ghost_tetromino();
        output();

        quit_button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                primaryStage.setScene(startpage.getStartPageScene());
                primaryStage.setTitle("Startpage");
            }
        });

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

                if (game_in_progress && !deleting) {
                    if (removeActiveKey("LEFT")) {
                        if (!game_paused) {
                            move.stop();
                            move.seek(Duration.ZERO);
                            move.play();
                            move_tetromino("LEFT");
                        }
                    }
    
                    if (removeActiveKey("RIGHT")) {
                        if (!game_paused) {
                            move.stop();
                            move.seek(Duration.ZERO);
                            move.play();
                            move_tetromino("RIGHT");
                        }
                    }
    
                    if (removeActiveKey("UP")) {
                        if (!game_paused) {
                            move.stop();
                            move.seek(Duration.ZERO);
                            move.play();
                            drop(primaryStage);
                        }
                    }
    
                    if (removeActiveKey("DOWN")) {
                        move.stop();
                        move.seek(Duration.ZERO);
                        move.play();
                        lower_tetromino(false, primaryStage);
                    }

                    if (removeActiveKey("SPACE")) {
                        move.stop();
                        move.seek(Duration.ZERO);
                        move.play();
                        rotate();
                    }

                    if (removeActiveKey("TAB")) {
                        current_background += 1;
                        if (current_background > 5) {
                            current_background = 1;
                        }
                        background = new Image(new File(Backend.getXdgUserDir("DOCUMENTS")+ "/myGames/Jtetris/textures/background" + current_background + ".gif").toURI().toString());
                        imageView.setImage(background);
                    }

                    if (removeActiveKey("M")) {
                        main_theme.stop();
                        if (main_theme.getMedia().getSource().contains("Tetris_TypeA.wav")) {
                            main_theme = new MediaPlayer(new Media(new File(Backend.getXdgUserDir("DOCUMENTS")+ "/myGames/Jtetris/audio/Tetris_TypeB.wav").toURI().toString()));
                        } else if (main_theme.getMedia().getSource().contains("Tetris_TypeB")) {
                            main_theme = new MediaPlayer(new Media(new File(Backend.getXdgUserDir("DOCUMENTS")+ "/myGames/Jtetris/audio/Tetris_TypeC.wav").toURI().toString()));
                        } else if (main_theme.getMedia().getSource().contains("Tetris_TypeC")) {
                            main_theme = new MediaPlayer(new Media(new File(Backend.getXdgUserDir("DOCUMENTS")+ "/myGames/Jtetris/audio/Tetris_TypeD.wav").toURI().toString()));
                        } else {
                            main_theme = new MediaPlayer(new Media(new File(Backend.getXdgUserDir("DOCUMENTS")+ "/myGames/Jtetris/audio/Tetris_TypeA.wav").toURI().toString()));
                        }
                        main_theme.setVolume(0.3);
                        main_theme.play();
                    }

                    if (removeActiveKey("ESCAPE")) {
                        if (game_paused) {
                             pause(false);
                        } else {
                            pause(true);
                        }
                    }
                }
            }
        }.start();

        primaryStage.show();
    }   

    public void setStartpage(Startpage startpage) {
        this.startpage = startpage;
    }
}