package me.alpha432.oyvey.features.modules.render;

import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Predicate;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.combat.AutoCrystal;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.MathUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Tracer extends Module {

    public Setting players = this.register(new Setting("Players", Boolean.valueOf(true)));
    public Setting mobs = this.register(new Setting("Mobs", Boolean.valueOf(false)));
    public Setting animals = this.register(new Setting("Animals", Boolean.valueOf(false)));
    public Setting invisibles = this.register(new Setting("Invisibles", Boolean.valueOf(false)));
    public Setting drawFromSky = this.register(new Setting("DrawFromSky", Boolean.valueOf(false)));
    public Setting width = this.register(new Setting("Width", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(5.0F)));
    public Setting distance = this.register(new Setting("Radius", Integer.valueOf(300), Integer.valueOf(0), Integer.valueOf(300)));
    public Setting crystalCheck = this.register(new Setting("CrystalCheck", Boolean.valueOf(false)));

    public Tracer() {
        super("Tracers", "Draws lines to other players.", Module.Category.RENDER, false, false, false);
    }

    public void onRender3D(Render3DEvent event) {
        if (!fullNullCheck()) {
            GlStateManager.pushMatrix();
            Tracer.mc.world.loadedEntityList.stream().filter(EntityUtil::isLiving).filter((entity) -> {
                return entity instanceof EntityPlayer ? ((Boolean) this.players.getValue()).booleanValue() && Tracer.mc.player != entity : (EntityUtil.isPassive(entity) ? ((Boolean) this.animals.getValue()).booleanValue() : ((Boolean) this.mobs.getValue()).booleanValue());
            }).filter((entity) -> {
                return Tracer.mc.player.getDistanceSq(entity) < MathUtil.square((double) ((Integer) this.distance.getValue()).intValue());
            }).filter((entity) -> {
                return ((Boolean) this.invisibles.getValue()).booleanValue() || !entity.isInvisible();
            }).forEach((entity) -> {
                float[] colour = this.getColorByDistance(entity);

                this.drawLineToEntity(entity, colour[0], colour[1], colour[2], colour[3]);
            });
            GlStateManager.popMatrix();
        }
    }

    public double interpolate(double now, double then) {
        return then + (now - then) * (double) Tracer.mc.getRenderPartialTicks();
    }

    public double[] interpolate(Entity entity) {
        double posX = this.interpolate(entity.posX, entity.lastTickPosX) - Tracer.mc.getRenderManager().renderPosX;
        double posY = this.interpolate(entity.posY, entity.lastTickPosY) - Tracer.mc.getRenderManager().renderPosY;
        double posZ = this.interpolate(entity.posZ, entity.lastTickPosZ) - Tracer.mc.getRenderManager().renderPosZ;

        return new double[] { posX, posY, posZ};
    }

    public void drawLineToEntity(Entity e, float red, float green, float blue, float opacity) {
        double[] xyz = this.interpolate(e);

        this.drawLine(xyz[0], xyz[1], xyz[2], (double) e.height, red, green, blue, opacity);
    }

    public void drawLine(double posx, double posy, double posz, double up, float red, float green, float blue, float opacity) {
        Vec3d eyes = (new Vec3d(0.0D, 0.0D, 1.0D)).rotatePitch(-((float) Math.toRadians((double) Tracer.mc.player.rotationPitch))).rotateYaw(-((float) Math.toRadians((double) Tracer.mc.player.rotationYaw)));

        if (!((Boolean) this.drawFromSky.getValue()).booleanValue()) {
            this.drawLineFromPosToPos(eyes.x, eyes.y + (double) Tracer.mc.player.getEyeHeight(), eyes.z, posx, posy, posz, up, red, green, blue, opacity);
        } else {
            this.drawLineFromPosToPos(posx, 256.0D, posz, posx, posy, posz, up, red, green, blue, opacity);
        }

    }

    public void drawLineFromPosToPos(double posx, double posy, double posz, double posx2, double posy2, double posz2, double up, float red, float green, float blue, float opacity) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(((Float) this.width.getValue()).floatValue());
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, opacity);
        GlStateManager.disableLighting();
        GL11.glLoadIdentity();
        Tracer.mc.entityRenderer.orientCamera(Tracer.mc.getRenderPartialTicks());
        GL11.glBegin(1);
        GL11.glVertex3d(posx, posy, posz);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glColor3d(1.0D, 1.0D, 1.0D);
        GlStateManager.enableLighting();
    }

    public float[] getColorByDistance(Entity entity) {
        if (entity instanceof EntityPlayer && OyVey.friendManager.isFriend(entity.getName())) {
            return new float[] { 0.0F, 0.5F, 1.0F, 1.0F};
        } else {
            AutoCrystal autoCrystal = (AutoCrystal) OyVey.moduleManager.getModuleByClass(AutoCrystal.class);
            Color col = new Color(Color.HSBtoRGB((float) (Math.max(0.0D, Math.min(Tracer.mc.player.getDistanceSq(entity), ((Boolean) this.crystalCheck.getValue()).booleanValue() ? (double) (((Float) autoCrystal.placeRange.getValue()).floatValue() * ((Float) autoCrystal.placeRange.getValue()).floatValue()) : 2500.0D) / (double) (((Boolean) this.crystalCheck.getValue()).booleanValue() ? ((Float) autoCrystal.placeRange.getValue()).floatValue() * ((Float) autoCrystal.placeRange.getValue()).floatValue() : 2500.0F)) / 3.0D), 1.0F, 0.8F) | -16777216);

            return new float[] { (float) col.getRed() / 255.0F, (float) col.getGreen() / 255.0F, (float) col.getBlue() / 255.0F, 1.0F};
        }
    }
}
