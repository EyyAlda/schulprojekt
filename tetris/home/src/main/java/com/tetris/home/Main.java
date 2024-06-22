package com.tetris.home;

import com.tetris.backend.Backend;
import com.tetris.backend.Init;

import java.io.IOException;
import java.util.HashMap;
//import com.tetris.tetris_game.main_handler;

public class Main {
    HashMap<String, Object> lang = null;
    HashMap<String, Object> profile = null;

    public void start() throws IOException, InterruptedException {

        if (Init.doesRootDirExist()) {
            Init.verifyFiles();
        } else {
            Init.createProgramDir();
        }
        /* Select Profile Function should be linked here */
        String prName = "?Name?";
        System.out.println("Loading profile");
        profile = Backend.readJSON("profile",prName);
        if (profile != null) {
            lang = Backend.readJSON("lang", (String) profile.get("language"));
        } else {
            profile = Backend.readJSON("profile", "default");
            if (profile == null){
                System.out.println("Error: Could not load profiles!");
                System.out.println("Exiting...");
                System.exit(1);
            }
            lang = Backend.readJSON("lang", "en");
        }
        System.exit(0);

        //main_handler.start();

    }


    public static void main(String[] args) throws IOException, InterruptedException {
        new Main().start();

    }
}
