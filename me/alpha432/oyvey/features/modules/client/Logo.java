package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.Util;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Logo extends Module {

    public static final ResourceLocation mark = new ResourceLocation("textures/CrepeLogo1.png");
    public Setting imageX = this.register(new Setting("logoX", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(300)));
    public Setting imageY = this.register(new Setting("logoY", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(300)));
    public Setting imageWidth = this.register(new Setting("logoWidth", Integer.valueOf(97), Integer.valueOf(0), Integer.valueOf(1000)));
    public Setting imageHeight = this.register(new Setting("logoHeight", Integer.valueOf(97), Integer.valueOf(0), Integer.valueOf(1000)));
    private int color;

    public Logo() {
        super("Logo", "Puts a logo there (there)", Module.Category.CLIENT, false, false, false);
    }

    public void renderLogo() {
        int width = ((Integer) this.imageWidth.getValue()).intValue();
        int height = ((Integer) this.imageHeight.getValue()).intValue();
        int x = ((Integer) this.imageX.getValue()).intValue();
        int y = ((Integer) this.imageY.getValue()).intValue();

        Util.mc.renderEngine.bindTexture(Logo.mark);
        GlStateManager.color(255.0F, 255.0F, 255.0F);
        Gui.drawScaledCustomSizeModalRect(x - 2, y - 36, 7.0F, 7.0F, width - 7, height - 7, width, height, (float) width, (float) height);
    }

    public void onRender2D(Render2DEvent event) {
        if (!Feature.fullNullCheck()) {
            int width = this.renderer.scaledWidth;
            int height = this.renderer.scaledHeight;

            this.color = ColorUtil.toRGBA(((Integer) ClickGui.getInstance().red.getValue()).intValue(), ((Integer) ClickGui.getInstance().green.getValue()).intValue(), ((Integer) ClickGui.getInstance().blue.getValue()).intValue());
            if (((Boolean) this.enabled.getValue()).booleanValue()) {
                this.renderLogo();
            }
        }

    }
}
