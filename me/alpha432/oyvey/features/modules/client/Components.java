package me.alpha432.oyvey.features.modules.client;

import java.awt.Color;
import java.util.Iterator;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Components extends Module {

    private static final ResourceLocation box = new ResourceLocation("textures/gui/container/shulker_box.png");
    public static ResourceLocation logo = new ResourceLocation("textures/CrepeLogo1.png");
    public static ResourceLocation logo2 = new ResourceLocation("textures/back.png");
    public Setting inventory = this.register(new Setting("Inventory", Boolean.valueOf(false)));
    public Setting invX = this.register(new Setting("InvX", Integer.valueOf(564), Integer.valueOf(0), Integer.valueOf(1000), test<invokedynamic>(this)));
    public Setting invY = this.register(new Setting("InvY", Integer.valueOf(467), Integer.valueOf(0), Integer.valueOf(1000), test<invokedynamic>(this)));
    public Setting fineinvX = this.register(new Setting("InvFineX", Integer.valueOf(0), test<invokedynamic>(this)));
    public Setting fineinvY = this.register(new Setting("InvFineY", Integer.valueOf(0), test<invokedynamic>(this)));
    public Setting renderXCarry;
    public Setting invH;
    public Setting holeHud;
    public Setting holeX;
    public Setting holeY;
    public Setting compass;
    public Setting compassX;
    public Setting compassY;
    public Setting scale;
    public Setting playerViewer;
    public Setting playerViewerX;
    public Setting playerViewerY;
    public Setting playerScale;
    public Setting imageLogo;
    public Setting imageX;
    public Setting imageY;
    public Setting imageWidth;
    public Setting imageHeight;
    public Setting imageLogo2;
    public Setting imageX2;
    public Setting imageY2;
    public Setting imageWidth2;
    public Setting imageHeight2;
    public Setting clock;
    public Setting clockFill;
    public Setting clockX;
    public Setting clockY;
    public Setting clockRadius;
    public Setting clockLineWidth;
    public Setting clockSlices;
    public Setting clockLoops;

    public Components() {
        super("HUD MAIN", "HudComponents", Module.Category.CLIENT, false, false, true);
        this.renderXCarry = this.register(new Setting("RenderXCarry", Boolean.FALSE, test<invokedynamic>(this)));
        this.invH = this.register(new Setting("InvH", Integer.valueOf(3), test<invokedynamic>(this)));
        this.holeHud = this.register(new Setting("HoleHUD", Boolean.valueOf(false)));
        this.holeX = this.register(new Setting("HoleX", Integer.valueOf(279), Integer.valueOf(0), Integer.valueOf(1000), test<invokedynamic>(this)));
        this.holeY = this.register(new Setting("HoleY", Integer.valueOf(485), Integer.valueOf(0), Integer.valueOf(1000), test<invokedynamic>(this)));
        this.compass = this.register(new Setting("Compass", Components.Compass.NONE));
        this.compassX = this.register(new Setting("CompX", Integer.valueOf(472), Integer.valueOf(0), Integer.valueOf(1000), test<invokedynamic>(this)));
        this.compassY = this.register(new Setting("CompY", Integer.valueOf(424), Integer.valueOf(0), Integer.valueOf(1000), test<invokedynamic>(this)));
        this.scale = this.register(new Setting("Scale", Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(10), test<invokedynamic>(this)));
        this.playerViewer = this.register(new Setting("PlayerViewer", Boolean.valueOf(false)));
        this.playerViewerX = this.register(new Setting("PlayerX", Integer.valueOf(752), Integer.valueOf(0), Integer.valueOf(1000), test<invokedynamic>(this)));
        this.playerViewerY = this.register(new Setting("PlayerY", Integer.valueOf(497), Integer.valueOf(0), Integer.valueOf(1000), test<invokedynamic>(this)));
        this.playerScale = this.register(new Setting("PlayerScale", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(2.0F), test<invokedynamic>(this)));
        this.imageLogo = this.register(new Setting("ImageLogo", Boolean.valueOf(true)));
        this.imageX = this.register(new Setting("ImageX", Integer.valueOf(-35), Integer.valueOf(-100), Integer.valueOf(1000), test<invokedynamic>(this)));
        this.imageY = this.register(new Setting("ImageY", Integer.valueOf(-48), Integer.valueOf(-100), Integer.valueOf(1000), test<invokedynamic>(this)));
        this.imageWidth = this.register(new Setting("ImageWidth", Integer.valueOf(177), Integer.valueOf(0), Integer.valueOf(1000), test<invokedynamic>(this)));
        this.imageHeight = this.register(new Setting("ImageHeight", Integer.valueOf(142), Integer.valueOf(0), Integer.valueOf(1000), test<invokedynamic>(this)));
        this.imageLogo2 = this.register(new Setting("ImageLogo", Boolean.valueOf(false)));
        this.imageX2 = this.register(new Setting("ImageX", Integer.valueOf(497), Integer.valueOf(0), Integer.valueOf(1000), test<invokedynamic>(this)));
        this.imageY2 = this.register(new Setting("ImageY", Integer.valueOf(230), Integer.valueOf(0), Integer.valueOf(1000), test<invokedynamic>(this)));
        this.imageWidth2 = this.register(new Setting("ImageWidth", Integer.valueOf(194), Integer.valueOf(0), Integer.valueOf(1000), test<invokedynamic>(this)));
        this.imageHeight2 = this.register(new Setting("ImageHeight", Integer.valueOf(182), Integer.valueOf(0), Integer.valueOf(1000), test<invokedynamic>(this)));
        this.clock = this.register(new Setting("Clock", Boolean.valueOf(true)));
        this.clockFill = this.register(new Setting("ClockFill", Boolean.valueOf(true)));
        this.clockX = this.register(new Setting("ClockX", Float.valueOf(2.0F), Float.valueOf(0.0F), Float.valueOf(1000.0F), test<invokedynamic>(this)));
        this.clockY = this.register(new Setting("ClockY", Float.valueOf(2.0F), Float.valueOf(0.0F), Float.valueOf(1000.0F), test<invokedynamic>(this)));
        this.clockRadius = this.register(new Setting("ClockRadius", Float.valueOf(6.0F), Float.valueOf(0.0F), Float.valueOf(100.0F), test<invokedynamic>(this)));
        this.clockLineWidth = this.register(new Setting("ClockLineWidth", Float.valueOf(1.0F), Float.valueOf(0.0F), Float.valueOf(5.0F), test<invokedynamic>(this)));
        this.clockSlices = this.register(new Setting("ClockSlices", Integer.valueOf(360), Integer.valueOf(1), Integer.valueOf(720), test<invokedynamic>(this)));
        this.clockLoops = this.register(new Setting("ClockLoops", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(720), test<invokedynamic>(this)));
    }

    public static EntityPlayer getClosestEnemy() {
        EntityPlayer closestPlayer = null;
        Iterator iterator = Components.mc.world.playerEntities.iterator();

        while (iterator.hasNext()) {
            EntityPlayer player = (EntityPlayer) iterator.next();

            if (player != Components.mc.player && !OyVey.friendManager.isFriend(player)) {
                if (closestPlayer == null) {
                    closestPlayer = player;
                } else if (Components.mc.player.getDistanceSq(player) < Components.mc.player.getDistanceSq(closestPlayer)) {
                    closestPlayer = player;
                }
            }
        }

        return closestPlayer;
    }

    private static double getPosOnCompass(Components.Direction dir) {
        double yaw = Math.toRadians((double) MathHelper.wrapDegrees(Components.mc.player.rotationYaw));
        int index = dir.ordinal();

        return yaw + (double) index * 1.5707963267948966D;
    }

    private static void preboxrender() {
        GL11.glPushMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        GlStateManager.enableBlend();
        GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
    }

    private static void postboxrender() {
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
        GL11.glPopMatrix();
    }

    private static void preitemrender() {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.scale(1.0F, 1.0F, 0.01F);
    }

    private static void postitemrender() {
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5D, 0.5D, 0.5D);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        GL11.glPopMatrix();
    }

    public static void drawCompleteImage(int posX, int posY, int width, int height) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) posX, (float) posY, 0.0F);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0F, 0.0F);
        GL11.glVertex3f(0.0F, 0.0F, 0.0F);
        GL11.glTexCoord2f(0.0F, 1.0F);
        GL11.glVertex3f(0.0F, (float) height, 0.0F);
        GL11.glTexCoord2f(1.0F, 1.0F);
        GL11.glVertex3f((float) width, (float) height, 0.0F);
        GL11.glTexCoord2f(1.0F, 0.0F);
        GL11.glVertex3f((float) width, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public void onRender2D(Render2DEvent event) {
        if (!fullNullCheck()) {
            if (((Boolean) this.playerViewer.getValue()).booleanValue()) {
                this.drawPlayer();
            }

            if (this.compass.getValue() != Components.Compass.NONE) {
                this.drawCompass();
            }

            if (((Boolean) this.holeHud.getValue()).booleanValue()) {
                this.drawOverlay(event.partialTicks);
            }

            if (((Boolean) this.inventory.getValue()).booleanValue()) {
                this.renderInventory();
            }

            if (((Boolean) this.imageLogo.getValue()).booleanValue()) {
                this.drawImageLogo();
            }

            if (((Boolean) this.imageLogo2.getValue()).booleanValue()) {
                this.drawImageLogo();
            }

            if (((Boolean) this.clock.getValue()).booleanValue()) {
                RenderUtil.drawClock(((Float) this.clockX.getValue()).floatValue(), ((Float) this.clockY.getValue()).floatValue(), ((Float) this.clockRadius.getValue()).floatValue(), ((Integer) this.clockSlices.getValue()).intValue(), ((Integer) this.clockLoops.getValue()).intValue(), ((Float) this.clockLineWidth.getValue()).floatValue(), ((Boolean) this.clockFill.getValue()).booleanValue(), new Color(255, 0, 0, 255));
            }

        }
    }

    public void drawImageLogo() {
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        Components.mc.getTextureManager().bindTexture(Components.logo);
        drawCompleteImage(((Integer) this.imageX.getValue()).intValue(), ((Integer) this.imageY.getValue()).intValue(), ((Integer) this.imageWidth.getValue()).intValue(), ((Integer) this.imageHeight.getValue()).intValue());
        Components.mc.getTextureManager().deleteTexture(Components.logo);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
    }

    public void drawImageLogo2() {
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        Components.mc.getTextureManager().bindTexture(Components.logo2);
        drawCompleteImage(((Integer) this.imageX2.getValue()).intValue(), ((Integer) this.imageY2.getValue()).intValue(), ((Integer) this.imageWidth2.getValue()).intValue(), ((Integer) this.imageHeight2.getValue()).intValue());
        Components.mc.getTextureManager().deleteTexture(Components.logo2);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
    }

    public void drawCompass() {
        ScaledResolution sr = new ScaledResolution(Components.mc);

        if (this.compass.getValue() == Components.Compass.LINE) {
            float centerX = Components.mc.player.rotationYaw;
            float rotationYaw = MathUtil.wrap(centerX);

            RenderUtil.drawRect((float) ((Integer) this.compassX.getValue()).intValue(), (float) ((Integer) this.compassY.getValue()).intValue(), (float) (((Integer) this.compassX.getValue()).intValue() + 100), (float) (((Integer) this.compassY.getValue()).intValue() + this.renderer.getFontHeight()), 1963986960);
            RenderUtil.glScissor((float) ((Integer) this.compassX.getValue()).intValue(), (float) ((Integer) this.compassY.getValue()).intValue(), (float) (((Integer) this.compassX.getValue()).intValue() + 100), (float) (((Integer) this.compassY.getValue()).intValue() + this.renderer.getFontHeight()), sr);
            GL11.glEnable(3089);
            float centerY = MathUtil.wrap((float) (Math.atan2(0.0D - Components.mc.player.posZ, 0.0D - Components.mc.player.posX) * 180.0D / 3.141592653589793D) - 90.0F);

            RenderUtil.drawLine((float) ((Integer) this.compassX.getValue()).intValue() - rotationYaw + 50.0F + centerY, (float) (((Integer) this.compassY.getValue()).intValue() + 2), (float) ((Integer) this.compassX.getValue()).intValue() - rotationYaw + 50.0F + centerY, (float) (((Integer) this.compassY.getValue()).intValue() + this.renderer.getFontHeight() - 2), 2.0F, -61424);
            RenderUtil.drawLine((float) ((Integer) this.compassX.getValue()).intValue() - rotationYaw + 50.0F + 45.0F, (float) (((Integer) this.compassY.getValue()).intValue() + 2), (float) ((Integer) this.compassX.getValue()).intValue() - rotationYaw + 50.0F + 45.0F, (float) (((Integer) this.compassY.getValue()).intValue() + this.renderer.getFontHeight() - 2), 2.0F, -1);
            RenderUtil.drawLine((float) ((Integer) this.compassX.getValue()).intValue() - rotationYaw + 50.0F - 45.0F, (float) (((Integer) this.compassY.getValue()).intValue() + 2), (float) ((Integer) this.compassX.getValue()).intValue() - rotationYaw + 50.0F - 45.0F, (float) (((Integer) this.compassY.getValue()).intValue() + this.renderer.getFontHeight() - 2), 2.0F, -1);
            RenderUtil.drawLine((float) ((Integer) this.compassX.getValue()).intValue() - rotationYaw + 50.0F + 135.0F, (float) (((Integer) this.compassY.getValue()).intValue() + 2), (float) ((Integer) this.compassX.getValue()).intValue() - rotationYaw + 50.0F + 135.0F, (float) (((Integer) this.compassY.getValue()).intValue() + this.renderer.getFontHeight() - 2), 2.0F, -1);
            RenderUtil.drawLine((float) ((Integer) this.compassX.getValue()).intValue() - rotationYaw + 50.0F - 135.0F, (float) (((Integer) this.compassY.getValue()).intValue() + 2), (float) ((Integer) this.compassX.getValue()).intValue() - rotationYaw + 50.0F - 135.0F, (float) (((Integer) this.compassY.getValue()).intValue() + this.renderer.getFontHeight() - 2), 2.0F, -1);
            this.renderer.drawStringWithShadow("n", (float) ((Integer) this.compassX.getValue()).intValue() - rotationYaw + 50.0F + 180.0F - (float) this.renderer.getStringWidth("n") / 2.0F, (float) ((Integer) this.compassY.getValue()).intValue(), -1);
            this.renderer.drawStringWithShadow("n", (float) ((Integer) this.compassX.getValue()).intValue() - rotationYaw + 50.0F - 180.0F - (float) this.renderer.getStringWidth("n") / 2.0F, (float) ((Integer) this.compassY.getValue()).intValue(), -1);
            this.renderer.drawStringWithShadow("e", (float) ((Integer) this.compassX.getValue()).intValue() - rotationYaw + 50.0F - 90.0F - (float) this.renderer.getStringWidth("e") / 2.0F, (float) ((Integer) this.compassY.getValue()).intValue(), -1);
            this.renderer.drawStringWithShadow("s", (float) ((Integer) this.compassX.getValue()).intValue() - rotationYaw + 50.0F - (float) this.renderer.getStringWidth("s") / 2.0F, (float) ((Integer) this.compassY.getValue()).intValue(), -1);
            this.renderer.drawStringWithShadow("w", (float) ((Integer) this.compassX.getValue()).intValue() - rotationYaw + 50.0F + 90.0F - (float) this.renderer.getStringWidth("w") / 2.0F, (float) ((Integer) this.compassY.getValue()).intValue(), -1);
            RenderUtil.drawLine((float) (((Integer) this.compassX.getValue()).intValue() + 50), (float) (((Integer) this.compassY.getValue()).intValue() + 1), (float) (((Integer) this.compassX.getValue()).intValue() + 50), (float) (((Integer) this.compassY.getValue()).intValue() + this.renderer.getFontHeight() - 1), 2.0F, -7303024);
            GL11.glDisable(3089);
        } else {
            double d0 = (double) ((Integer) this.compassX.getValue()).intValue();
            double d1 = (double) ((Integer) this.compassY.getValue()).intValue();
            Components.Direction[] acomponents_direction = Components.Direction.values();
            int i = acomponents_direction.length;

            for (int j = 0; j < i; ++j) {
                Components.Direction dir = acomponents_direction[j];
                double rad = getPosOnCompass(dir);

                this.renderer.drawStringWithShadow(dir.name(), (float) (d0 + this.getX(rad)), (float) (d1 + this.getY(rad)), dir == Components.Direction.N ? -65536 : -1);
            }
        }

    }

    public void drawPlayer(EntityPlayer player) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.rotate(0.0F, 0.0F, 5.0F, 0.0F);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) (((Integer) this.playerViewerX.getValue()).intValue() + 25), (float) (((Integer) this.playerViewerY.getValue()).intValue() + 25), 50.0F);
        GlStateManager.scale(-50.0F * ((Float) this.playerScale.getValue()).floatValue(), 50.0F * ((Float) this.playerScale.getValue()).floatValue(), 50.0F * ((Float) this.playerScale.getValue()).floatValue());
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float) Math.atan((double) ((float) ((Integer) this.playerViewerY.getValue()).intValue() / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Components.mc.getRenderManager();

        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);

        try {
            rendermanager.renderEntity(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        } catch (Exception exception) {
            ;
        }

        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.depthFunc(515);
        GlStateManager.resetColor();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }

    public void drawPlayer() {
        EntityPlayerSP ent = Components.mc.player;

        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.rotate(0.0F, 0.0F, 5.0F, 0.0F);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) (((Integer) this.playerViewerX.getValue()).intValue() + 25), (float) (((Integer) this.playerViewerY.getValue()).intValue() + 25), 50.0F);
        GlStateManager.scale(-50.0F * ((Float) this.playerScale.getValue()).floatValue(), 50.0F * ((Float) this.playerScale.getValue()).floatValue(), 50.0F * ((Float) this.playerScale.getValue()).floatValue());
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float) Math.atan((double) ((float) ((Integer) this.playerViewerY.getValue()).intValue() / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Components.mc.getRenderManager();

        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);

        try {
            rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        } catch (Exception exception) {
            ;
        }

        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.depthFunc(515);
        GlStateManager.resetColor();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }

    private double getX(double rad) {
        return Math.sin(rad) * (double) (((Integer) this.scale.getValue()).intValue() * 10);
    }

    private double getY(double rad) {
        double epicPitch = (double) MathHelper.clamp(Components.mc.player.rotationPitch + 30.0F, -90.0F, 90.0F);
        double pitchRadians = Math.toRadians(epicPitch);

        return Math.cos(rad) * Math.sin(pitchRadians) * (double) (((Integer) this.scale.getValue()).intValue() * 10);
    }

    public void drawOverlay(float partialTicks) {
        float yaw = 0.0F;
        int dir = MathHelper.floor((double) (Components.mc.player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        switch (dir) {
        case 1:
            yaw = 90.0F;
            break;

        case 2:
            yaw = -180.0F;
            break;

        case 3:
            yaw = -90.0F;
        }

        BlockPos northPos = this.traceToBlock(partialTicks, yaw);
        Block north = this.getBlock(northPos);
        int damage;

        if (north != null && north != Blocks.AIR) {
            damage = this.getBlockDamage(northPos);
            if (damage != 0) {
                RenderUtil.drawRect((float) (((Integer) this.holeX.getValue()).intValue() + 16), (float) ((Integer) this.holeY.getValue()).intValue(), (float) (((Integer) this.holeX.getValue()).intValue() + 32), (float) (((Integer) this.holeY.getValue()).intValue() + 16), 1627324416);
            }

            this.drawBlock(north, (float) (((Integer) this.holeX.getValue()).intValue() + 16), (float) ((Integer) this.holeY.getValue()).intValue());
        }

        BlockPos southPos;
        Block south;

        if ((south = this.getBlock(southPos = this.traceToBlock(partialTicks, yaw - 180.0F))) != null && south != Blocks.AIR) {
            damage = this.getBlockDamage(southPos);
            if (damage != 0) {
                RenderUtil.drawRect((float) (((Integer) this.holeX.getValue()).intValue() + 16), (float) (((Integer) this.holeY.getValue()).intValue() + 32), (float) (((Integer) this.holeX.getValue()).intValue() + 32), (float) (((Integer) this.holeY.getValue()).intValue() + 48), 1627324416);
            }

            this.drawBlock(south, (float) (((Integer) this.holeX.getValue()).intValue() + 16), (float) (((Integer) this.holeY.getValue()).intValue() + 32));
        }

        BlockPos eastPos;
        Block east;

        if ((east = this.getBlock(eastPos = this.traceToBlock(partialTicks, yaw + 90.0F))) != null && east != Blocks.AIR) {
            damage = this.getBlockDamage(eastPos);
            if (damage != 0) {
                RenderUtil.drawRect((float) (((Integer) this.holeX.getValue()).intValue() + 32), (float) (((Integer) this.holeY.getValue()).intValue() + 16), (float) (((Integer) this.holeX.getValue()).intValue() + 48), (float) (((Integer) this.holeY.getValue()).intValue() + 32), 1627324416);
            }

            this.drawBlock(east, (float) (((Integer) this.holeX.getValue()).intValue() + 32), (float) (((Integer) this.holeY.getValue()).intValue() + 16));
        }

        BlockPos westPos;
        Block west;

        if ((west = this.getBlock(westPos = this.traceToBlock(partialTicks, yaw - 90.0F))) != null && west != Blocks.AIR) {
            damage = this.getBlockDamage(westPos);
            if (damage != 0) {
                RenderUtil.drawRect((float) ((Integer) this.holeX.getValue()).intValue(), (float) (((Integer) this.holeY.getValue()).intValue() + 16), (float) (((Integer) this.holeX.getValue()).intValue() + 16), (float) (((Integer) this.holeY.getValue()).intValue() + 32), 1627324416);
            }

            this.drawBlock(west, (float) ((Integer) this.holeX.getValue()).intValue(), (float) (((Integer) this.holeY.getValue()).intValue() + 16));
        }

    }

    public void drawOverlay(float partialTicks, Entity player, int x, int y) {
        float yaw = 0.0F;
        int dir = MathHelper.floor((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        switch (dir) {
        case 1:
            yaw = 90.0F;
            break;

        case 2:
            yaw = -180.0F;
            break;

        case 3:
            yaw = -90.0F;
        }

        BlockPos northPos = this.traceToBlock(partialTicks, yaw, player);
        Block north = this.getBlock(northPos);
        int damage;

        if (north != null && north != Blocks.AIR) {
            damage = this.getBlockDamage(northPos);
            if (damage != 0) {
                RenderUtil.drawRect((float) (x + 16), (float) y, (float) (x + 32), (float) (y + 16), 1627324416);
            }

            this.drawBlock(north, (float) (x + 16), (float) y);
        }

        BlockPos southPos;
        Block south;

        if ((south = this.getBlock(southPos = this.traceToBlock(partialTicks, yaw - 180.0F, player))) != null && south != Blocks.AIR) {
            damage = this.getBlockDamage(southPos);
            if (damage != 0) {
                RenderUtil.drawRect((float) (x + 16), (float) (y + 32), (float) (x + 32), (float) (y + 48), 1627324416);
            }

            this.drawBlock(south, (float) (x + 16), (float) (y + 32));
        }

        BlockPos eastPos;
        Block east;

        if ((east = this.getBlock(eastPos = this.traceToBlock(partialTicks, yaw + 90.0F, player))) != null && east != Blocks.AIR) {
            damage = this.getBlockDamage(eastPos);
            if (damage != 0) {
                RenderUtil.drawRect((float) (x + 32), (float) (y + 16), (float) (x + 48), (float) (y + 32), 1627324416);
            }

            this.drawBlock(east, (float) (x + 32), (float) (y + 16));
        }

        BlockPos westPos;
        Block west;

        if ((west = this.getBlock(westPos = this.traceToBlock(partialTicks, yaw - 90.0F, player))) != null && west != Blocks.AIR) {
            damage = this.getBlockDamage(westPos);
            if (damage != 0) {
                RenderUtil.drawRect((float) x, (float) (y + 16), (float) (x + 16), (float) (y + 32), 1627324416);
            }

            this.drawBlock(west, (float) x, (float) (y + 16));
        }

    }

    private int getBlockDamage(BlockPos pos) {
        Iterator iterator = Components.mc.renderGlobal.damagedBlocks.values().iterator();

        DestroyBlockProgress destBlockProgress;

        do {
            if (!iterator.hasNext()) {
                return 0;
            }

            destBlockProgress = (DestroyBlockProgress) iterator.next();
        } while (destBlockProgress.getPosition().getX() != pos.getX() || destBlockProgress.getPosition().getY() != pos.getY() || destBlockProgress.getPosition().getZ() != pos.getZ());

        return destBlockProgress.getPartialBlockDamage();
    }

    private BlockPos traceToBlock(float partialTicks, float yaw) {
        Vec3d pos = EntityUtil.interpolateEntity(Components.mc.player, partialTicks);
        Vec3d dir = MathUtil.direction(yaw);

        return new BlockPos(pos.x + dir.x, pos.y, pos.z + dir.z);
    }

    private BlockPos traceToBlock(float partialTicks, float yaw, Entity player) {
        Vec3d pos = EntityUtil.interpolateEntity(player, partialTicks);
        Vec3d dir = MathUtil.direction(yaw);

        return new BlockPos(pos.x + dir.x, pos.y, pos.z + dir.z);
    }

    private Block getBlock(BlockPos pos) {
        Block block = Components.mc.world.getBlockState(pos).getBlock();

        return block != Blocks.BEDROCK && block != Blocks.OBSIDIAN ? Blocks.AIR : block;
    }

    private void drawBlock(Block block, float x, float y) {
        ItemStack stack = new ItemStack(block);

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.translate(x, y, 0.0F);
        Components.mc.getRenderItem().zLevel = 501.0F;
        Components.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
        Components.mc.getRenderItem().zLevel = 0.0F;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public void renderInventory() {
        this.boxrender(((Integer) this.invX.getValue()).intValue() + ((Integer) this.fineinvX.getValue()).intValue(), ((Integer) this.invY.getValue()).intValue() + ((Integer) this.fineinvY.getValue()).intValue());
        this.itemrender(Components.mc.player.inventory.mainInventory, ((Integer) this.invX.getValue()).intValue() + ((Integer) this.fineinvX.getValue()).intValue(), ((Integer) this.invY.getValue()).intValue() + ((Integer) this.fineinvY.getValue()).intValue());
    }

    private void boxrender(int x, int y) {
        preboxrender();
        Components.mc.renderEngine.bindTexture(Components.box);
        RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
        RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 54 + ((Integer) this.invH.getValue()).intValue(), 500);
        RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
        postboxrender();
    }

    private void itemrender(NonNullList items, int x, int y) {
        int iX;
        int i;

        for (i = 0; i < items.size() - 9; ++i) {
            iX = x + i % 9 * 18 + 8;
            int itemStack = y + i / 9 * 18 + 18;
            ItemStack itemStack1 = (ItemStack) items.get(i + 9);

            preitemrender();
            Components.mc.getRenderItem().zLevel = 501.0F;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(itemStack1, iX, itemStack);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(Components.mc.fontRenderer, itemStack1, iX, itemStack, (String) null);
            Components.mc.getRenderItem().zLevel = 0.0F;
            postitemrender();
        }

        if (((Boolean) this.renderXCarry.getValue()).booleanValue()) {
            for (i = 1; i < 5; ++i) {
                iX = x + (i + 4) % 9 * 18 + 8;
                ItemStack itemstack = ((Slot) Components.mc.player.inventoryContainer.inventorySlots.get(i)).getStack();

                if (!itemstack.isEmpty) {
                    preitemrender();
                    Components.mc.getRenderItem().zLevel = 501.0F;
                    RenderUtil.itemRender.renderItemAndEffectIntoGUI(itemstack, iX, y + 1);
                    RenderUtil.itemRender.renderItemOverlayIntoGUI(Components.mc.fontRenderer, itemstack, iX, y + 1, (String) null);
                    Components.mc.getRenderItem().zLevel = 0.0F;
                    postitemrender();
                }
            }
        }

    }

    private boolean lambda$new$27(Object v) {
        return ((Boolean) this.clock.getValue()).booleanValue();
    }

    private boolean lambda$new$26(Object v) {
        return ((Boolean) this.clock.getValue()).booleanValue();
    }

    private boolean lambda$new$25(Object v) {
        return ((Boolean) this.clock.getValue()).booleanValue();
    }

    private boolean lambda$new$24(Object v) {
        return ((Boolean) this.clock.getValue()).booleanValue();
    }

    private boolean lambda$new$23(Object v) {
        return ((Boolean) this.clock.getValue()).booleanValue();
    }

    private boolean lambda$new$22(Object v) {
        return ((Boolean) this.clock.getValue()).booleanValue();
    }

    private boolean lambda$new$21(Object v) {
        return ((Boolean) this.imageLogo.getValue()).booleanValue();
    }

    private boolean lambda$new$20(Object v) {
        return ((Boolean) this.imageLogo.getValue()).booleanValue();
    }

    private boolean lambda$new$19(Object v) {
        return ((Boolean) this.imageLogo.getValue()).booleanValue();
    }

    private boolean lambda$new$18(Object v) {
        return ((Boolean) this.imageLogo.getValue()).booleanValue();
    }

    private boolean lambda$new$17(Object v) {
        return ((Boolean) this.imageLogo.getValue()).booleanValue();
    }

    private boolean lambda$new$16(Object v) {
        return ((Boolean) this.imageLogo.getValue()).booleanValue();
    }

    private boolean lambda$new$15(Object v) {
        return ((Boolean) this.imageLogo.getValue()).booleanValue();
    }

    private boolean lambda$new$14(Object v) {
        return ((Boolean) this.imageLogo.getValue()).booleanValue();
    }

    private boolean lambda$new$13(Object v) {
        return ((Boolean) this.playerViewer.getValue()).booleanValue();
    }

    private boolean lambda$new$12(Object v) {
        return ((Boolean) this.playerViewer.getValue()).booleanValue();
    }

    private boolean lambda$new$11(Object v) {
        return ((Boolean) this.playerViewer.getValue()).booleanValue();
    }

    private boolean lambda$new$10(Object v) {
        return this.compass.getValue() != Components.Compass.NONE;
    }

    private boolean lambda$new$9(Object v) {
        return this.compass.getValue() != Components.Compass.NONE;
    }

    private boolean lambda$new$8(Object v) {
        return this.compass.getValue() != Components.Compass.NONE;
    }

    private boolean lambda$new$7(Object v) {
        return ((Boolean) this.holeHud.getValue()).booleanValue();
    }

    private boolean lambda$new$6(Object v) {
        return ((Boolean) this.holeHud.getValue()).booleanValue();
    }

    private boolean lambda$new$5(Object v) {
        return ((Boolean) this.inventory.getValue()).booleanValue();
    }

    private boolean lambda$new$4(Object v) {
        return ((Boolean) this.inventory.getValue()).booleanValue();
    }

    private boolean lambda$new$3(Object v) {
        return ((Boolean) this.inventory.getValue()).booleanValue();
    }

    private boolean lambda$new$2(Object v) {
        return ((Boolean) this.inventory.getValue()).booleanValue();
    }

    private boolean lambda$new$1(Object v) {
        return ((Boolean) this.inventory.getValue()).booleanValue();
    }

    private boolean lambda$new$0(Object v) {
        return ((Boolean) this.inventory.getValue()).booleanValue();
    }

    private static enum Direction {

        N, W, S, E;
    }

    public static enum Compass {

        NONE, CIRCLE, LINE;
    }

    public static enum TargetHudDesign {

        NORMAL, COMPACT;
    }
}
