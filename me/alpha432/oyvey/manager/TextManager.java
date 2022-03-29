package me.alpha432.oyvey.manager;

import java.awt.Font;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.gui.font.CustomFont;
import me.alpha432.oyvey.features.modules.client.FontMod;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.util.math.MathHelper;

public class TextManager extends Feature {

    private final Timer idleTimer = new Timer();
    public int scaledWidth;
    public int scaledHeight;
    public int scaleFactor;
    private CustomFont customFont = new CustomFont(new Font("Verdana", 0, 17), true, false);
    private boolean idling;

    public TextManager() {
        this.updateResolution();
    }

    public void init(boolean startup) {
        FontMod cFont = (FontMod) OyVey.moduleManager.getModuleByClass(FontMod.class);

        try {
            this.setFontRenderer(new Font((String) cFont.fontName.getValue(), ((Integer) cFont.fontStyle.getValue()).intValue(), ((Integer) cFont.fontSize.getValue()).intValue()), ((Boolean) cFont.antiAlias.getValue()).booleanValue(), ((Boolean) cFont.fractionalMetrics.getValue()).booleanValue());
        } catch (Exception exception) {
            ;
        }

    }

    public void drawStringWithShadow(String text, float x, float y, int color) {
        this.drawString(text, x, y, color, true);
    }

    public void drawString(String text, float x, float y, int color, boolean shadow) {
        if (OyVey.moduleManager.isModuleEnabled(FontMod.getInstance().getName())) {
            if (shadow) {
                this.customFont.drawStringWithShadow(text, (double) x, (double) y, color);
            } else {
                this.customFont.drawString(text, x, y, color);
            }

        } else {
            TextManager.mc.fontRenderer.drawString(text, x, y, color, shadow);
        }
    }

    public int getStringWidth(String text) {
        return OyVey.moduleManager.isModuleEnabled(FontMod.getInstance().getName()) ? this.customFont.getStringWidth(text) : TextManager.mc.fontRenderer.getStringWidth(text);
    }

    public int getFontHeight() {
        if (OyVey.moduleManager.isModuleEnabled(FontMod.getInstance().getName())) {
            String text = "A";

            return this.customFont.getStringHeight(text);
        } else {
            return TextManager.mc.fontRenderer.FONT_HEIGHT;
        }
    }

    public void setFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        this.customFont = new CustomFont(font, antiAlias, fractionalMetrics);
    }

    public Font getCurrentFont() {
        return this.customFont.getFont();
    }

    public void updateResolution() {
        this.scaledWidth = TextManager.mc.displayWidth;
        this.scaledHeight = TextManager.mc.displayHeight;
        this.scaleFactor = 1;
        boolean flag = TextManager.mc.isUnicode();
        int i = TextManager.mc.gameSettings.guiScale;

        if (i == 0) {
            i = 1000;
        }

        while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }

        if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }

        double scaledWidthD = (double) (this.scaledWidth / this.scaleFactor);
        double scaledHeightD = (double) (this.scaledHeight / this.scaleFactor);

        this.scaledWidth = MathHelper.ceil(scaledWidthD);
        this.scaledHeight = MathHelper.ceil(scaledHeightD);
    }

    public String getIdleSign() {
        if (this.idleTimer.passedMs(500L)) {
            this.idling = !this.idling;
            this.idleTimer.reset();
        }

        return this.idling ? "_" : "";
    }

    public void drawStringSmall(String s, float x, float v, int rgb, boolean b) {}

    public void drawStringBig(String kuroHack_, float x, float v, int rgb, boolean b) {}
}
