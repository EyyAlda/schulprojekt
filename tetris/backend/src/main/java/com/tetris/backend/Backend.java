package com.tetris.backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.util.Scanner;



public class Backend {

    public static  HashMap<String, Object> readJSON(String type, String param) throws IOException, InterruptedException {
        Gson gson = new Gson();
        HashMap<String, Object> map = null;
        String filePath = null;
        switch (type) {
            case "lang":
                filePath = getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/languages/lang_" + param + ".json";
                try (FileReader reader = new FileReader(filePath)) {
                    // Parse the JSON file into a HashMap
                    map = gson.fromJson(reader, HashMap.class);

                    // Print the contents of the HashMap
                } catch (JsonIOException | JsonSyntaxException | IOException e) {
                throw new RuntimeException(e);
                }
                break;

            case "profile":
                filePath = getXdgUserDir("DOCUMENTS")+ "/myGames/Jtetris/profiles/profile_"+param+".json";
                try (FileReader reader = new FileReader(filePath)) {
                // Parse the JSON file into a HashMap
                map = gson.fromJson(reader, HashMap.class);

                // Print the contents of the HashMap
                } catch (JsonIOException | JsonSyntaxException | IOException e) {
                throw new RuntimeException(e);
                }
                break;
            default:
                System.out.println("No fitting parameter... \n Have you called the right function?");
                return null;

        }
        return map;
    }
    public static void readConfig(){


        
    }

    public static String getXdgUserDir(String dirType) throws IOException, InterruptedException {
        // Construct the command
        String command = "xdg-user-dir " + dirType;

        // Start the process
        Process process = Runtime.getRuntime().exec(command);

        // Get the output of the command
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String path = reader.readLine();

        // Wait for the process to complete
        int exitCode = process.waitFor();
        if (exitCode == 0 && path != null && !path.isEmpty()) {
            return path;
        } else {
            return null;
        }
    }

}
