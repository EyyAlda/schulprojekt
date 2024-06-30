package com.tetris.backend;

import java.util.ArrayList;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.FileInputStream;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;


public class Backend {

    static String basePath;

    static {
        try {
            basePath = getXdgUserDir("DOCUMENTS") + "/myGames/Jtetris";
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] profilesList() throws IOException, InterruptedException{
        ArrayList<String> list = new ArrayList<>();
        File baseDir = new File(getXdgUserDir("DOCUMENTS") + "/profiles");
        if (baseDir.exists() && baseDir.isDirectory()){
            FileFilter profileFilter = file -> file.isFile() && file.getName().contains("profile_") && file.getName().endsWith(".json");

            File[] prfFiles = baseDir.listFiles(profileFilter);

            if (prfFiles != null){
                for (int i = 0; i< prfFiles.length; i++){
                    list.add(prfFiles[i].toString());
                }
                String[] prfFileNames = new String[list.size()];
                return list.toArray(prfFileNames);
            }
        }            
        return null;
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

    public static HashMap<String, Object> readConfig(boolean isCached, HashMap<String, Object> conf) throws IOException {
        HashMap<String, Object> values = new HashMap<>();
        File config = new File(basePath + "/jtetris.config");
        if (!config.exists() && isCached){
            System.out.println("The config file has been deleted for some reason");
            System.out.println("creating new...");
            if (writeConfig(conf)){
                System.out.println("Saved new config file");
            } else {
                System.out.println("Couldn't create new config file");
                System.out.println("Exiting...");
                System.exit(1);
            }
        } else if (!config.exists() && !isCached){
            System.out.println("Cannot create new config file...");
            System.out.println("Exiting...");
            System.exit(0);
        }
        Properties configF = new Properties();
        try (FileInputStream input = new FileInputStream(basePath + "/jtetris.config")) {
            configF.load(input);
            for (String key : configF.stringPropertyNames()){
                assert false;
                values.put(key, configF.getProperty(key));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return values;
    }

    public static boolean writeConfig(HashMap<String, Object> conf) throws IOException {
        Properties configF = new Properties();
        configF.putAll(conf);
        File configFileCheck = new File(basePath + "/jtetris.config");
        if (configFileCheck.exists()){
            if (configFileCheck.delete()){
                try (FileOutputStream output = new FileOutputStream(basePath + "/jtetris.config")){
                    configF.store(output, "Saved config");

                } catch (IOException e){
                    throw new IOException(e);
                }
            } else {
                System.out.println("Error while trying to write to config file");
                return false;
            }

        }
        return true;
    }



    public static String getXdgUserDir(String dirType) throws IOException, InterruptedException {
        String command = "xdg-user-dir " + dirType;
        
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
