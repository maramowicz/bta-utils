package de.olivermakesco.bta_utils.mixin.server;

import de.olivermakesco.bta_utils.server.MinecraftDConnect;
import de.olivermakesco.bta_utils.server.MinecraftPingHandler;
import net.minecraft.core.net.command.Commands;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Commands.class, remap = false)
public class Mixin_Commands {

    @Inject(
            method = "initServerCommands",
            at = @At("RETURN")
    )
    private static void postInitServerCommands(MinecraftServer server, CallbackInfo ci) {
        MinecraftPingHandler pingHandler = new MinecraftPingHandler(server);
        Commands.commands.add(pingHandler);
        MinecraftDConnect connectionHandler = new MinecraftDConnect(server);
        Commands.commands.add(connectionHandler);
    }
}