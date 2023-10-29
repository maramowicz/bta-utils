//Thanks for https://github.com/Apollointhehouse/TimerCommand for Kotlin example

package de.olivermakesco.bta_utils.server;

import net.minecraft.core.net.command.ServerCommand;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.server.MinecraftServer;
import turniplabs.halplibe.helper.CommandHelper;

public class MinecraftPingHandler extends ServerCommand {

    public MinecraftPingHandler(MinecraftServer server) {
        super(server, "ping", new String[0]);
    }

    public void initCommands() {
        CommandHelper.createCommand(this);
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
        sender.sendMessage("Pong! ;)");
        return true;
    }

    @Override
    public boolean opRequired(String[] args) {
        return false;
    }

    @Override
    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
        sender.sendMessage("/ping");
    }
}
