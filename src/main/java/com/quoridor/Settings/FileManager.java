package com.quoridor.Settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONObject;

public class FileManager {
    public static JSONObject readJSONFromFile(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            return new JSONObject(content);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
