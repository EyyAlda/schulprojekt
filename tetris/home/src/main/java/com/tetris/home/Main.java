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
        catch(){
            System.out.println("Error reading files");
            System.out.println("Do you want to repair?");
            Init.repairFiles();


        }

    }


    public static void main(String[] args){
        new Main().start();

    }
}
