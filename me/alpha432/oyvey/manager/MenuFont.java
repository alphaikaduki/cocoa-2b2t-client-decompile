package me.alpha432.oyvey.manager;

import java.awt.Font;
import me.alpha432.oyvey.features.gui.CustomFont;
import me.alpha432.oyvey.util.Globals;

public class MenuFont implements Globals {

    private final CustomFont menuFont = new CustomFont(new Font("Tahoma", 1, 21), true, false);
    private final CustomFont headerFont = new CustomFont(new Font("Tahoma", 1, 41), true, false);

    public void drawStringWithShadow(String string, float x, float y, int colour) {
        this.drawString(string, x, y, colour, true);
    }

    public float drawString(String string, float x, float y, int colour, boolean shadow) {
        return shadow ? this.menuFont.drawStringWithShadow(string, (double) x, (double) y, colour) : this.menuFont.drawString(string, x, y, colour);
    }

    public float drawStringBig(String string, float x, float y, int colour, boolean shadow) {
        return shadow ? this.headerFont.drawStringWithShadow(string, (double) x, (double) y, colour) : this.headerFont.drawString(string, x, y, colour);
    }

    public int getTextHeight() {
        return this.menuFont.getStringHeight();
    }

    public int getTextWidth(String string) {
        return this.menuFont.getStringWidth(string);
    }
}
