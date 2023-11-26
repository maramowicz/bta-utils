package de.olivermakesco.bta_utils.core;

import java.io.File;
import java.util.ArrayList;

import com.badlogic.gdx.utils.Array;

public class PlayerManager {
    ArrayList<Player> players = new ArrayList<Player>();
    public PlayerManager() {
        init();
    }

    public void init() {
        File files[] = DataManager.findFilesWithNameRecursively("perPlayer", "data");
        for (File file : files) {
            Player player = new Player(file);
            players.add(player);
        }
    }

}
//Create new class Player containing username, isConnected and discordID
class Player {
    public String username;
    public boolean isConnected;
    public String discordID;
    File file;
    public Player(String username, boolean isConnected, String discordID) {
        this.username = username;
        this.isConnected = isConnected;
        this.discordID = discordID;
    }
    public Player(File file) {
        this.file = file;
        this.username = DataManager.getString(file, "username");
        this.isConnected = stringToBoolean(DataManager.getString(file, "connected"));
        this.discordID = DataManager.getString(file, "discordID");
    }


    boolean stringToBoolean(String s) {
        return s.equals("true");
    }
}