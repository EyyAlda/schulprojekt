package org.jtetris;

import java.io.IOException;
import java.util.HashMap;

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
        String prName = "default";
        System.out.println("Loading profile "+prName);
        try {

            profile = Backend.readJSON("profile",prName, null);
        } catch (IOException e){
            throw new IOException(e);
        }
        main_handler.main(null);



        if (profile != null) {
            lang = Backend.readJSON("lang", (String) profile.get("lang"), null);
        } else {
            profile = Backend.readJSON("profile", "default", null);
            if (profile == null){
                System.out.println("Error: Could not load profiles!");
                System.out.println("Exiting...");
                System.exit(1);
            }
            lang = Backend.readJSON("lang", "en", null);
        }
        System.exit(0);

        //main_handler.start();

    }


    public static void main(String[] args) throws IOException, InterruptedException {
        new Main().start();

    }
}
