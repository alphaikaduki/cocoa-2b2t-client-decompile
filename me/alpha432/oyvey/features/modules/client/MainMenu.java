package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class MainMenu extends Module {

    public static MainMenu INSTANCE;
    public Setting mainScreen = this.register(new Setting("MainScreen", Boolean.valueOf(true)));

    public MainMenu() {
        super("Menu", "Controls custom screens used by the client", Module.Category.CLIENT, true, false, false);
        MainMenu.INSTANCE = this;
    }

    public void onTick() {}
}
