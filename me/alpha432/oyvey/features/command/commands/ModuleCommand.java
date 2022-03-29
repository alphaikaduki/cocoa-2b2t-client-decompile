package me.alpha432.oyvey.features.command.commands;

import com.google.gson.JsonParser;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Iterator;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.manager.ConfigManager;

public class ModuleCommand extends Command {

    public ModuleCommand() {
        super("module", new String[] { "<module>", "<set/reset>", "<setting>", "<value>"});
    }

    public void execute(String[] commands) {
        if (commands.length == 1) {
            sendMessage("Modules: ");
            Iterator module1 = OyVey.moduleManager.getCategories().iterator();

            while (module1.hasNext()) {
                Module.Category jp2 = (Module.Category) module1.next();
                String e1 = jp2.getName() + ": ";

                Module module1;

                for (Iterator iterator = OyVey.moduleManager.getModulesByCategory(jp2).iterator(); iterator.hasNext(); e1 = e1 + (module1.isEnabled() ? ChatFormatting.GREEN : ChatFormatting.RED) + module1.getName() + ChatFormatting.WHITE + ", ") {
                    module1 = (Module) iterator.next();
                }

                sendMessage(e1);
            }

        } else {
            Module module = OyVey.moduleManager.getModuleByDisplayName(commands[0]);

            if (module == null) {
                module = OyVey.moduleManager.getModuleByName(commands[0]);
                if (module == null) {
                    sendMessage("This module doesnt exist.");
                } else {
                    sendMessage(" This is the original name of the module. Its current name is: " + module.getDisplayName());
                }
            } else {
                Setting e;
                Iterator jp1;

                if (commands.length == 2) {
                    sendMessage(module.getDisplayName() + " : " + module.getDescription());
                    jp1 = module.getSettings().iterator();

                    while (jp1.hasNext()) {
                        e = (Setting) jp1.next();
                        sendMessage(e.getName() + " : " + e.getValue() + ", " + e.getDescription());
                    }

                } else if (commands.length == 3) {
                    if (commands[1].equalsIgnoreCase("set")) {
                        sendMessage("Please specify a setting.");
                    } else if (commands[1].equalsIgnoreCase("reset")) {
                        jp1 = module.getSettings().iterator();

                        while (jp1.hasNext()) {
                            e = (Setting) jp1.next();
                            e.setValue(e.getDefaultValue());
                        }
                    } else {
                        sendMessage("This command doesnt exist.");
                    }

                } else if (commands.length == 4) {
                    sendMessage("Please specify a value.");
                } else {
                    Setting setting;

                    if (commands.length == 5 && (setting = module.getSettingByName(commands[2])) != null) {
                        JsonParser jp = new JsonParser();

                        if (setting.getType().equalsIgnoreCase("String")) {
                            setting.setValue(commands[3]);
                            sendMessage(ChatFormatting.DARK_GRAY + module.getName() + " " + setting.getName() + " has been set to " + commands[3] + ".");
                            return;
                        }

                        try {
                            if (setting.getName().equalsIgnoreCase("Enabled")) {
                                if (commands[3].equalsIgnoreCase("true")) {
                                    module.enable();
                                }

                                if (commands[3].equalsIgnoreCase("false")) {
                                    module.disable();
                                }
                            }

                            ConfigManager.setValueFromJson(module, setting, jp.parse(commands[3]));
                        } catch (Exception exception) {
                            sendMessage("Bad Value! This setting requires a: " + setting.getType() + " value.");
                            return;
                        }

                        sendMessage(ChatFormatting.GRAY + module.getName() + " " + setting.getName() + " has been set to " + commands[3] + ".");
                    }

                }
            }
        }
    }
}
