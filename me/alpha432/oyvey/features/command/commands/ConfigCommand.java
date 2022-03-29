package me.alpha432.oyvey.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.command.Command;

public class ConfigCommand extends Command {

    public ConfigCommand() {
        super("config", new String[] { "<save/load>"});
    }

    public void execute(String[] commands) {
        if (commands.length == 1) {
            sendMessage("You`ll find the config files in your gameProfile directory under oyvey/config");
        } else {
            String configs;

            if (commands.length == 2) {
                if ("list".equals(commands[0])) {
                    configs = "Configs: ";
                    File file = new File("crepe/");
                    List directories = (List) Arrays.stream(file.listFiles()).filter(File::isDirectory).filter((f) -> {
                        return !f.getName().equals("util");
                    }).collect(Collectors.toList());
                    StringBuilder builder = new StringBuilder(configs);
                    Iterator iterator = directories.iterator();

                    while (iterator.hasNext()) {
                        File file1 = (File) iterator.next();

                        builder.append(file1.getName() + ", ");
                    }

                    configs = builder.toString();
                    sendMessage(configs);
                } else {
                    sendMessage("Not a valid command... Possible usage: <list>");
                }
            }

            if (commands.length >= 3) {
                configs = commands[0];
                byte file1 = -1;

                switch (configs.hashCode()) {
                case 3522941:
                    if (configs.equals("save")) {
                        file1 = 0;
                    }

                default:
                    switch (file1) {
                    case 0:
                        OyVey.configManager.saveConfig(commands[1]);
                        sendMessage(ChatFormatting.GREEN + "Config \'" + commands[1] + "\' has been saved.");
                        return;

                    default:
                        sendMessage("Not a valid command... Possible usage: <save/load>");
                    }
                }
            }

        }
    }
}
