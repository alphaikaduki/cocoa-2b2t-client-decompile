package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;

public class ReverseStep extends Module {

    public ReverseStep() {
        super("ReverseStep", "Screams chinese words and teleports you", Module.Category.MOVEMENT, true, false, false);
    }

    public void onUpdate() {
        if (ReverseStep.mc.player.onGround) {
            --ReverseStep.mc.player.motionY;
        }

    }
}
