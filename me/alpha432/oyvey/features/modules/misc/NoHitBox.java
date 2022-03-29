package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class NoHitBox extends Module {

    private static NoHitBox INSTANCE = new NoHitBox();
    public Setting pickaxe = this.register(new Setting("Pickaxe", Boolean.valueOf(true)));
    public Setting crystal = this.register(new Setting("Crystal", Boolean.valueOf(true)));
    public Setting gapple = this.register(new Setting("Gapple", Boolean.valueOf(true)));

    public NoHitBox() {
        super("NoHitBox", "NoHitBox.", Module.Category.MISC, false, false, false);
        this.setInstance();
    }

    public static NoHitBox getINSTANCE() {
        if (NoHitBox.INSTANCE == null) {
            NoHitBox.INSTANCE = new NoHitBox();
        }

        return NoHitBox.INSTANCE;
    }

    private void setInstance() {
        NoHitBox.INSTANCE = this;
    }
}
