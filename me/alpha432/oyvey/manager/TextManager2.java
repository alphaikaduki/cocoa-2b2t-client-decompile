package me.alpha432.oyvey.manager;

import java.awt.Color;
import java.awt.Font;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.gui.font.CustomFont;
import me.alpha432.oyvey.features.modules.client.FontMod;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.util.math.MathHelper;

public abstract class TextManager2 extends Feature {

    private final Timer idleTimer = new Timer();
    private final CustomFont headerFont = new CustomFont(new Font("Tahoma", 1, 40), true, false);
    private final CustomFont smallString = new CustomFont(new Font("tahoma", 1, 10), true, false);
    public int scaledWidth;
    public int scaledHeight;
    public int scaleFactor;
    private CustomFont customFont = new CustomFont(new Font("Verdana", 0, 17), true, false);
    private boolean idling;

    public TextManager2() {
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

    public float drawString(String text, float x, float y, int color, boolean shadow) {
        return shadow ? this.customFont.drawStringWithShadow(text, (double) x, (double) y, color) : this.customFont.drawString(text, x, y, color);
    }

    public void drawRainbowString(String text, float x, float y, int startColor, float factor, boolean shadow) {
        Color currentColor = new Color(startColor);
        float hueIncrement = 1.0F / factor;
        String[] rainbowStrings = text.split("§.");
        float currentHue = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), (float[]) null)[0];
        float saturation = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), (float[]) null)[1];
        float brightness = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), (float[]) null)[2];
        int currentWidth = 0;
        boolean shouldRainbow = true;
        boolean shouldContinue = false;

        for (int i = 0; i < text.length(); ++i) {
            char currentChar = text.charAt(i);
            char nextChar = text.charAt(MathUtil.clamp(i + 1, 0, text.length() - 1));

            if ((String.valueOf(currentChar) + nextChar).equals("§r")) {
                shouldRainbow = false;
            } else if ((String.valueOf(currentChar) + nextChar).equals("§+")) {
                shouldRainbow = true;
            }

            if (shouldContinue) {
                shouldContinue = false;
            } else {
                if ((String.valueOf(currentChar) + nextChar).equals("§r")) {
                    String escapeString = text.substring(i);

                    this.drawString(escapeString, x + (float) currentWidth, y, Color.WHITE.getRGB(), shadow);
                    break;
                }

                this.drawString(String.valueOf(currentChar).equals("§") ? "" : String.valueOf(currentChar), x + (float) currentWidth, y, shouldRainbow ? currentColor.getRGB() : Color.WHITE.getRGB(), shadow);
                if (String.valueOf(currentChar).equals("§")) {
                    shouldContinue = true;
                }

                currentWidth += this.getStringWidth(String.valueOf(currentChar));
                if (!String.valueOf(currentChar).equals(" ")) {
                    currentColor = new Color(Color.HSBtoRGB(currentHue, saturation, brightness));
                    currentHue += hueIncrement;
                }
            }
        }

    }

    public int getStringWidth(String text) {
        return this.customFont.getStringWidth(text);
    }

    public int getFontHeight() {
        String text = "A";

        return this.customFont.getStringHeight(text);
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
        boolean flag = TextManager2.mc.isUnicode();
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

        double scaledWidthD = (double) this.scaledWidth / (double) this.scaleFactor;
        double scaledHeightD = (double) this.scaledHeight / (double) this.scaleFactor;

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

    public float drawStringBig(String string, float x, float y, int colour, boolean shadow) {
        return shadow ? this.headerFont.drawStringWithShadow(string, (double) x, (double) y, colour) : this.headerFont.drawString(string, x, y, colour);
    }

    public float drawStringSmall(String string, float x, float y, int colour, boolean shadow) {
        return shadow ? this.smallString.drawStringWithShadow(string, (double) x, (double) y, colour) : this.smallString.drawString(string, x, y, colour);
    }
}
