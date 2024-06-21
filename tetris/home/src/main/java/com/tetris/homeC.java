package com.tetris.home;
import com.tetris.init.initc;


public class homeC { 
    public void start(){
        System.out.println("Executing home!");
        initc.verifyFiles();

    }
    public static void main(String[] args) {
        new homeC().start();
    }
}
