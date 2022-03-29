package me.alpha432.oyvey.manager;

import java.awt.Font;
import me.alpha432.oyvey.features.gui.custom.Globals;
import me.alpha432.oyvey.features.gui.font.CustomFont;

public class DonatorFont implements Globals {

    private final String fontName = "Tahoma";
    private final int smallSize = 15;
    private final int mediumSize = 19;
    private final int largeSize = 24;
    private final CustomFont smallFont = new CustomFont(new Font("Tahoma", 0, 15), true, false);
    private final CustomFont mediumFont = new CustomFont(new Font("Tahoma", 0, 19), true, false);
    private final CustomFont largeFont = new CustomFont(new Font("Tahoma", 0, 24), true, false);

    public void drawSmallStringRainbow(String string, float x, float y, int colour) {
        this.smallFont.drawStringWithShadow(string, (double) x, (double) y, colour);
    }

    public void drawMediumStringRainbow(String string, float x, float y, int colour) {
        this.mediumFont.drawStringWithShadow(string, (double) x, (double) y, colour);
    }

    public void drawLargeStringRainbow(String string, float x, float y, int colour) {
        this.largeFont.drawStringWithShadow(string, (double) x, (double) y, colour);
    }
}
