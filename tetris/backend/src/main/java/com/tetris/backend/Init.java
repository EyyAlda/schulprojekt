package com.tetris.backend;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.jar.*;

/*
This class is only executed at the start of the program.
It looks for missing files that could prevent the program from functioning.
It has the capability to recreate the files for the program to be functioning again.
This package depends on a jarfile structure and Linux as OS.
Author: Lennard Rütten
created: 22.06.24
last edited: 24.06.24
jdk: openjdk-17-jdk
OS: Ubuntu 23.10
 */

public class Init {

    public static boolean doesRootDirExist() throws IOException, InterruptedException {
        String path = Backend.getXdgUserDir("DOCUMENTS")+"/myGames/Jtetris/";
        File rootDir = new File(path);

        return rootDir.exists() && rootDir.isDirectory();
    }


    public static void verifyFiles() throws IOException, InterruptedException {
        String basePath = Backend.getXdgUserDir("DOCUMENTS")+ "/myGames/Jtetris";
        String config = "/jtetris.config";
        String profiles = "/profiles/";
        String lang = "/languages/";
        String langFName = "lang_";
        String profileFname = "profile_";
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
        Scanner in = new Scanner(System.in);


        System.out.println("Do you want to use the language and profile presets included in the jar? (O)");
        System.out.println("It is also possible to download them from the GitHub repository (g)\n (doesn't happen automatically)");
        System.out.println("link: https://github.com/EyyAlda/schulprojekt");
        System.out.println("(g/o/n");
        String key = in.nextLine();
        if (key.equals("g") || key.equals("G")){
            moveFiles(false);
        } else if (key.equals("o") || key.equals("O")){
            System.out.println("Using the files included in the jar");
            moveFiles(true);
        } else {
            System.out.println("Cancelled");
            System.exit(1);
        }





    }

    public static void moveFiles(boolean includeFiles) throws IOException, InterruptedException {
        String basePath = Backend.getXdgUserDir("DOCUMENTS")+ "/myGames/Jtetris";
        File rootDir = new File(basePath);
        System.out.println("Download the files and put the config in /Dokumente/myGames/Jtetris/");
        System.out.println("the languages in the /languages/ and the profile_default.json in /profiles/");
        System.out.println("after doing so start the Jtetris again");
        if (rootDir.mkdirs()) {
            System.out.println("root directory created");
        } else {
            System.out.println("unable to create root directory");
            System.out.println("Exiting...");
            System.exit(1);
        }

        File langDir = new File(basePath + "/languages/");
        if (langDir.mkdir()) {
            System.out.println("Language directory created");
        } else {
            System.out.println("Unable to create language directory");
            System.out.println("Exiting...");
            System.exit(1);
        }

        File profDir = new File(basePath + "/profiles/");
        if (profDir.mkdir()) {
            System.out.println("Created profile directory");
        } else {
            System.out.println("Couldn't create profile directory");
        }
        //stop the Jtetris if the user wants to copy the files manually
        if (!includeFiles){
            System.exit(0);
        }
        System.exit(0);
        /*try {
            String jarPath = Init.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        } catch ( e){
            throw new IOException(e);
        }
        */
    }

}
