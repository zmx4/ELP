package org.tick.elp.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tick.elp.Helper.PathHelper;

public class FilePreferenceStorage implements IPreferenceStorage{

    private static final Logger logger = Logger.getLogger(FilePreferenceStorage.class.getName());

    @Override
    public void set(String key, String value) {
        var filePath = PathHelper.getLocalFilePath(key);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(value);
        } catch (IOException e) {
            logger.log(Level.SEVERE, null, e);
        }
    }

    @Override
    public String get(String key, String defaultValue) {
        var filePath = PathHelper.getLocalFilePath(key);
        File file = new File(filePath);
        if (!file.exists()) {
            return defaultValue;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.readLine();
        } catch (IOException e) {
            logger.log(Level.SEVERE, null, e);
            return defaultValue;
        }
    }

    @Override
    public void set(String key, boolean value) {
        set(key, Boolean.toString(value));
    }

    @Override
    public boolean get(String key, boolean defaultValue) {
        String value = get(key, Boolean.toString(defaultValue));
        return Boolean.parseBoolean(value);
    }

    @Override
    public void set(String key, int value) {
        set(key, Integer.toString(value));
    }

    @Override
    public int get(String key, int defaultValue) {
        String value = get(key, Integer.toString(defaultValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, null, e);
            return defaultValue;
        }
    }

    @Override
    public void set(String key, Date value) {
        set(key, Long.toString(value.getTime()));
    }

    @Override
    public Date get(String key, Date defaultValue) {
        String value = get(key, Long.toString(defaultValue.getTime()));
        try {
            long time = Long.parseLong(value);
            return new Date(time);
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, null, e);
            return defaultValue;
        }
    }
    
}
