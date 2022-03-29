package me.alpha432.oyvey.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Iterator;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.manager.FriendManager;

public class FriendCommand extends Command {

    public FriendCommand() {
        super("friend", new String[] { "<add/del/name/clear>", "<name>"});
    }

    public void execute(String[] commands) {
        String f;

        if (commands.length != 1) {
            byte b0;

            if (commands.length == 2) {
                f = commands[0];
                b0 = -1;
                switch (f.hashCode()) {
                case 108404047:
                    if (f.equals("reset")) {
                        b0 = 0;
                    }

                default:
                    switch (b0) {
                    case 0:
                        OyVey.friendManager.onLoad();
                        sendMessage("Friends got reset.");
                        return;

                    default:
                        sendMessage(commands[0] + (OyVey.friendManager.isFriend(commands[0]) ? " is friended." : " isn\'t friended."));
                    }
                }
            } else {
                if (commands.length >= 2) {
                    f = commands[0];
                    b0 = -1;
                    switch (f.hashCode()) {
                    case 96417:
                        if (f.equals("add")) {
                            b0 = 0;
                        }
                        break;

                    case 99339:
                        if (f.equals("del")) {
                            b0 = 1;
                        }
                    }

                    switch (b0) {
                    case 0:
                        OyVey.friendManager.addFriend(commands[1]);
                        sendMessage(ChatFormatting.GREEN + commands[1] + " has been friended");
                        return;

                    case 1:
                        OyVey.friendManager.removeFriend(commands[1]);
                        sendMessage(ChatFormatting.RED + commands[1] + " has been unfriended");
                        return;

                    default:
                        sendMessage("Unknown Command, try friend add/del (name)");
                    }
                }

            }
        } else {
            if (OyVey.friendManager.getFriends().isEmpty()) {
                sendMessage("Friend list empty D:.");
            } else {
                f = "Friends: ";
                Iterator iterator = OyVey.friendManager.getFriends().iterator();

                while (iterator.hasNext()) {
                    FriendManager.Friend friend = (FriendManager.Friend) iterator.next();

                    try {
                        f = f + friend.getUsername() + ", ";
                    } catch (Exception exception) {
                        ;
                    }
                }

                sendMessage(f);
            }

        }
    }
}
