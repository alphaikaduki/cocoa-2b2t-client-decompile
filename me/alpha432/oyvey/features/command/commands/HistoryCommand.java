package me.alpha432.oyvey.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.util.PlayerUtil;

public class HistoryCommand extends Command {

    public HistoryCommand() {
        super("history", new String[] { "<player>"});
    }

    public void execute(String[] commands) {
        if (commands.length == 1 || commands.length == 0) {
            sendMessage(ChatFormatting.RED + "Please specify a player.");
        }

        UUID uuid;

        try {
            uuid = PlayerUtil.getUUIDFromName(commands[0]);
        } catch (Exception exception) {
            sendMessage("An error occured.");
            return;
        }

        List names;

        try {
            names = PlayerUtil.getHistoryOfNames(uuid);
        } catch (Exception exception1) {
            sendMessage("An error occured.");
            return;
        }

        if (names != null) {
            sendMessage(commands[0] + "Â´s name history:");
            Iterator e = names.iterator();

            while (e.hasNext()) {
                String name = (String) e.next();

                sendMessage(name);
            }
        } else {
            sendMessage("No names found.");
        }

    }
}
