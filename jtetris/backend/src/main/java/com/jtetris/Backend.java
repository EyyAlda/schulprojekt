package com.jtetris;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import javax.net.ssl.HttpsURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.IOUtils;


public class Backend {

    static String basePath;

    static {
        basePath = getXdgUserDir("DOCUMENTS") + "/myGames/Jtetris";
    }

    public static String[] profilesList(){
        ArrayList<String> list = new ArrayList<>();
        File baseDir = new File(getXdgUserDir("DOCUMENTS") + "/myGames/Jtetris/profiles");
        if (baseDir.exists() && baseDir.isDirectory()){
            FileFilter profileFilter = file -> file.isFile() && file.getName().startsWith("profile_") && file.getName().endsWith(".json");

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

    public static HashMap<String, Object> readJSON(String type, String param, String path) throws IOException, InterruptedException {
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
            System.out.println(e);
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



    public static String getXdgUserDir(String dirType) {
        String command = "xdg-user-dir " + dirType;
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String path = reader.readLine();

        // Wait for the process to complete
        int exitCode = process.waitFor();
        if (exitCode == 0 && path != null && !path.isEmpty()) {
            return path;
        } else {
            return null;
        }
        } catch (Exception e) {
            return null;
        }
    }
    

    public static void downloadFileManager(int fileType) throws IOException {
        String[] fileUrl = {"https://www.dropbox.com/scl/fo/qc8qwdphogw4i0mellj0k/AH-nyq2l9uxZWkYD4CtO2iw?rlkey=w78nd8gkv3p1ecj8stwh1w5e7&st=6b0u7mn1&dl=1", "https://www.dropbox.com/scl/fo/zripd99r8mu5jurknakrp/ACTCNWILsq7gO3HF0nKNrF0?rlkey=sejzzaj0yt3ubt64y30sgjqsl&st=sde4l7y6&dl=1", "https://www.dropbox.com/scl/fo/dfusbez5c5aokmyjl8z18/AOgp2Obi8OYUMcjZOW06hv4?rlkey=b2ssec2s35p6zzw5f4zhfmqxb&st=fdylahw5&dl=1"};
        int UrlChooser = 0;
        String destPath;
        for (int i = 0; i < 3; i++){
            destPath = null;
            String dldFilePath;
            switch (fileType) {
                case 1:
                    //only downloads textures
                    break;
                case 2:
                    //only downloads audio
                    UrlChooser = 1;
                    break;
                case 3:
                    //only downloads configs
                    UrlChooser = 2;
                    break;
                case 4:
                    //downloads everything
                    UrlChooser = i;
                    break;
                default:
                    System.out.println("Nothing to download");
                    break;
            }
            if (fileType == 1 && i == 1 || fileType == 2 && i == 1 || fileType == 3 && i == 1){
                break;
            }
            switch (UrlChooser){
                case 0:
                    destPath = basePath + "/textures";
                    break;
                case 1:
                    destPath = basePath +"/audio";
                    break;
                case 2:
                    destPath = basePath;
                    break;
            }
            dldFilePath = downloadFiles(fileUrl[UrlChooser]);
            extractZip(dldFilePath, destPath);
        }
        File tetrisConf = new File(System.getProperty("user.home") + "/tetrisConfFiles.zip");
        File audio = new File(System.getProperty("user.home")+ "/audio.zip");
        File textures = new File(System.getProperty("user.home")+ "/textures.zip");
        try {
            audio.delete();
            tetrisConf.delete();
            textures.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Files downloaded successfully");
    }

    public static String downloadFiles(String path)throws IOException{
        URL url = new URL(path);
        String saveDir = System.getProperty("user.home");
        HttpURLConnection httpURLConnection;
        if (path.startsWith("https")){
            httpURLConnection = (HttpsURLConnection) url.openConnection();
        } else {
            httpURLConnection = (HttpURLConnection) url.openConnection();
        }
        int responseCode = httpURLConnection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK){
            String fileName = "";
            String disposition = httpURLConnection.getHeaderField("Content-Disposition");

            if (disposition != null) {
                Pattern pattern = Pattern.compile("filename\\*=UTF-8''(.+)|filename=\"(.+?)\"");
                Matcher matcher = pattern.matcher(disposition);
                if (matcher.find()) {
                    if (matcher.group(1) != null) {
                        fileName = URLDecoder.decode(matcher.group(1), "UTF-8");
                    } else if (matcher.group(2) != null) {
                        fileName = matcher.group(2);
                    }
                }
            }
            if (fileName.isEmpty()) {
                fileName = path.substring(path.lastIndexOf("/") + 1);
            }
            System.out.println("Downloading " + fileName);

            InputStream inputStream = httpURLConnection.getInputStream();
            String saveFilePath = saveDir + File.separator + fileName;

            FileOutputStream fileOutputStream = new FileOutputStream(saveFilePath);

            int bytesRead;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1){
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            fileOutputStream.close();
            inputStream.close();
            System.out.println("Downloaded successfully");
            System.out.println(saveFilePath);
            return saveFilePath;
        } else {
            System.out.println("Could not download files! Code " + responseCode);
        }

        return null;
    }

    private static void extractZip(String zipFilePath, String destPath) throws IOException{
        File destDirFile = new File(destPath);
        if (!destDirFile.exists()){
            destDirFile.mkdirs();
        }

        try (ZipFile zipFile = new ZipFile(new File(zipFilePath))) {
            zipFile.getEntries().asIterator().forEachRemaining(entry -> {
                try {
                    extractEntry(zipFile, entry, destPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }
    private static void extractEntry(ZipFile zipFile, ZipArchiveEntry entry, String destDir) throws IOException {
        File file = new File(destDir, entry.getName());
        if (entry.isDirectory()) {
            file.mkdirs();
        } else {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            try (InputStream is = zipFile.getInputStream(entry);
                 OutputStream os = new FileOutputStream(file)) {
                IOUtils.copy(is, os);
            }
        }
    }

}

