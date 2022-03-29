package me.alpha432.oyvey.util.util2;

import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;

public class Colour extends Color {

    public Colour(int r, int g, int b) {
        super(r, g, b);
    }

    public Colour(int rgb) {
        super(rgb);
    }

    public Colour(int rgba, boolean hasalpha) {
        super(rgba, hasalpha);
    }

    public Colour(int r, int g, int b, int a) {
        super(r, g, b, a);
    }

    public Colour(Color color) {
        super(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public Colour(Colour color, int a) {
        super(color.getRed(), color.getGreen(), color.getBlue(), a);
    }

    public static Colour fromHSB(float hue, float saturation, float brightness) {
        return new Colour(Color.getHSBColor(hue, saturation, brightness));
    }

    public float getHue() {
        return RGBtoHSB(this.getRed(), this.getGreen(), this.getBlue(), (float[]) null)[0];
    }

    public float getSaturation() {
        return RGBtoHSB(this.getRed(), this.getGreen(), this.getBlue(), (float[]) null)[1];
    }

    public float getBrightness() {
        return RGBtoHSB(this.getRed(), this.getGreen(), this.getBlue(), (float[]) null)[2];
    }

    public void glColor() {
        GlStateManager.color((float) this.getRed() / 255.0F, (float) this.getGreen() / 255.0F, (float) this.getBlue() / 255.0F, (float) this.getAlpha() / 255.0F);
    }
}
