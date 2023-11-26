package de.olivermakesco.bta_utils;

import de.olivermakesco.bta_utils.config.BtaUtilsConfig;
import de.olivermakesco.bta_utils.server.DiscordChatRelay;
import de.olivermakesco.bta_utils.server.DiscordClient;
import de.olivermakesco.bta_utils.server.DiscordSlashHandler;
import de.olivermakesco.bta_utils.server.MinecraftStopIfNoPlayers;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class BtaUtilsMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("BTA Utils");

    @Override
    public void onInitialize() {
        Signal.handle(new Signal("INT"), new SignalHandler() {
            @Override
            public void handle(Signal signal) {
                LOGGER.info("Ctrl+C, stopping server!");
                MinecraftServer.getInstance().initiateShutdown();
            }
        });

        MinecraftStopIfNoPlayers.scheduleServerShutdownIfNoPlayers();

        new Thread(() -> {
            if (DiscordClient.init()) {
                if (!BtaUtilsConfig.discord_message_started.equals("")) 
                DiscordChatRelay.sendMessageAsBot(BtaUtilsConfig.discord_message_started);
                DiscordSlashHandler.initCommands(DiscordClient.jda);
                DiscordClient.jda.addEventListener(new DiscordSlashHandler());
            }
        }).start();
    }

    public static void info(String s) {
        LOGGER.info(s);
    }
}
