package com.example.service;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FileHelperService {

    private static final String FILE_DIRECTORY = "src/main/java/com/example/data/";

    private Gson gson;

    public FileHelperService() {
        gson = new Gson();
    }

    public static String getFileName(String fileName) {
        return FILE_DIRECTORY + fileName;
    }

    public boolean fileExists(String fileName) {
        return new File(getFileName(fileName)).exists();
    }

    public void storeJsonAsFile(String fileName, JsonObject data) {

        if (StringUtils.isEmpty(fileName) || data == null) {
            throw new IllegalArgumentException("Invalid file name or data");
        }

        File file = new File(getFileName(fileName));
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(data.toString());
            writer.close();
        } catch (IOException e) {
            file.delete();
            log.error("Could not store file: " + fileName);
            e.printStackTrace();
        }
    }

    public JsonObject getJsonFromFile(String fileName) {

        if (StringUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException(fileName);
        }

        File file = new File(getFileName(fileName));
        try {
            return gson.fromJson(new BufferedReader(new FileReader(file)), JsonObject.class);
        } catch (IOException e) {
            log.error("Could not retrieve file: " + fileName);
            e.printStackTrace();
            return null;
        }
    }
}
