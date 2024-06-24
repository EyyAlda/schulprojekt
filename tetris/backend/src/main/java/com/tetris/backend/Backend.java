package com.tetris.backend;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.util.Scanner;



public class Backend {
    static String basePath;

    static {
        try {
            basePath = getXdgUserDir("DOCUMENTS") + "/myGames/Jtetris";
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static  HashMap<String, Object> readJSON(String type, String param, String path) throws IOException, InterruptedException {
        Gson gson = new Gson();
        HashMap<String, Object> map;
        String filePath;
        switch (type) {
            case "lang":
                filePath = getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/languages/lang_" + param + ".json";
                try (FileReader reader = new FileReader(filePath)) {

                    map = gson.fromJson(reader, HashMap.class);

                } catch (JsonIOException | JsonSyntaxException | IOException e) {
                throw new RuntimeException(e);
                }
                break;

            case "profile":
                filePath = getXdgUserDir("DOCUMENTS")+ "/myGames/Jtetris/profiles/profile_"+param+".json";
                try (FileReader reader = new FileReader(filePath)) {

                map = gson.fromJson(reader, HashMap.class);

                } catch (JsonIOException | JsonSyntaxException | IOException e) {
                throw new RuntimeException(e);
                }
                break;
            case "custom":
                try (FileReader reader = new FileReader(path)){
                    map = gson.fromJson(reader, HashMap.class);
                } catch (JsonIOException | JsonSyntaxException | IOException e){
                    throw new RuntimeException(e);
                }
                break;
            default:
                System.out.println("No fitting parameter... \n Have you called the right function?");
                return null;

        }
        return map;
    }
    public static void readConfig(boolean isCached, ResourceBundle conf){
        File config = new File(basePath + "/jtetris.config");
        if (!config.exists() && isCached){
            System.out.println("The config file has been deleted for some reason");
            System.out.println("creating new...");
            if (writeConfig(true, conf)){
                System.out.println("Saved new config file");
            }
        } else if (!config.exists() && !isCached){
            System.out.println("Cannot create new config file...");
            System.out.println("Exiting...");
            System.exit(0);
        }

        
    }


    public static boolean writeProfiles(HashMap<String, Object> map, String profileName) throws IOException, InterruptedException {
        Gson gson = new Gson();
        String path = getXdgUserDir("DOCUMENTS") + "/myGames/Jtetris/profiles/";
        String toJson = gson.toJson(map);

        Path dirPath = Paths.get(path);
        if (!Files.exists(dirPath)){
            Files.createDirectories(dirPath);
        }

        File fileTest = new File(path + "profile_" + profileName + ".json");
        if (fileTest.exists() && fileTest.isFile()){
            if (fileTest.delete()){
                System.out.println("Deleted the old profile file");
            } else {
                System.out.println("Couldn't delete old profile file");
                System.out.println("Changes cannot be saved right now");
                return false;
            }

        }

        try (FileWriter profileData = new FileWriter(path+"profile_"+profileName+".json")){
            profileData.write(toJson);
            System.out.println("Profile saved");
        } catch (IOException e){
            System.out.println(new IOException(e));
            return false;
        }
        return true;
    }

    public static boolean writeConfig(boolean newOne, ResourceBundle conf){





        return true;
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
