package com.jtetris;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (!Init.doesRootDirExist()){
            Init.createProgramDir();
        } else {
            Init.verifyFiles();
        }
        Startpage.main(null);
    }
}