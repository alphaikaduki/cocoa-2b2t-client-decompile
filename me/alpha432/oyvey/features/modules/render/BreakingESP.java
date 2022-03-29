package me.alpha432.oyvey.features.modules.render;

import java.awt.Color;
import java.util.function.Predicate;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.RenderUtil2;

public class BreakingESP extends Module {

    public final Setting lineWidth = this.register(new Setting("LineWidth", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(5.0F), (object) -> {
        return ((Boolean) this.outline.getValue()).booleanValue();
    }));
    public final Setting boxAlpha = this.register(new Setting("BoxAlpha", Integer.valueOf(85), Integer.valueOf(0), Integer.valueOf(255), (object) -> {
        return ((Boolean) this.box.getValue()).booleanValue();
    }));
    public Setting red = this.register(new Setting("Red", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting green = this.register(new Setting("Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting blue = this.register(new Setting("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting box = this.register(new Setting("Box", Boolean.valueOf(true)));
    public Setting outline = this.register(new Setting("Outline", Boolean.valueOf(true)));

    public BreakingESP() {
        super("BreakingESP", "Renders a box on blocks being broken", Module.Category.RENDER, true, false, false);
    }

    public void onRender3D(Render3DEvent render3DEvent) {
        if (BreakingESP.mc.playerController.currentBlock != null) {
            Color color = new Color(((Integer) this.red.getValue()).intValue(), ((Integer) this.green.getValue()).intValue(), ((Integer) this.blue.getValue()).intValue(), ((Integer) this.boxAlpha.getValue()).intValue());

            RenderUtil2.boxESP(BreakingESP.mc.playerController.currentBlock, color, ((Float) this.lineWidth.getValue()).floatValue(), ((Boolean) this.outline.getValue()).booleanValue(), ((Boolean) this.box.getValue()).booleanValue(), ((Integer) this.boxAlpha.getValue()).intValue(), false);
        }

    }
}
