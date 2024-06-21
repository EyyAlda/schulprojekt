package com.tetris.home;

import com.tetris.backend.Backend;
import com.tetris.backend.Init;
import com.tetris.tetris_game.main_handler;

import java.io.FileNotFoundException;

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

    }


    public static void main(String[] args){
        new Main().start();

    }
}
