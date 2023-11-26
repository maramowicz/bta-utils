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
        MinecraftServer.getInstance().entityTracker[sender.getPlayer().dimension].untrackEntity(sender.getPlayer());
        sender.sendMessage("Vanished!");
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
