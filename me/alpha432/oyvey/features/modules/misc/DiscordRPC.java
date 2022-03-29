package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.RPC;
import me.alpha432.oyvey.features.modules.Module;

public class DiscordRPC extends Module {

    public DiscordRPC() {
        super("Discord RPC", "RPC", Module.Category.CLIENT, true, false, false);
    }

    public void onEnable() {
        RPC.startRPC();
    }

    public void onDisable() {
        RPC.stopRPC();
    }
}
