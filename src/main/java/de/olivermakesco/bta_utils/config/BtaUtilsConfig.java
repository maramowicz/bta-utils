package de.olivermakesco.bta_utils.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.olivermakesco.bta_utils.BtaUtilsMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class BtaUtilsConfig {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static boolean useSdl = false;
    public static boolean disableTrample = false;
    public static boolean discord_enable = false;
    public static String discord_token = "SUPER SECRET TOKEN";
    public static String discord_channel = "CHANNEL ID";
    public static boolean discord_replace_messages = false;
    public static String discord_status = "Server running!";
    public static String discord_message_started = "**Server Started**";
    public static String discord_message_stopped = "**Server Stopped**";
    public static int as_wait_delay = 0;
    public static int as_shutdown_delay = 0;

    public static void load() {
        File file = getFilePath();

        if (!file.exists()) {
            initFile(file);
        }

        try {
            FileReader reader = new FileReader(file);
            JsonObject obj = GSON.fromJson(reader, JsonObject.class);
            reader.close();

            updateValues(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        save();
    }

    public static void save() {
        File file = getFilePath();
        JsonObject obj = new JsonObject();
        updateValues(obj);

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(GSON.toJson(obj));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initFile(File file) {
        try {
            boolean ignore = file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write("{}");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T get(JsonObject object, String key, T defaultValue) {
        JsonElement element = object.get(key);
        if (element == null) {
            object.add(key, GSON.toJsonTree(defaultValue));
            return defaultValue;
        }
        return GSON.fromJson(element, (Class<T>)defaultValue.getClass());
    }

    public static void updateValues(JsonObject object) {
        useSdl = get(object, "use_sdl", useSdl);
        disableTrample = get(object, "disable_trample", disableTrample);

        /* discord = */ {
            JsonObject discord = get(object, "discord", new JsonObject());
            discord_enable = get(discord, "enable", discord_enable);
            discord_token = get(discord, "token", discord_token);
            discord_channel = get(discord, "channel", discord_channel);
            discord_replace_messages = get(discord, "replace_users_messages", discord_replace_messages);
            discord_status = get(discord, "discord_status", discord_status);
            discord_message_started = get(discord, "message_started", discord_message_started);
            discord_message_stopped = get(discord, "message_stopped", discord_message_stopped);

            object.add("discord", discord);
        }
        /* auto shutdown = */ {
            JsonObject auto_shutdown = get(object, "auto_shutdown", new JsonObject());
            as_wait_delay = get(auto_shutdown, "wait_delay_seconds_0_to_off", as_wait_delay);
            as_shutdown_delay = get(auto_shutdown, "shutdown_delay_minutes", as_shutdown_delay);
        
            object.add("auto_shutdown", auto_shutdown);
        }
    }

    public static File getFilePath() {
        return FabricLoader.getInstance().getConfigDir().resolve("bta_utils.json").toFile();
    }

    public static void printConfigValues() {
        BtaUtilsMod.info("use_sdl = " + useSdl);
        BtaUtilsMod.info("disable_trample = " + disableTrample);
        BtaUtilsMod.info("discord.enable = " + discord_enable);
        BtaUtilsMod.info("discord.replace_users_messages = " + discord_replace_messages);
        if (as_wait_delay == 0) {
            BtaUtilsMod.info("auto_shutdown=off");
        } else {
            BtaUtilsMod.info("auto_shutdown.wait_delay = " + as_wait_delay);
            BtaUtilsMod.info("auto_shutdown.shutdown_delay = " + as_shutdown_delay);
        }
    }

    static {
        load();
    }
}
