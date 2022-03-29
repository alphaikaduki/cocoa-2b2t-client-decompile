package me.alpha432.oyvey.features.modules.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.event.events.RenderEntityModelEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Skeleton extends Module {

    private static Skeleton INSTANCE = new Skeleton();
    private final Setting red = this.register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    private final Setting green = this.register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    private final Setting blue = this.register(new Setting("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    private final Setting alpha = this.register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    private final Setting lineWidth = this.register(new Setting("LineWidth", Float.valueOf(1.5F), Float.valueOf(0.1F), Float.valueOf(5.0F)));
    private final Setting colorFriends = this.register(new Setting("Friends", Boolean.valueOf(true)));
    private final Setting invisibles = this.register(new Setting("Invisibles", Boolean.valueOf(false)));
    private final Map rotationList = new HashMap();

    public Skeleton() {
        super("Skeleton", "Draws a nice Skeleton.", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    public static Skeleton getInstance() {
        if (Skeleton.INSTANCE == null) {
            Skeleton.INSTANCE = new Skeleton();
        }

        return Skeleton.INSTANCE;
    }

    private void setInstance() {
        Skeleton.INSTANCE = this;
    }

    public void onRender3D(Render3DEvent event) {
        RenderUtil.GLPre(((Float) this.lineWidth.getValue()).floatValue());
        Iterator iterator = Skeleton.mc.world.playerEntities.iterator();

        while (iterator.hasNext()) {
            EntityPlayer player = (EntityPlayer) iterator.next();

            if (player != null && player != Skeleton.mc.getRenderViewEntity() && player.isEntityAlive() && !player.isPlayerSleeping() && (!player.isInvisible() || ((Boolean) this.invisibles.getValue()).booleanValue()) && this.rotationList.get(player) != null && Skeleton.mc.player.getDistanceSq(player) >= 2500.0D) {
                ;
            }
        }

        RenderUtil.GlPost();
    }

    public void onRenderModel(RenderEntityModelEvent event) {
        if (event.getStage() == 0 && event.entity instanceof EntityPlayer && event.modelBase instanceof ModelBiped) {
            ModelBiped biped = (ModelBiped) event.modelBase;
            float[][] rotations = RenderUtil.getBipedRotations(biped);
            EntityPlayer player = (EntityPlayer) event.entity;

            this.rotationList.put(player, rotations);
        }

    }

    private void renderSkeleton(EntityPlayer player, float[][] rotations, Color color) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.color((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
        Vec3d interp = EntityUtil.getInterpolatedRenderPos(player, Skeleton.mc.getRenderPartialTicks());
        double pX = interp.x;
        double pY = interp.y;
        double pZ = interp.z;

        GlStateManager.translate(pX, pY, pZ);
        GlStateManager.rotate(-player.renderYawOffset, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0D, 0.0D, player.isSneaking() ? -0.235D : 0.0D);
        float sneak = player.isSneaking() ? 0.6F : 0.75F;

        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.125D, (double) sneak, 0.0D);
        if (rotations[3][0] != 0.0F) {
            GlStateManager.rotate(rotations[3][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
        }

        if (rotations[3][1] != 0.0F) {
            GlStateManager.rotate(rotations[3][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
        }

        if (rotations[3][2] != 0.0F) {
            GlStateManager.rotate(rotations[3][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.glBegin(3);
        GL11.glVertex3d(0.0D, 0.0D, 0.0D);
        GL11.glVertex3d(0.0D, (double) (-sneak), 0.0D);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.125D, (double) sneak, 0.0D);
        if (rotations[4][0] != 0.0F) {
            GlStateManager.rotate(rotations[4][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
        }

        if (rotations[4][1] != 0.0F) {
            GlStateManager.rotate(rotations[4][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
        }

        if (rotations[4][2] != 0.0F) {
            GlStateManager.rotate(rotations[4][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.glBegin(3);
        GL11.glVertex3d(0.0D, 0.0D, 0.0D);
        GL11.glVertex3d(0.0D, (double) (-sneak), 0.0D);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.translate(0.0D, 0.0D, player.isSneaking() ? 0.25D : 0.0D);
        GlStateManager.pushMatrix();
        double sneakOffset = 0.0D;

        if (player.isSneaking()) {
            sneakOffset = -0.05D;
        }

        GlStateManager.translate(0.0D, sneakOffset, player.isSneaking() ? -0.01725D : 0.0D);
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.375D, (double) sneak + 0.55D, 0.0D);
        if (rotations[1][0] != 0.0F) {
            GlStateManager.rotate(rotations[1][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
        }

        if (rotations[1][1] != 0.0F) {
            GlStateManager.rotate(rotations[1][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
        }

        if (rotations[1][2] != 0.0F) {
            GlStateManager.rotate(-rotations[1][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.glBegin(3);
        GL11.glVertex3d(0.0D, 0.0D, 0.0D);
        GL11.glVertex3d(0.0D, -0.5D, 0.0D);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.375D, (double) sneak + 0.55D, 0.0D);
        if (rotations[2][0] != 0.0F) {
            GlStateManager.rotate(rotations[2][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
        }

        if (rotations[2][1] != 0.0F) {
            GlStateManager.rotate(rotations[2][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
        }

        if (rotations[2][2] != 0.0F) {
            GlStateManager.rotate(-rotations[2][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.glBegin(3);
        GL11.glVertex3d(0.0D, 0.0D, 0.0D);
        GL11.glVertex3d(0.0D, -0.5D, 0.0D);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0D, (double) sneak + 0.55D, 0.0D);
        if (rotations[0][0] != 0.0F) {
            GlStateManager.rotate(rotations[0][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
        }

        GlStateManager.glBegin(3);
        GL11.glVertex3d(0.0D, 0.0D, 0.0D);
        GL11.glVertex3d(0.0D, 0.3D, 0.0D);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        GlStateManager.rotate(player.isSneaking() ? 25.0F : 0.0F, 1.0F, 0.0F, 0.0F);
        if (player.isSneaking()) {
            sneakOffset = -0.16175D;
        }

        GlStateManager.translate(0.0D, sneakOffset, player.isSneaking() ? -0.48025D : 0.0D);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0D, (double) sneak, 0.0D);
        GlStateManager.glBegin(3);
        GL11.glVertex3d(-0.125D, 0.0D, 0.0D);
        GL11.glVertex3d(0.125D, 0.0D, 0.0D);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0D, (double) sneak, 0.0D);
        GlStateManager.glBegin(3);
        GL11.glVertex3d(0.0D, 0.0D, 0.0D);
        GL11.glVertex3d(0.0D, 0.55D, 0.0D);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0D, (double) sneak + 0.55D, 0.0D);
        GlStateManager.glBegin(3);
        GL11.glVertex3d(-0.375D, 0.0D, 0.0D);
        GL11.glVertex3d(0.375D, 0.0D, 0.0D);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }

    private void renderSkeletonTest(EntityPlayer player, float[][] rotations, Color startColor, Color endColor) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F);
        Vec3d interp = EntityUtil.getInterpolatedRenderPos(player, Skeleton.mc.getRenderPartialTicks());
        double pX = interp.x;
        double pY = interp.y;
        double pZ = interp.z;

        GlStateManager.translate(pX, pY, pZ);
        GlStateManager.rotate(-player.renderYawOffset, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0D, 0.0D, player.isSneaking() ? -0.235D : 0.0D);
        float sneak = player.isSneaking() ? 0.6F : 0.75F;

        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.125D, (double) sneak, 0.0D);
        if (rotations[3][0] != 0.0F) {
            GlStateManager.rotate(rotations[3][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
        }

        if (rotations[3][1] != 0.0F) {
            GlStateManager.rotate(rotations[3][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
        }

        if (rotations[3][2] != 0.0F) {
            GlStateManager.rotate(rotations[3][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.glBegin(3);
        GL11.glVertex3d(0.0D, 0.0D, 0.0D);
        GlStateManager.color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F);
        GL11.glVertex3d(0.0D, (double) (-sneak), 0.0D);
        GlStateManager.color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.125D, (double) sneak, 0.0D);
        if (rotations[4][0] != 0.0F) {
            GlStateManager.rotate(rotations[4][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
        }

        if (rotations[4][1] != 0.0F) {
            GlStateManager.rotate(rotations[4][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
        }

        if (rotations[4][2] != 0.0F) {
            GlStateManager.rotate(rotations[4][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.glBegin(3);
        GlStateManager.color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F);
        GL11.glVertex3d(0.0D, 0.0D, 0.0D);
        GlStateManager.color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F);
        GL11.glVertex3d(0.0D, (double) (-sneak), 0.0D);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.translate(0.0D, 0.0D, player.isSneaking() ? 0.25D : 0.0D);
        GlStateManager.pushMatrix();
        double sneakOffset = 0.0D;

        if (player.isSneaking()) {
            sneakOffset = -0.05D;
        }

        GlStateManager.translate(0.0D, sneakOffset, player.isSneaking() ? -0.01725D : 0.0D);
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.375D, (double) sneak + 0.55D, 0.0D);
        if (rotations[1][0] != 0.0F) {
            GlStateManager.rotate(rotations[1][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
        }

        if (rotations[1][1] != 0.0F) {
            GlStateManager.rotate(rotations[1][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
        }

        if (rotations[1][2] != 0.0F) {
            GlStateManager.rotate(-rotations[1][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.glBegin(3);
        GlStateManager.color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F);
        GL11.glVertex3d(0.0D, 0.0D, 0.0D);
        GlStateManager.color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F);
        GL11.glVertex3d(0.0D, -0.5D, 0.0D);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.375D, (double) sneak + 0.55D, 0.0D);
        if (rotations[2][0] != 0.0F) {
            GlStateManager.rotate(rotations[2][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
        }

        if (rotations[2][1] != 0.0F) {
            GlStateManager.rotate(rotations[2][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
        }

        if (rotations[2][2] != 0.0F) {
            GlStateManager.rotate(-rotations[2][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.glBegin(3);
        GlStateManager.color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F);
        GL11.glVertex3d(0.0D, 0.0D, 0.0D);
        GlStateManager.color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F);
        GL11.glVertex3d(0.0D, -0.5D, 0.0D);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0D, (double) sneak + 0.55D, 0.0D);
        if (rotations[0][0] != 0.0F) {
            GlStateManager.rotate(rotations[0][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
        }

        GlStateManager.glBegin(3);
        GlStateManager.color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F);
        GL11.glVertex3d(0.0D, 0.0D, 0.0D);
        GlStateManager.color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F);
        GL11.glVertex3d(0.0D, 0.3D, 0.0D);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        GlStateManager.rotate(player.isSneaking() ? 25.0F : 0.0F, 1.0F, 0.0F, 0.0F);
        if (player.isSneaking()) {
            sneakOffset = -0.16175D;
        }

        GlStateManager.translate(0.0D, sneakOffset, player.isSneaking() ? -0.48025D : 0.0D);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0D, (double) sneak, 0.0D);
        GlStateManager.glBegin(3);
        GlStateManager.color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F);
        GL11.glVertex3d(-0.125D, 0.0D, 0.0D);
        GlStateManager.color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F);
        GL11.glVertex3d(0.125D, 0.0D, 0.0D);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0D, (double) sneak, 0.0D);
        GlStateManager.glBegin(3);
        GlStateManager.color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F);
        GL11.glVertex3d(0.0D, 0.0D, 0.0D);
        GlStateManager.color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F);
        GL11.glVertex3d(0.0D, 0.55D, 0.0D);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0D, (double) sneak + 0.55D, 0.0D);
        GlStateManager.glBegin(3);
        GlStateManager.color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F);
        GL11.glVertex3d(-0.375D, 0.0D, 0.0D);
        GlStateManager.color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F);
        GL11.glVertex3d(0.375D, 0.0D, 0.0D);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }
}
