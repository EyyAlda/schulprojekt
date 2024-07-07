package org.jtetris;

import java.io.IOException;
import java.util.HashMap;

public class Main {
    HashMap<String, Object> lang = null;
    HashMap<String, Object> profile = null;
    HashMap<String, Object> config = null;
    String[] profilesList;

    public void start() throws IOException, InterruptedException {



        if (Init.doesRootDirExist()) {
            Init.verifyFiles();
        } else {
            Init.createProgramDir();
        }

            mainmenu.main(null);

            profilesList = Backend.profilesList();
            /* Select Profile Function should be linked here */
            config = Backend.readConfig(false,null);
            System.out.println("Loading profile "+ config.get("lastProfile"));
            try {

                profile = Backend.readJSON("profile", (String) config.get("lastProfile"), null);
            } catch (IOException e){
                throw new IOException(e);
            }

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
            assert lang != null;
            System.out.println("Using language "+ lang.get("lang"));
            try {
                config.put("lastProfile", profile.get("profileName"));
                config.put("lang", profile.get("lang"));
                Backend.writeConfig(config);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        System.exit(0);

    }



    public static void main(String[] args) throws IOException, InterruptedException {
        new Main().start();

    }
}
