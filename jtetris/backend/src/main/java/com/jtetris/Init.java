package com.jtetris;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.io.IOException;



/*
This class is only executed at the start of the program.
It looks for missing files that could prevent the program from functioning.
It has the capability to recreate the files for the program to be functioning again.
This package depends on a jarfile structure and Linux as OS.
Author: Lennard Rütten
created: 22.06.24
last edited: 06.07.24
jdk: openjdk-21-jdk
OS: Ubuntu 23.10
 */

public class Init {

    public static boolean doesRootDirExist() throws IOException, InterruptedException {
        String path = Backend.getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/";
        File rootDir = new File(path);

        return rootDir.exists() && rootDir.isDirectory();
    }

    public static void redownloadTextures(String basePath) throws InterruptedException, IOException{
       File txDir = new File(basePath + "/textures");
       if (txDir.exists() && txDir.isDirectory()){
           try {
               txDir.delete();
               Backend.downloadFileManager(1);
           } catch (Exception e) {
               throw new RuntimeException(e);
           }
       } else {
           deleteFiles(basePath, true);
       }
    }


    public static void verifyFiles() throws IOException, InterruptedException {
        String basePath = Backend.getXdgUserDir("DOCUMENTS")+ "/myGames/Jtetris";
        String config = "/jtetris.config";
        String profiles = "/profiles/";
        String lang = "/languages/";
        String langFName = "lang_";
        String profileFname = "profile_";
        String backgroundName = "background";
        String backgroundEnding = ".gif";
        String[] tetrominoTexName = {"i.png", "j.png", "j_sussy.png", "l.png", "null.png", "o.png", "s.png", "t.png", "z.png", "ghost.png", "true.png"};
        String[] audioFiles = {"horosho_louder.wav", "line_clear.wav", "line_clear_4.wav", "move.wav", "sussy_baka.wav", "tetris.wav", "Tetris_TypeA.wav", "Tetris_TypeB.wav", "Tetris_TypeC.wav", "Tetris_TypeD.wav"};
        String fEnding = ".json";

        System.out.println("Searching for files...");

        //looking for the base Folder
        File baseFolder = new File(basePath);
        if (baseFolder.exists() && baseFolder.isDirectory()) {
            System.out.println("Found base directory");
        } else {
            System.out.println("Couldn't find base directory!");
            deleteFiles(basePath, true);
        }
        //looking for the config file
        File configF = new File(basePath + config);
        if (configF.exists() && configF.isFile()) {
            System.out.println("Found config file");
        } else {
            System.out.println("Couldnt find config file!");
            System.out.println("This file is necessary to initialize the program");
            deleteFiles(basePath, true);
        }
        //looking for available language Packs
        File languages = new File(basePath + lang);
        if (languages.exists() && languages.isDirectory()) {
            FilenameFilter langFilter = new FilenameFilter() {
                @Override
                public boolean accept(File type, String name) {
                    return name.contains(langFName) && name.endsWith(fEnding);
                }
            };

            File[] matchingLangFiles = languages.listFiles(langFilter);
            if (matchingLangFiles != null && matchingLangFiles.length > 0) {
                for (File file : matchingLangFiles) {
                    System.out.println("Language " + file.getName() + " found");
                }
            } else {
                System.out.println("No language packs could be found!");
                System.out.println("At the moment the program can not run without a Profile!");
                deleteFiles(basePath, true);
            }
        } else {
            System.out.println("Could not find a language pack!");
            System.out.println("Without a language pack no text can be shown in the Program");
            deleteFiles(basePath, true);
        }

        //looking for installed profiles
        File profileF = new File(basePath + profiles);
        if (profileF.exists() && profileF.isDirectory()) {
            FilenameFilter profileFilter = new FilenameFilter() {
                @Override
                public boolean accept(File type, String name) {
                    return name.contains(profileFname) && name.endsWith(fEnding);
                }
            };

            File[] matchingProfileFiles = profileF.listFiles(profileFilter);
            if (matchingProfileFiles != null && matchingProfileFiles.length > 0) {
                for (File file : matchingProfileFiles) {
                    System.out.println("Profile " + file.getName() + " found");
                }
            } else {
                System.out.println("No Profiles could be found!");
                System.out.println("At the moment the program can not run without a Profile!");
                deleteFiles(basePath, true);
            }
        } else {
            System.out.println("No Profiles could be found!");
            System.out.println("At the moment the program can not run without a Profile!");
            deleteFiles(basePath, true);
        }

        //looking if tetromino textures are at the right place
        for (String Element : tetrominoTexName){
            File texture = new File(basePath + "/textures/" + Element);
            if (!texture.exists() && !texture.isFile()){
                System.out.println("Some textures are missing");
                try {
                    File tetrDir = new File(basePath + "/textures");
                    if (!tetrDir.exists() || !tetrDir.isDirectory()){
                        deleteFiles(basePath, true);
                    } else{
                        redownloadTextures(basePath);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            
        }

        //looking if the backgrounds are at the right place
        for (int i = 0; i < 5; i++){
            File background = new File(basePath + "/textures/" + backgroundName + (i+1) + backgroundEnding);

            if (!background.exists() && !background.isFile()){
                System.out.println("Some backgrounds are missing");
                try {
                    File tetrDir = new File(basePath + "/textures");
                    if (!tetrDir.exists() || !tetrDir.isDirectory()){
                        deleteFiles(basePath, true);
                    } else{
                        redownloadTextures(basePath);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }

        //looking if the audio files are in the right place
        for (String file : audioFiles) {
            File audioFile = new File(basePath + "/audio/" + file);
            if (!audioFile.exists() && !audioFile.isFile()){
                System.out.println("Some audio files are missing");
                File audioDir = new File(basePath + "/audio");
                if (audioDir.exists() && audioDir.isDirectory()){
                    try {
                    audioDir.delete();
                    Backend.downloadFileManager(2);
                    } catch (Exception e){
                        System.out.println(e);
                        deleteFiles(basePath, true);
                    }
                }
            }
        }


        System.out.println("Everything set!\nStarting Jtetris!");

    }

    


    public static void deleteFiles(String basePath, boolean createNew) throws IOException, InterruptedException {
        Scanner in = new Scanner(System.in);
        String value;
        System.out.println("The program can try to repair the files...");
        System.out.println("Do you want your files to be repaired? The directory structure will get recreated during that process.");
        System.out.println("If you have any saved profiles you want to keep, back them up now!");
        System.out.println("Repair files? (y/N)");
        value = in.nextLine();
        if (value.equals("y") || value.equals("Y")) {
            System.out.println("Starting repairing process");
        } else {
            System.out.println("Exiting...");
            System.exit(0);
        }
        File FileRootDir = new File(basePath);
        if (deleteDir(FileRootDir)) {
            System.out.println("Successfully deleted program file root!");
            System.out.println("Creating new one");
        } else {
            System.out.println("Failed to delete directory");
            System.out.println("You might have to reinstall Jtetris");
            System.out.println("Exiting...");
            System.exit(1);
        }
        if (createNew){
            createProgramDir();
        } else {
            System.out.println("Uninstalled successfully!");
            System.out.println("Have a nice one!");
            System.out.println("Exiting...");
            System.exit(0);
        }
    }


    public static boolean deleteDir(File dir){
        if (dir.isDirectory()) {
            File[] containingFiles = dir.listFiles();
            assert containingFiles != null;
            for (File file : containingFiles) {
                deleteDir(file);
            }
        }
        return dir.delete();
    }

    public static void createProgramDir() throws IOException, InterruptedException {
        Backend.downloadFileManager(4);
    }
    public static void moveFiles()throws IOException{
        Backend.downloadFileManager(4);
    }



    }
