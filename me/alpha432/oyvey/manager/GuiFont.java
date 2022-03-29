package me.alpha432.oyvey.manager;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Locale;
import me.alpha432.oyvey.features.gui.CustomFont;
import me.alpha432.oyvey.util.Globals;
import me.alpha432.oyvey.util.Rainbow;
import me.alpha432.oyvey.util.util2.RenderUtil;

public class GuiFont implements Globals {

    private final String[] fonts;
    public String fontName;
    public int fontSize;
    private CustomFont font;

    public GuiFont() {
        this.fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(Locale.ENGLISH);
        this.fontName = "Tahoma";
        this.fontSize = 16;
        this.font = new CustomFont(new Font(this.fontName, 0, this.fontSize), true, false);
    }

    public void setFont() {
        this.font = new CustomFont(new Font(this.fontName, 0, this.fontSize), true, false);
    }

    public void reset() {
        this.setFont("Tahoma");
        this.setFontSize(16);
        this.setFont();
    }

    public boolean setFont(String fontName) {
        String[] astring = this.fonts;
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String font = astring[j];

            if (fontName.equalsIgnoreCase(font)) {
                this.fontName = font;
                this.setFont();
                return true;
            }
        }

        return false;
    }

    public void setFontSize(int size) {
        this.fontSize = size;
        this.setFont();
    }

    public String setRandomFont() {
        this.fontName = RenderUtil.getRandomFont();
        this.setFont();
        return this.fontName;
    }

    public void drawStringWithShadow(String string, float x, float y, int colour) {
        this.drawString(string, x, y, colour, true);
    }

    public float drawString(String string, float x, float y, int colour, boolean shadow) {
        return shadow ? this.font.drawStringWithShadow(string, (double) x, (double) y, colour) : this.font.drawString(string, x, y, colour);
    }

    public void drawStringRainbow(String string, float x, float y, boolean shadow) {
        if (shadow) {
            this.font.drawStringWithShadow(string, (double) x, (double) y, Rainbow.getColour().getRGB());
        } else {
            this.font.drawString(string, x, y, Rainbow.getColour().getRGB());
        }

    }

    public int getTextHeight() {
        return this.font.getStringHeight();
    }

    public int getTextWidth(String string) {
        return this.font.getStringWidth(string);
    }
}
