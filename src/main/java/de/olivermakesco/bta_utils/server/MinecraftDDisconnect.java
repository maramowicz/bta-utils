package de.olivermakesco.bta_utils.server;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.olivermakesco.bta_utils.core.DataManager;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.ServerCommand;
import net.minecraft.server.MinecraftServer;

public class MinecraftDDisconnect extends ServerCommand {

    public MinecraftDDisconnect(MinecraftServer server) {
        super(server, "mdunpair", new String[0]);
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
        String username = sender.getPlayer().username;
        if (username.contains(".") || username.contains(";") || username.contains("/") || username.contains("\\")) {
            sender.sendMessage(
                    "Did you know your username is Illegal? Yes, we are not wrong, the only characters you can use in Minecraft username are: abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_ so first change your username to normal, then try again.");
            return true;
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //if there one argument in args and ""confirm".equals(args[0])"
        if (args.length == 1 && "confirm".equals(args[0])) {
            executor.submit(() -> {
                try {
                    File[] files = DataManager.findFilesWithNameRecursively("perPlayer", "data");
                    for (int i = 0; i < files.length; i++) {
                        if (username.equals(DataManager.getString(files[i], "username"))) {
                            if ("false".equals(DataManager.getString(files[i], "connected"))) {
                                sender.sendMessage("this account is not paired with any Discord accounts!");
                                return false;
                            }
                            DataManager.setString(files[i], "connected", "false");
                            sender.sendMessage("Accounts unpaired!");
                            return false;
                        }
                    }
                } catch (Exception e) {
                    sender.sendMessage("Error while initiating the connection.");
                }
                return false;
            });
        }
        return false;
    }

    @Override
    public boolean opRequired(String[] var1) {
        return false;
    }

    @Override
    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
        sender.sendMessage("/mdunpair confirm");
    }
}
