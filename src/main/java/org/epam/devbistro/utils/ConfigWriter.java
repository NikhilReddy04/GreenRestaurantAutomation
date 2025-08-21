package org.epam.devbistro.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigWriter {
    public static void setProperty(String key, String value) {
        String filePath = "src/test/resources/config.properties";
        Properties props = new Properties();

        try {
            // Load existing properties
            props.load(ConfigWriter.class.getClassLoader().getResourceAsStream("config.properties"));

            // Set new property
            props.setProperty(key, value);

            // Save back to file
            FileOutputStream out = new FileOutputStream(filePath);
            props.store(out, null);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to update config.properties", e);
        }
    }
}
