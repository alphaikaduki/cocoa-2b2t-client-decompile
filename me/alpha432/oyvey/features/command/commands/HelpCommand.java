package me.alpha432.oyvey.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Iterator;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.command.Command;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help");
    }

    public void execute(String[] commands) {
        sendMessage("Commands: ");
        Iterator iterator = OyVey.commandManager.getCommands().iterator();

        while (iterator.hasNext()) {
            Command command = (Command) iterator.next();

            sendMessage(ChatFormatting.GRAY + OyVey.commandManager.getPrefix() + command.getName());
        }

    }
}
