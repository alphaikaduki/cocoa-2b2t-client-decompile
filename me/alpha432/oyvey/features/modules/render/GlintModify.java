package me.alpha432.oyvey.features.modules.render;

import java.awt.Color;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class GlintModify extends Module {

    public Setting red = this.register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting green = this.register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting blue = this.register(new Setting("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting rainbow = this.register(new Setting("Rainbow", Boolean.valueOf(false)));

    public GlintModify() {
        super("GlintModify", "Changes the enchant glint color", Module.Category.RENDER, true, false, true);
    }

    public static Color getColor(long offset, float fade) {
        float hue = (float) (System.nanoTime() + offset) / 1.0E10F % 1.0F;
        long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0F, 1.0F)), 16);
        Color c = new Color((int) color);

        return new Color((float) c.getRed() / 255.0F * fade, (float) c.getGreen() / 255.0F * fade, (float) c.getBlue() / 255.0F * fade, (float) c.getAlpha() / 255.0F);
    }

    public void onUpdate() {
        if (((Boolean) this.rainbow.getValue()).booleanValue()) {
            this.cycleRainbow();
        }

    }

    public void cycleRainbow() {
        float[] tick_color = new float[] { (float) (System.currentTimeMillis() % 11520L) / 11520.0F};
        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8F, 0.8F);

        this.red.setValue(Integer.valueOf(color_rgb_o >> 16 & 255));
        this.green.setValue(Integer.valueOf(color_rgb_o >> 8 & 255));
        this.blue.setValue(Integer.valueOf(color_rgb_o & 255));
    }
}
