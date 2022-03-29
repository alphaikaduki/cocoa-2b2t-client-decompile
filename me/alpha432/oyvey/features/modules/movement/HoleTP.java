package me.alpha432.oyvey.features.modules.movement;

import java.util.Comparator;
import java.util.function.Function;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.util.math.BlockPos;

public class HoleTP extends Module {

    private final Setting range = this.register(new Setting("Range", Float.valueOf(0.5F), Float.valueOf(0.1F), Float.valueOf(10.0F)));

    public HoleTP() {
        super("HoleTP", "like TP", Module.Category.MOVEMENT, true, false, false);
    }

    public void onUpdate() {
        BlockPos hole = (BlockPos) OyVey.holeManager.calcHoles().stream().min(Comparator.comparing((p) -> {
            return Double.valueOf(HoleTP.mc.player.getDistance((double) p.getX(), (double) p.getY(), (double) p.getZ()));
        })).orElse((BlockPos) null);

        if (hole != null && HoleTP.mc.player.getDistance((double) hole.getX(), (double) hole.getY(), (double) hole.getZ()) < (double) ((Float) this.range.getValue()).floatValue() + 1.5D) {
            HoleTP.mc.player.setPosition((double) hole.getX() + 0.5D, (double) hole.getY(), (double) hole.getZ() + 0.5D);
            HoleTP.mc.player.setPosition((double) hole.getX() + 0.5D, (double) hole.getY(), (double) hole.getZ() + 0.5D);
            this.disable();
        }

    }
}
