package me.alpha432.oyvey.features.modules.render;

import java.awt.Color;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;

public class BlockHighlight extends Module {

    private final Setting lineWidth = this.register(new Setting("LineWidth", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(5.0F)));
    private final Setting cAlpha = this.register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));

    public BlockHighlight() {
        super("BlockHighlight", "Highlights the block u look at.", Module.Category.RENDER, false, false, false);
    }

    public void onRender3D(Render3DEvent event) {
        RayTraceResult ray = BlockHighlight.mc.objectMouseOver;

        if (ray != null && ray.typeOfHit == Type.BLOCK) {
            BlockPos blockpos = ray.getBlockPos();

            RenderUtil.drawBlockOutline(blockpos, ((Boolean) ClickGui.getInstance().rainbow.getValue()).booleanValue() ? ColorUtil.rainbow(((Integer) ClickGui.getInstance().rainbowHue.getValue()).intValue()) : new Color(((Integer) ClickGui.getInstance().red.getValue()).intValue(), ((Integer) ClickGui.getInstance().green.getValue()).intValue(), ((Integer) ClickGui.getInstance().blue.getValue()).intValue(), ((Integer) this.cAlpha.getValue()).intValue()), ((Float) this.lineWidth.getValue()).floatValue(), false);
        }

    }
}
