package com.tetris.backend;

import com.google.gson.Gson;

public class Backend {

    Gson gson = new Gson();

    public static void readProfiles(){
        System.out.println("Reading saved Profiles");

    }
    public static void writeProfiles(){
        System.out.println("Saving changes");

    }
    public static void readSettings(){
        System.out.println("reading Settings");
    }

}
