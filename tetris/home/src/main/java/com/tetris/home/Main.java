package com.tetris.home;

import com.tetris.backend.Backend;
import com.tetris.backend.Init;
import com.tetris.tetris_game.main_handler;

public class Main {

    public void start(){
        try {
            Init.verifyFiles();
        }
        catch(Exception e){
            System.out.println(e);
            System.out.println("Error reading files");
            System.out.println("Do you want to repair?");
            Init.repairFiles();
        }
        Backend.readSettings();
        Backend.readProfiles();

        main_handler.start();

    }


    public static void main(String[] args){
        new Main().start();

    }
}
