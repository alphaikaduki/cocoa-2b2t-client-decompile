package me.alpha432.oyvey.features.modules.render;

import java.util.Random;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class AimBug extends Module {

    private final Random random = new Random();
    private final Setting turn = this.register(new Setting("cocoaaaa", Boolean.valueOf(true)));

    public AimBug() {
        super("AimBug", "Stop servers attempting to kick u for being AFK.", Module.Category.PLAYER, true, false, false);
    }

    public void onUpdate() {
        if (AimBug.mc.player.ticksExisted % 1 == 0 && ((Boolean) this.turn.getValue()).booleanValue()) {
            AimBug.mc.player.rotationYaw = (float) (this.random.nextInt(360) + -31 - 90 - 10);
        }

    }
}
