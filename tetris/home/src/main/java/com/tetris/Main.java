package com.tetris.home;
import com.tetris.init.initc;


public class Main { 
    public void start(){
        System.out.println("Executing home!");
        initc.verifyFiles();

    }
    public static void main(String[] args) {
        new Main().start();
    }
}
