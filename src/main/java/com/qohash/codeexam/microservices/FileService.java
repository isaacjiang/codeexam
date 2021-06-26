package com.qohash.codeexam.microservices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class FileService {

    public String DEFAULT_ROOT = "home";
    public String DEFAULT_OUTPUT = "tmp";

    public FileService() {
    }

    /***
     * save default folders and files list and save to default output file
     */
    public void saveDefaultFoldersAndFilesList() {
        JSONObject output = getFoldersAndFilesList(DEFAULT_ROOT);
        String outputFile = (DEFAULT_OUTPUT.startsWith("/") ? "" : "/") + DEFAULT_OUTPUT + (DEFAULT_OUTPUT.endsWith("/") ? "" : "/") + "output.txt";
        saveFoldersAndFilesList(output,outputFile);
    }

    /***
     * Save JSONObject to default file
     * @param jsonObject
     */
    public void saveFoldersAndFilesList(JSONObject jsonObject,String outputFile) {
        FileWriter file = null;
        try {
            // Constructs a FileWriter given a file name, using the platform's default charset
            Path path = Paths.get(outputFile);
            if (Files.exists(path)) {
                file = new FileWriter(String.valueOf(path), true);
            } else {
                Files.createFile(path);
                file = new FileWriter(String.valueOf(path), false);
            }

            file.write(jsonObject.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /***
     * get all folders and files under root folder
     * get last modified time and file size
     * count total files and calculate total files size
     * sort the results base on size
     * @param root
     * @return
     */
    public JSONObject getFoldersAndFilesList(String root) {
        Path rootPath = Paths.get((root.startsWith("/") ? "" : "/") + root);
        JSONObject output = new JSONObject();
        AtomicReference<Integer> total_file = new AtomicReference<>(0);
        AtomicReference<Long> total_size = new AtomicReference<>(0L);
        JSONArray elements = new JSONArray();
        List<JSONObject> elementList = new ArrayList<>();
        try {
            Files.newDirectoryStream(rootPath).forEach(file -> {
                JSONObject element = new JSONObject();
                try {
                    element.put("name", file.getFileName());
                    element.put("size", Files.size(file));
                    element.put("lastModifiedTime", Files.getLastModifiedTime(file).toString().split("\\.")[0].replace("T", " "));
                    element.put("type", Files.isDirectory(file) ? "Directory" : "File");
                    elementList.add(element);
                    if (Files.isRegularFile(file)) {
                        total_file.updateAndGet(v -> v + 1);
                    }
                    total_size.updateAndGet(v -> {
                        try {
                            return v + Files.size(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return v;
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Sort files or folders by size
        elementList.sort((jsonObjectA, jsonObjectB) -> {
            int compare = 0;
            try {
                Long keyA = jsonObjectA.getLong("size");
                Long keyB = jsonObjectB.getLong("size");
                compare = keyA.compareTo(keyB);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return compare;
        });

        for (JSONObject element : elementList) {
            elements.put(element);
        }
        output.put("total_file", total_file);
        output.put("total_size", total_size);
        output.put("elements", elements);
        return output;
    }
}
