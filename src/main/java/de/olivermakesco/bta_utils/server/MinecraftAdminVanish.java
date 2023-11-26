package de.olivermakesco.bta_utils.server;

import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.ServerCommand;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.EntityTracker;

public class MinecraftAdminVanish extends ServerCommand {

    public MinecraftAdminVanish(MinecraftServer server) {
        super(server, "vanish", new String[0]);
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
        if (args[0].equals("on")) {
            MinecraftServer.getInstance().entityTracker[sender.getPlayer().dimension].untrackEntity(sender.getPlayer());
            sender.getPlayer().gamemode.areMobsHostile = false;
            sender.getPlayer().gamemode.isPlayerInvulnerable = true;
            sender.sendMessage("Vanished!");
            return true;
        } else if (args[0].equals("off")) {
            MinecraftServer.getInstance().entityTracker[sender.getPlayer().dimension].trackEntity(sender.getPlayer());
            sender.getPlayer().gamemode.areMobsHostile = true;
            sender.getPlayer().gamemode.isPlayerInvulnerable = false;
            sender.sendMessage("UnVanished!");
            return true;
        } else if (args[0].equals("status")) {
            if (sender.getPlayer().gamemode.areMobsHostile || !sender.getPlayer().gamemode.isPlayerInvulnerable)
                sender.sendMessage("You not vanised");
            else
                sender.sendMessage("You vanised");
            return true;
        }
        if (sender.getPlayer().gamemode.areMobsHostile || !sender.getPlayer().gamemode.isPlayerInvulnerable) {
            MinecraftServer.getInstance().entityTracker[sender.getPlayer().dimension].untrackEntity(sender.getPlayer());
            sender.getPlayer().gamemode.areMobsHostile = false;
            sender.getPlayer().gamemode.isPlayerInvulnerable = true;
            sender.sendMessage("Vanished!");
        } else {
            MinecraftServer.getInstance().entityTracker[sender.getPlayer().dimension].trackEntity(sender.getPlayer());
            sender.getPlayer().gamemode.areMobsHostile = true;
            sender.getPlayer().gamemode.isPlayerInvulnerable = false;
            sender.sendMessage("UnVanished!");
        }
        return true;
    }

    @Override
    public boolean opRequired(String[] var1) {
        return true;
    }

    @Override
    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
        sender.sendMessage("/vanish <on/off/status/switch (if empty)>");
    }
}
