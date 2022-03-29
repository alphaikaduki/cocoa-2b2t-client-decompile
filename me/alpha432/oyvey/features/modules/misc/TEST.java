package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.Timerm;

public class TEST extends Module {

    private final Timerm timer = new Timerm();
    private Setting custom = this.register(new Setting("Custom", "Crepe Client On Top!!!"));

    public TEST() {
        super("AWA", "testmodule", Module.Category.MISC, true, false, false);
    }

    public void onEnable() {
        TEST.mc.player.sendChatMessage((String) this.custom.getValue());
        this.disable();
    }
}
