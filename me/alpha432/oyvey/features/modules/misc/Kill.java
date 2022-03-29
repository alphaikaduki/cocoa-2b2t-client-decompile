package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;

public class Kill extends Module {

    public Kill() {
        super("/Kill", "Ez Spam if you have bariton", Module.Category.MISC, true, false, false);
    }

    public void onEnable() {
        Kill.mc.player.sendChatMessage("/kill");
        Kill.mc.player.sendChatMessage("kill");
        Command.sendMessage("Kill");
        this.disable();
    }
}
