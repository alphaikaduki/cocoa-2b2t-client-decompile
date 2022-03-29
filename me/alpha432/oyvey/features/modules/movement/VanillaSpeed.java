package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.MathUtil;

public class VanillaSpeed extends Module {

    public Setting speed = this.register(new Setting("Speed", Double.valueOf(1.0D), Double.valueOf(1.0D), Double.valueOf(20.0D)));

    public VanillaSpeed() {
        super("VanillaSpeed", "ec.me", Module.Category.MOVEMENT, true, false, false);
    }

    public void onUpdate() {
        if (VanillaSpeed.mc.player != null && VanillaSpeed.mc.world != null) {
            double[] calc = MathUtil.directionSpeed(((Double) this.speed.getValue()).doubleValue() / 10.0D);

            VanillaSpeed.mc.player.motionX = calc[0];
            VanillaSpeed.mc.player.motionZ = calc[1];
        }
    }
}
