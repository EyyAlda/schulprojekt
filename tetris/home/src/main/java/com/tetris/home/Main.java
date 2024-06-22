package com.tetris.home;

import com.tetris.backend.Backend;
import com.tetris.backend.Init;

import java.io.IOException;
import java.util.HashMap;
//import com.tetris.tetris_game.main_handler;

public class Main {
    HashMap<String, Object> lang = null;
    HashMap<String, Object> profile = null;

    public void start() throws IOException {

        try {
            Init.verifyFiles();
        }
        catch(Exception e){
            System.out.println(e);
            System.out.println("Error reading files");
            System.out.println("Do you want to repair?");
            Init.repairFiles();
        }
        /* Select Profile Function should be placed here */
        String prName = "?Name?";
        profile = Backend.hashMap(prName, "profile");
        lang = Backend.hashMap(profile.lang, "lang");


        //main_handler.start();

    }


    public static void main(String[] args) throws IOException {
        new Main().start();

    }
}
