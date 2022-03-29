package me.alpha432.oyvey.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class ParticleGenerator {

    private final int count;
    private final int width;
    private final int height;
    private final ArrayList particles = new ArrayList();
    private final Random random = new Random();
    int state = 0;
    int a = 255;
    int r = 255;
    int g = 0;
    int b = 0;

    public ParticleGenerator(int count, int width, int height) {
        this.count = count;
        this.width = width;
        this.height = height;

        for (int i = 0; i < count; ++i) {
            this.particles.add(new ParticleGenerator.Particle(this.random.nextInt(width), this.random.nextInt(height)));
        }

    }

    public void drawParticles(int mouseX, int mouseY) {
        ParticleGenerator.Particle p;

        for (Iterator iterator = this.particles.iterator(); iterator.hasNext(); p.draw(mouseX, mouseY)) {
            p = (ParticleGenerator.Particle) iterator.next();
            if (p.reset) {
                p.resetPosSize();
                p.reset = false;
            }
        }

    }

    public class Particle {

        private final Random random = new Random();
        private int x;
        private int y;
        private int k;
        private float size;
        private boolean reset;

        public Particle(int x, int y) {
            this.x = x;
            this.y = y;
            this.size = this.genRandom(1.0F, 4.0F);
        }

        public void draw(int mouseX, int mouseY) {
            if (this.size <= 0.0F) {
                this.reset = true;
            }

            this.size -= 0.05F;
            ++this.k;
            int xx = (int) (MathHelper.cos(0.1F * (float) (this.x + this.k)) * 10.0F);
            int yy = (int) (MathHelper.cos(0.1F * (float) (this.y + this.k)) * 10.0F);

            Utils.drawBorderedCircle(this.x + xx, this.y + yy, this.size, 0, 553648127);
            float distance = (float) Utils.distance((float) (this.x + xx), (float) (this.y + yy), (float) mouseX, (float) mouseY);

            if (distance < 70.0F) {
                float alpha1 = Math.min(1.0F, Math.min(1.0F, 1.0F - distance / 70.0F));

                GL11.glEnable(2848);
                GL11.glDisable(2929);
                GL11.glColor4f(255.0F, 255.0F, 255.0F, 255.0F);
                GL11.glDisable(3553);
                GL11.glDepthMask(false);
                GL11.glBlendFunc(770, 771);
                GL11.glEnable(3042);
                GL11.glLineWidth(0.5F);
                GL11.glBegin(1);
                GL11.glVertex2f((float) (this.x + xx), (float) (this.y + yy));
                GL11.glVertex2f((float) mouseX, (float) mouseY);
                GL11.glEnd();
            }

        }

        public void resetPosSize() {
            this.x = this.random.nextInt(ParticleGenerator.this.width);
            this.y = this.random.nextInt(ParticleGenerator.this.height);
            this.size = this.genRandom(1.0F, 4.0F);
        }

        public float genRandom(float min, float max) {
            return (float) ((double) min + Math.random() * (double) (max - min + 1.0F));
        }
    }
}
