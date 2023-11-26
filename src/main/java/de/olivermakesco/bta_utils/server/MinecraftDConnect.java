package de.olivermakesco.bta_utils.server;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.olivermakesco.bta_utils.core.DataManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.ServerCommand;
import net.minecraft.server.MinecraftServer;

public class MinecraftDConnect extends ServerCommand {

    public MinecraftDConnect(MinecraftServer server) {
        super(server, "mdpair", new String[0]);
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
        String username = sender.getPlayer().username;
        if (username.contains(".")||username.contains(";")||username.contains("/")||username.contains("\\")) {
            sender.sendMessage("Did you know your username is Illegal? Yes, we are not wrong, the only characters you can use in Minecraft username are: abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_ so first change your username to normal, then try again.");
            return false;
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                RestAction<User> action = DiscordClient.jda.retrieveUserById(args[0]);
                action.queue(user -> {
                    try {
                        String file = "perPlayer/" + sender.getPlayer().username + "/data";
                        DataManager.initDataFile(file);
                        DataManager.setString(file, "discordConnectionKeyExpireDate", Long.toString(System.currentTimeMillis()/1000L+180));
                        DataManager.setString(file, "discordConnectionKey", getRandomHexString(32));
                        DataManager.setString(file, "username", sender.getPlayer().username);
                        DataManager.setString(file, "discordUserID", args[0]);
                        DataManager.setString(file, "phase", Integer.toString(1));
                        DataManager.setString(file, "connected", "false");
                        sender.sendMessage("Connecting your Minecraft account with user " + user.getName() + ". Your code is " + DataManager.getString(file, "discordConnectionKey") + " and expires in next 3 minutes!");
                    } catch (Exception e) {
                        sender.sendMessage("Error! Server... handled it! Crash? Not today ;)");
                        System.out.println(e);
                    }
                }, throwable -> sender.sendMessage("Unknown user, did you paste your Discord UserID?"));
            } catch (Exception e) {
                sender.sendMessage("Usage: /mdpair DiscordID");
            }
        });
        return true;
    }

    @Override
    public boolean opRequired(String[] var1) {
        return false;
    }

    @Override
    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
        sender.sendMessage("/mdpair DiscordUserID");
    }

    private String getRandomHexString(int numchars){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }
    
}
