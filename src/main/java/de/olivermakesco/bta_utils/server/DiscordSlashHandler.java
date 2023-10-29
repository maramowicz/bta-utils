package de.olivermakesco.bta_utils.server;

import java.io.File;

import de.olivermakesco.bta_utils.core.DataManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class DiscordSlashHandler extends ListenerAdapter {
    public static void initCommands(JDA jda) {
        jda.updateCommands()
                .addCommands(Commands.slash("ping", "Gives the current ping"))
                .addCommands(Commands
                        .slash("dconnect", "Allow you to connect account on Minecraft server with account on Discord")
                        .addOption(OptionType.STRING, "key", "connection key", true, false))
                .queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ping")) {
            event.reply("Pong! :D").queue();
        } else if (event.getName().equals("dconnect")) {
            String username = event.getUser().getName();
            if (username.contains(".") || username.contains(";") || username.contains("/") || username.contains("\\")) {
                event.reply(
                        "Sorry user, for security reasons all usernames with dots (.), semicolons (;), slashes (/) and backslashes (\\) has been blocked from pairing! Remove all illegal characters and try again.\nAlso if you change your username back to original we detect it and unpair accounts!")
                        .queue();
                return;
            }
            String key = event.getOption("key").getAsString();
            File[] files = DataManager.findFilesWithNameRecursively("perPlayer", "data");
            for (int i = 0; i < files.length; i++) {
                try {
                    if (DataManager.getString(files[i], "discordConnectionKey").equals(key) || ("confirm".equals(key)
                            && DataManager.getString(files[i], "discordUserID").equals(event.getUser().getId()))) {
                        if (!DataManager.getString(files[i], "discordUserID").equals(event.getUser().getId())) {
                            event.reply("I'm not waiting for you, Mr. " + event.getUser().getName()
                                    + ", go away and give me your own key!").queue();
                            return;
                        }
                        if (System.currentTimeMillis() / 1000L < Long
                                .parseLong(DataManager.getString(files[i], "discordConnectionKeyExpireDate"))) {
                            if (key.equals("confirm") && DataManager.getString(files[i], "phase").equals("2")) {
                                event.reply("And... connected! Welcome " + DataManager.getString(files[i], "username")
                                        + " or " + event.getUser().getName() + ", whatever you like ;)").queue();
                                DataManager.deleteString(files[i], "phase");
                                DataManager.deleteString(files[i], "discordConnectionKeyExpireDate");
                                DataManager.deleteString(files[i], "discordConnectionKey");
                                DataManager.setString(files[i], "connected", "true");
                                return;
                            }
                            if (DataManager.getString(files[i], "phase").equals(Integer.toString(1))) {
                                event.reply("Hi " + DataManager.getString(files[i], "username")
                                        + "! Did you want to connect your Minecraft account with your Discord account? If yes then confirm it in next 30 seconds with /dconnect confirm")
                                        .queue();
                                DataManager.setString(files[i], "phase", Integer.toString(2));
                                DataManager.setString(files[i], "discordConnectionKeyExpireDate",
                                        Long.toString(System.currentTimeMillis() / 1000L + 30));
                                return;
                            } else {
                                event.reply("You probably sended same key two times you know?").queue();
                                return;
                            }
                        } else {
                            event.reply("Sorry, your key expired, please back to Minecraft and generate new key")
                                    .queue();
                            DataManager.setString(files[i], "phase", Integer.toString(0));
                        }
                    }
                } catch (NullPointerException e) {

                }
            }
            if (key.equals("confirm")) {
                event.reply("Confirming the confirmation of your unconfirmed confirmation.").queue();
            }
            event.reply("Cannot find any request with that ID, did you rewrted your ID correctly?").queue();
        }
    }
}
