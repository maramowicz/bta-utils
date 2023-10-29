package de.olivermakesco.bta_utils.server;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.ServerCommand;
import net.minecraft.server.MinecraftServer;

public class MinecraftDConnect extends ServerCommand {

    public MinecraftDConnect(MinecraftServer server) {
        super(server, "dconnect", new String[0]);
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                RestAction<User> action = DiscordClient.jda.retrieveUserById(args[0]);
                action.queue(user -> {
                    try {
                        // try {
                        //     sender.getPlayer().getEntityData().set("BTAUtilsConnectCodeExpireDate".hashCode(), (int)(System.currentTimeMillis() / 1000L + 180));
                        // } catch (NullPointerException e) {
                        //     sender.getPlayer().getEntityData().define("BTAUtilsConnectCodeExpireDate".hashCode(), (int)(System.currentTimeMillis() / 1000L + 180));
                        // } 
                        // System.out.println(1);
                        // try {
                        //     sender.getPlayer().getEntityData().set((int)("BTAUtilsConnectCode".hashCode()), getRandomHexString(32));
                        // } catch (NullPointerException e) {
                        //     sender.getPlayer().getEntityData().define((int)("BTAUtilsConnectCode".hashCode()), getRandomHexString(32));
                        // }
                        System.out.println(2);
                        sender.sendMessage("Connecting your Minecraft account with user " + user.getName() + ". Your code is " + sender.getPlayer().getEntityData().getInt("BTAUtilsConnectCode".hashCode()) + " and expires in next 3 minutes!");
                        System.out.println(3);
                    } catch (Exception e) {
                        sender.sendMessage("Error! Server... handled it! Crash? Not today ;)");
                        System.out.println(e);
                    }
                }, throwable -> sender.sendMessage("Unknown user, did you paste your Discord UserID?"));
            } catch (Exception e) {
                sender.sendMessage("Error while initiating the connection.");
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
        sender.sendMessage("/dconnect DiscordUserID");
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
