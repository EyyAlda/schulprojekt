package com.tetris.backend;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.util.Scanner;



public class Backend {

    public static HashMap<String, Object> hashMap(String param, String type) throws IOException {
        Gson gson = new Gson();
        HashMap<String, Object> map = null;
        String filePath = null;
        switch (type) {
            case "lang":
                filePath = System.getProperty("user.home") + "/Dokumente/languages/" + param + ".json";
                try (FileReader reader = new FileReader(filePath)) {
                    // Parse the JSON file into a HashMap
                    map = gson.fromJson(reader, HashMap.class);

                    // Print the contents of the HashMap
                } catch (JsonIOException | JsonSyntaxException | IOException e) {
                    e.printStackTrace();
                }
                break;

            case "profile":
                filePath = System.getProperty("user.home")+ "/Dokumente/profiles/"+param+".json";
                try (FileReader reader = new FileReader(filePath)) {
                // Parse the JSON file into a HashMap
                map = gson.fromJson(reader, HashMap.class);

                // Print the contents of the HashMap
                } catch (JsonIOException | JsonSyntaxException | IOException e) {
                e.printStackTrace();
                }
                break;

        }
        return map;
    }

}
