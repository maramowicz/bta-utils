package de.olivermakesco.bta_utils.server;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class DiscordSlashHandler extends ListenerAdapter {
    public static void initCommands(JDA jda) {
        jda.updateCommands()
        .addCommands(Commands.slash("ping", "Gives the current ping"))
        .queue();
    }
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ping")) {
            event.reply("Pong! :D").queue();
        }
    }
}
