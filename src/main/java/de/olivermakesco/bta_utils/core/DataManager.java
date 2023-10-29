package de.olivermakesco.bta_utils.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import de.olivermakesco.bta_utils.BtaUtilsMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import processing.core.PApplet;

public class DataManager {
    // thanks to Processing foundation and micycle1 for processing 4 (LGPL)

    static public File initDataFile(String fileName) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            File filePath = getFile(fileName);
            if (!filePath.getParentFile().exists()) {
                filePath.getParentFile().mkdirs();
            }
            try {
                filePath.createNewFile();
                return filePath;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            BtaUtilsMod.LOGGER.debug("Data manager not working on client yet");
        return null;

    }

    static public String getString(String fileName, String key) {
        return getString(getFile(fileName), key);
    }

    static public String getString(File file, String key) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            String[] data = PApplet.loadStrings(file);
            for (String string : data) {
                String[] keyValue = string.split(";");
                if (keyValue[0].equals(key))
                    return keyValue[1];
            }
        } else
            BtaUtilsMod.LOGGER.debug("Data manager not working on client yet");
        return null;
    }
    static public void setString(String fileName, String key, String value) {
        setString(getFile(fileName), key, value);
    }

    static public void setString(File file, String key, String value) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            
            String[] data = PApplet.loadStrings(file);
            for (int i = 0; i < data.length; i++) {
                String[] keyValue = data[i].split(";");
                if (keyValue[0].equals(key)) {
                    data[i] = (key + ";" + value);
                    PApplet.saveStrings(file, data);
                    return;
                }
            }
            String[] newData = new String[data.length + 1];
            for (int i = 0; i < data.length; i++)
                newData[i] = data[i];
            newData[data.length] = (key + ";" + value);
            PApplet.saveStrings(file, newData);
            return;
        } else
            BtaUtilsMod.LOGGER.debug("Data manager not working on client yet");
        return;
    }

    static public void deleteString(File file, String key) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            int counter = 0;
            String[] data = PApplet.loadStrings(file);
            String[] newData = new String[data.length - 1];
            for (int i = 0; i < data.length; i++) {
                String[] keyValue = data[i].split(";");
                if (!keyValue[0].equals(key)) {
                    newData[counter] = data[i];
                    counter++;
                }
            }
            PApplet.saveStrings(file, newData);
            return;
        } else
            BtaUtilsMod.LOGGER.debug("Data manager not working on client yet");
        return;
    }

    static private File getFile(String fileName) {
        Path savePath = FabricLoader.getInstance().getGameDir()
                .resolve(MinecraftServer.getInstance().propertyManagerObj.getStringProperty("level-name", "-1"));
        return savePath.resolve("data").resolve(fileName).toFile();
    }

    static public File[] findFilesWithNameRecursively(String directoryPath, String fileName) {
        File directory = getFile(directoryPath);
        List<File> foundFiles = new ArrayList<>();
        findFilesRecursively(directory, fileName, foundFiles);
        return foundFiles.toArray(new File[0]);
    }

    static private void findFilesRecursively(File directory, String fileName, List<File> foundFiles) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // Recursively search in subdirectories
                        findFilesRecursively(file, fileName, foundFiles);
                    } else if (file.getName().equals(fileName)) {
                        foundFiles.add(file);
                    }
                }
            }
        }
    }
}
