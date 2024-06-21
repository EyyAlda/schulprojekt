package com.tetris.backend;

import com.google.gson.Gson;
public class Backend {

	public void scanFiles(){
		System.out.println("Scanning");
	}
	public void start(){
		System.out.println("Executing Start in Backend");
	}

	public static void main(String[] args) {
		new Backend().start();
	}
}

