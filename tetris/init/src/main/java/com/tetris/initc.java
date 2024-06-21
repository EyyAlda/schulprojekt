package com.tetris.init;

import java.util.HashMap;
import com.google.gson.Gson;
//import com.tetris.backend.Main;


public class initc {

	Gson gson = new Gson();
	//backend.Main backend = new Backend();
	HashMap<String, Object> filesPlaced = new HashMap<>();

	public void fillHashMap(){
		filesPlaced.put("settings", false);
		filesPlaced.put("profiles", false);
		filesPlaced.put("mediaSound", false);
		filesPlaced.put("mediaImgs", false);
	}
	public static void verifyFiles(){
		System.out.println("verifying files...");
		//backend.scanFiles();
	}

	public void start(){
	fillHashMap(); 
	verifyFiles();
	}


    public void main(String[] args) {
		new initc().start();       
    }
}
