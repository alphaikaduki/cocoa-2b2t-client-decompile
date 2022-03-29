package me.alpha432.oyvey.util;

import java.awt.Color;
import me.alpha432.oyvey.util.util2.Colour;

public class Rainbow {

    public static Color getColour() {
        return Colour.fromHSB((float) (System.currentTimeMillis() % 11520L) / 11520.0F, 1.0F, 1.0F);
    }
}
