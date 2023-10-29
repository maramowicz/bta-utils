package de.olivermakesco.bta_utils;

import de.olivermakesco.bta_utils.config.BtaUtilsConfig;
import de.olivermakesco.bta_utils.server.DiscordChatRelay;
import de.olivermakesco.bta_utils.server.DiscordClient;
import de.olivermakesco.bta_utils.server.DiscordSlashHandler;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BtaUtilsMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("BTA Utils");

    @Override
    public void onInitialize() {
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
