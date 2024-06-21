package com.tetris.backend;

import com.google.gson.Gson;

public class Backend {

    Gson gson = new Gson();

    public void readProfiles(){
        System.out.println("Reading saved Profiles");

    }
    public void writeProfiles(){
        System.out.println("Saving changes");

    }

}
