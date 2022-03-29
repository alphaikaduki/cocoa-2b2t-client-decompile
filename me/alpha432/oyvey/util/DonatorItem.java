package me.alpha432.oyvey.util;

import java.awt.Color;

public class DonatorItem implements me.alpha432.oyvey.features.gui.custom.Globals {

    private final String name;
    private final int size;
    private double x;
    private double y;
    private final double width;
    private final double height;
    private final int canvasWidth;
    private final int canvasHeight;
    private final Color colour;
    private double xSpeed;
    private double ySpeed;

    public DonatorItem(String name, int size, int width, int height, int canvasWidth, int canvasHeight) {
        this.name = name;
        this.size = size;
        this.x = (double) MathsUtil.random(20, canvasWidth - 20);
        this.y = (double) MathsUtil.random(20, canvasHeight - 20);
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.width = (double) width;
        this.height = (double) height;
        this.colour = new Color((int) (Math.random() * 1.6777216E7D));
        this.xSpeed = this.offsetStart(0.1D);
        this.ySpeed = this.offsetStart(0.1D);
    }

    public void updatePos() {
        if (this.x + this.width >= (double) this.canvasWidth || this.x <= 0.0D) {
            this.xSpeed *= -1.0D;
        }

        if (this.y + this.height >= (double) this.canvasHeight || this.y <= 0.0D) {
            this.ySpeed *= -1.0D;
        }

        this.x += this.xSpeed;
        this.y += this.ySpeed;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.size;
    }

    public int getRgb() {
        return this.colour.getRGB();
    }

    private double offsetStart(double i) {
        return DonatorItem.random.nextInt(2) == 0 ? -i : i;
    }
}
