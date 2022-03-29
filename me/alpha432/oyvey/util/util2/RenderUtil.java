package me.alpha432.oyvey.util.util2;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.Globals;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class RenderUtil implements Globals {

    private static final Map glCapMap = new HashMap();
    public static ICamera camera = new Frustum();

    public static String getRandomFont() {
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(Locale.ENGLISH);

        return fonts[RenderUtil.random.nextInt(fonts.length)];
    }

    public static void drawTriangleOutline(float x, float y, float size, float widthDiv, float heightDiv, float outlineWidth, int color) {
        boolean blend = GL11.glIsEnabled(3042);

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glLineWidth(outlineWidth);
        hexColor(color);
        GL11.glBegin(2);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) (x - size / widthDiv), (double) (y - size));
        GL11.glVertex2d((double) x, (double) (y - size / heightDiv));
        GL11.glVertex2d((double) (x + size / widthDiv), (double) (y - size));
        GL11.glVertex2d((double) x, (double) y);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        if (!blend) {
            GL11.glDisable(3042);
        }

        GL11.glDisable(2848);
    }

    public static void hexColor(int hexColor) {
        float red = (float) (hexColor >> 16 & 255) / 255.0F;
        float green = (float) (hexColor >> 8 & 255) / 255.0F;
        float blue = (float) (hexColor & 255) / 255.0F;
        float alpha = (float) (hexColor >> 24 & 255) / 255.0F;

        GL11.glColor4f(red, green, blue, alpha);
    }

    public static int getRainbow(int speed, int offset, float s, float b) {
        float hue = (float) ((System.currentTimeMillis() + (long) offset) % (long) speed);

        return Color.getHSBColor(hue / (float) speed, s, b).getRGB();
    }

    public static void drawLine(float x, float y, float x1, float y1, float thickness, int hex) {
        float red = (float) (hex >> 16 & 255) / 255.0F;
        float green = (float) (hex >> 8 & 255) / 255.0F;
        float blue = (float) (hex & 255) / 255.0F;
        float alpha = (float) (hex >> 24 & 255) / 255.0F;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GL11.glLineWidth(thickness);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) x, (double) y, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double) x1, (double) y1, 0.0D).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GL11.glDisable(2848);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public static void drawOutlineLine(double left, double top, double right, double bottom, double width, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth((float) width);
        double a;

        if (left < right) {
            a = left;
            left = right;
            right = a;
        }

        if (top < bottom) {
            a = top;
            top = bottom;
            bottom = a;
        }

        float a1 = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(left, bottom, 0.0D).color(r, g, b, a1).endVertex();
        bufferbuilder.pos(right, bottom, 0.0D).color(r, g, b, a1).endVertex();
        bufferbuilder.pos(right, top, 0.0D).color(r, g, b, a1).endVertex();
        bufferbuilder.pos(left, top, 0.0D).color(r, g, b, a1).endVertex();
        bufferbuilder.pos(left, bottom, 0.0D).color(r, g, b, a1).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawBox(BlockPos blockPos, double height, Colour color, int sides) {
        drawBox((double) blockPos.getX(), (double) blockPos.getY(), (double) blockPos.getZ(), 1.0D, height, 1.0D, color, color.getAlpha(), sides);
    }

    public static void drawBox(AxisAlignedBB bb, boolean check, double height, Colour color, int sides) {
        drawBox(bb, check, height, color, color.getAlpha(), sides);
    }

    public static void drawBox(AxisAlignedBB bb, boolean check, double height, Colour color, int alpha, int sides) {
        if (check) {
            drawBox(bb.minX, bb.minY, bb.minZ, bb.maxX - bb.minX, bb.maxY - bb.minY, bb.maxZ - bb.minZ, color, alpha, sides);
        } else {
            drawBox(bb.minX, bb.minY, bb.minZ, bb.maxX - bb.minX, height, bb.maxZ - bb.minZ, color, alpha, sides);
        }

    }

    public static void drawBoxESP(BlockPos pos, Color color, Color secondColor, float lineWidth, boolean outline, boolean box, boolean air) {
        if (box) {
            drawBox(pos, color, air);
        }

        if (outline) {
            drawBlockOutline(pos, secondColor, lineWidth, air);
        }

    }

    public static AxisAlignedBB interpolateAxis(AxisAlignedBB bb) {
        return new AxisAlignedBB(bb.minX - RenderUtil.mc.getRenderManager().viewerPosX, bb.minY - RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ - RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX - RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY - RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ - RenderUtil.mc.getRenderManager().viewerPosZ);
    }

    public static void drawRect(float x, float y, float w, float h, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) x, (double) h, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double) w, (double) h, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double) w, (double) y, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double) x, (double) y, 0.0D).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawBoxESP(BlockPos pos, Color color, float lineWidth, boolean outline, boolean box, int boxAlpha, Double height) {
        AxisAlignedBB bb = new AxisAlignedBB((double) pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double) pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double) pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double) (pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double) (pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY + height.doubleValue(), (double) (pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);

        RenderUtil.camera.setPosition(((Entity) Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity())).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (RenderUtil.camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glLineWidth(lineWidth);
            if (box) {
                RenderGlobal.renderFilledBox(bb, (float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) boxAlpha / 255.0F);
            }

            if (outline) {
                RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, (float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
            }

            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

    }

    public static void drawBoxESP(BlockPos pos, Color color, Color outLineColor, double lineWidth, boolean outline, boolean box, Float height) {
        AxisAlignedBB bb = new AxisAlignedBB((double) pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double) pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double) pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double) (pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double) (pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY + (double) height.floatValue(), (double) (pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);

        RenderUtil.camera.setPosition(((Entity) Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity())).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (RenderUtil.camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glLineWidth((float) lineWidth);
            if (box) {
                RenderGlobal.renderFilledBox(bb, (float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
            }

            if (outline) {
                RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, (float) outLineColor.getRed() / 255.0F, (float) outLineColor.getGreen() / 255.0F, (float) outLineColor.getBlue() / 255.0F, (float) outLineColor.getAlpha() / 255.0F);
            }

            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

    }

    public static void drawBlockOutline(BlockPos pos, Color color, float linewidth, boolean air) {
        IBlockState iblockstate = RenderUtil.mc.world.getBlockState(pos);

        if ((air || iblockstate.getMaterial() != Material.AIR) && RenderUtil.mc.world.getWorldBorder().contains(pos)) {
            Vec3d interp = EntityUtil.interpolateEntity(RenderUtil.mc.player, RenderUtil.mc.getRenderPartialTicks());

            drawBlockOutline(iblockstate.getSelectedBoundingBox(RenderUtil.mc.world, pos).grow(0.0020000000949949026D).offset(-interp.x, -interp.y, -interp.z), color, linewidth);
        }

    }

    public static void drawBox(BlockPos pos, Color color, boolean air) {
        IBlockState iblockstate = RenderUtil.mc.world.getBlockState(pos);

        if ((air || iblockstate.getMaterial() != Material.AIR) && RenderUtil.mc.world.getWorldBorder().contains(pos)) {
            Vec3d interp = EntityUtil.interpolateEntity(RenderUtil.mc.player, RenderUtil.mc.getRenderPartialTicks());
            AxisAlignedBB bb = iblockstate.getSelectedBoundingBox(RenderUtil.mc.world, pos).grow(0.0020000000949949026D).offset(-interp.x, -interp.y, -interp.z);

            RenderUtil.camera.setPosition(((Entity) Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity())).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
            if (RenderUtil.camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);
                GL11.glEnable(2848);
                GL11.glHint(3154, 4354);
                RenderGlobal.renderFilledBox(bb, (float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
                GL11.glDisable(2848);
                GlStateManager.depthMask(true);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }

    }

    public static void drawGradientBlockOutline(AxisAlignedBB bb, Color startColor, Color endColor, float linewidth) {
        float red = (float) startColor.getRed() / 255.0F;
        float green = (float) startColor.getGreen() / 255.0F;
        float blue = (float) startColor.getBlue() / 255.0F;
        float alpha = (float) startColor.getAlpha() / 255.0F;
        float red1 = (float) endColor.getRed() / 255.0F;
        float green1 = (float) endColor.getGreen() / 255.0F;
        float blue1 = (float) endColor.getBlue() / 255.0F;
        float alpha1 = (float) endColor.getAlpha() / 255.0F;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(linewidth);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawBlockOutline(AxisAlignedBB bb, Color color, float linewidth) {
        float red = (float) color.getRed() / 255.0F;
        float green = (float) color.getGreen() / 255.0F;
        float blue = (float) color.getBlue() / 255.0F;
        float alpha = (float) color.getAlpha() / 255.0F;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(linewidth);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawGradientBlockOutline(BlockPos pos, Color startColor, Color endColor, float linewidth, double height) {
        IBlockState iblockstate = RenderUtil.mc.world.getBlockState(pos);
        Vec3d interp = EntityUtil.interpolateEntity(RenderUtil.mc.player, RenderUtil.mc.getRenderPartialTicks());

        drawGradientBlockOutline(iblockstate.getSelectedBoundingBox(RenderUtil.mc.world, pos).grow(0.0020000000949949026D).offset(-interp.x, -interp.y, -interp.z).expand(0.0D, height, 0.0D), startColor, endColor, linewidth);
    }

    public static void drawBlockOutline(BlockPos pos, Color color, float linewidth, boolean air, double height, boolean gradient, boolean invert) {
        if (gradient) {
            Color iblockstate1 = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

            drawGradientBlockOutline(pos, invert ? iblockstate1 : color, invert ? color : iblockstate1, linewidth, height);
        } else {
            IBlockState iblockstate = RenderUtil.mc.world.getBlockState(pos);

            if ((air || iblockstate.getMaterial() != Material.AIR) && RenderUtil.mc.world.getWorldBorder().contains(pos)) {
                AxisAlignedBB blockAxis = new AxisAlignedBB((double) pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double) pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double) pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double) (pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double) (pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY + height, (double) (pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);

                drawBlockOutline(blockAxis.grow(0.0020000000949949026D), color, linewidth);
            }

        }
    }

    public static void drawBoxESP(BlockPos pos, Color color, Color secondColor, float lineWidth, boolean outline, boolean box, boolean air, double height, boolean gradientBox, boolean gradientOutline, boolean invertGradientBox, boolean invertGradientOutline, int gradientAlpha) {
        if (box) {
            drawBox(pos, new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()), height, gradientBox, invertGradientBox, gradientAlpha);
        }

        if (outline) {
            drawBlockOutline(pos, secondColor, lineWidth, air, height, gradientOutline, invertGradientOutline);
        }

    }

    public static void drawGradientPlane(BlockPos pos, EnumFacing face, Color startColor, Color endColor, double height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        IBlockState iblockstate = RenderUtil.mc.world.getBlockState(pos);
        Vec3d interp = EntityUtil.interpolateEntity(RenderUtil.mc.player, RenderUtil.mc.getRenderPartialTicks());
        AxisAlignedBB bb = iblockstate.getSelectedBoundingBox(RenderUtil.mc.world, pos).grow(0.0020000000949949026D).offset(-interp.x, -interp.y, -interp.z).expand(0.0D, height, 0.0D);
        float red = (float) startColor.getRed() / 255.0F;
        float green = (float) startColor.getGreen() / 255.0F;
        float blue = (float) startColor.getBlue() / 255.0F;
        float alpha = (float) startColor.getAlpha() / 255.0F;
        float red1 = (float) endColor.getRed() / 255.0F;
        float green1 = (float) endColor.getGreen() / 255.0F;
        float blue1 = (float) endColor.getBlue() / 255.0F;
        float alpha1 = (float) endColor.getAlpha() / 255.0F;
        double x1 = 0.0D;
        double y1 = 0.0D;
        double z1 = 0.0D;
        double x2 = 0.0D;
        double y2 = 0.0D;
        double z2 = 0.0D;

        if (face == EnumFacing.DOWN) {
            x1 = bb.minX;
            x2 = bb.maxX;
            y1 = bb.minY;
            y2 = bb.minY;
            z1 = bb.minZ;
            z2 = bb.maxZ;
        } else if (face == EnumFacing.UP) {
            x1 = bb.minX;
            x2 = bb.maxX;
            y1 = bb.maxY;
            y2 = bb.maxY;
            z1 = bb.minZ;
            z2 = bb.maxZ;
        } else if (face == EnumFacing.EAST) {
            x1 = bb.maxX;
            x2 = bb.maxX;
            y1 = bb.minY;
            y2 = bb.maxY;
            z1 = bb.minZ;
            z2 = bb.maxZ;
        } else if (face == EnumFacing.WEST) {
            x1 = bb.minX;
            x2 = bb.minX;
            y1 = bb.minY;
            y2 = bb.maxY;
            z1 = bb.minZ;
            z2 = bb.maxZ;
        } else if (face == EnumFacing.SOUTH) {
            x1 = bb.minX;
            x2 = bb.maxX;
            y1 = bb.minY;
            y2 = bb.maxY;
            z1 = bb.maxZ;
            z2 = bb.maxZ;
        } else if (face == EnumFacing.NORTH) {
            x1 = bb.minX;
            x2 = bb.maxX;
            y1 = bb.minY;
            y2 = bb.maxY;
            z1 = bb.minZ;
            z2 = bb.minZ;
        }

        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.depthMask(false);
        builder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        if (face != EnumFacing.EAST && face != EnumFacing.WEST && face != EnumFacing.NORTH && face != EnumFacing.SOUTH) {
            if (face == EnumFacing.UP) {
                builder.pos(x1, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x1, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x1, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x1, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x1, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x1, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x1, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x2, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x2, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x2, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x2, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x2, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x2, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x1, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x1, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x2, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x1, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x2, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x2, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x1, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x2, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
                builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            } else if (face == EnumFacing.DOWN) {
                builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
                builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
                builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
                builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
                builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
                builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
                builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
                builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
                builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
                builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
                builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
                builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
                builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
                builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
                builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
                builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
                builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
                builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
                builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
                builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
                builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
                builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
                builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
                builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
                builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
                builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
                builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
                builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
                builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
                builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
            }
        } else {
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
        }

        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    public static void drawOpenGradientBox(BlockPos pos, Color startColor, Color endColor, double height) {
        EnumFacing[] aenumfacing = EnumFacing.values();
        int i = aenumfacing.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing face = aenumfacing[j];

            if (face != EnumFacing.UP) {
                drawGradientPlane(pos, face, startColor, endColor, height);
            }
        }

    }

    public static void drawBox(BlockPos pos, Color color, double height, boolean gradient, boolean invert, int alpha) {
        if (gradient) {
            Color bb1 = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);

            drawOpenGradientBox(pos, invert ? bb1 : color, invert ? color : bb1, height);
        } else {
            AxisAlignedBB bb = new AxisAlignedBB((double) pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double) pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double) pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double) (pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double) (pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY + height, (double) (pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);

            RenderUtil.camera.setPosition(((Entity) Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity())).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
            if (RenderUtil.camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);
                GL11.glEnable(2848);
                GL11.glHint(3154, 4354);
                RenderGlobal.renderFilledBox(bb, (float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
                GL11.glDisable(2848);
                GlStateManager.depthMask(true);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }

        }
    }

    public static void drawBBBox(AxisAlignedBB BB, Colour colour, int alpha) {
        AxisAlignedBB bb = new AxisAlignedBB(BB.minX - RenderUtil.mc.getRenderManager().viewerPosX, BB.minY - RenderUtil.mc.getRenderManager().viewerPosY, BB.minZ - RenderUtil.mc.getRenderManager().viewerPosZ, BB.maxX - RenderUtil.mc.getRenderManager().viewerPosX, BB.maxY - RenderUtil.mc.getRenderManager().viewerPosY, BB.maxZ - RenderUtil.mc.getRenderManager().viewerPosZ);

        RenderUtil.camera.setPosition(((Entity) Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity())).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (RenderUtil.camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            RenderGlobal.renderFilledBox(bb, (float) colour.getRed() / 255.0F, (float) colour.getGreen() / 255.0F, (float) colour.getBlue() / 255.0F, (float) alpha / 255.0F);
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

    }

    public void resetCaps() {
        RenderUtil.glCapMap.forEach(this::setGlState);
    }

    public void setGlState(int cap, boolean state) {
        if (state) {
            GL11.glEnable(cap);
        } else {
            GL11.glDisable(cap);
        }

    }

    public static void drawBox(double x, double y, double z, double w, double h, double d, Colour color, int alpha, int sides) {
        GlStateManager.disableAlpha();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        color.glColor();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        doVerticies(new AxisAlignedBB(x, y, z, x + w, y + h, z + d), color, alpha, bufferbuilder, sides, false);
        tessellator.draw();
        GlStateManager.enableAlpha();
    }

    private static AxisAlignedBB getBoundingBox(BlockPos bp, double width, double height, double depth) {
        double x = (double) bp.getX();
        double y = (double) bp.getY();
        double z = (double) bp.getZ();

        return new AxisAlignedBB(x, y, z, x + width, y + height, z + depth);
    }

    private static AxisAlignedBB getBoundingBox(BlockPos blockPos) {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB((double) blockPos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double) blockPos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double) blockPos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double) (blockPos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double) (blockPos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double) (blockPos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);

        return axisAlignedBB;
    }

    public static void drawBoundingBox(AxisAlignedBB bb, double width, Colour color, int alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        GlStateManager.glLineWidth((float) width);
        color.glColor();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        colorVertex(bb.minX, bb.minY, bb.minZ, color, color.getAlpha(), bufferbuilder);
        colorVertex(bb.minX, bb.minY, bb.maxZ, color, color.getAlpha(), bufferbuilder);
        colorVertex(bb.maxX, bb.minY, bb.maxZ, color, color.getAlpha(), bufferbuilder);
        colorVertex(bb.maxX, bb.minY, bb.minZ, color, color.getAlpha(), bufferbuilder);
        colorVertex(bb.minX, bb.minY, bb.minZ, color, color.getAlpha(), bufferbuilder);
        colorVertex(bb.minX, bb.maxY, bb.minZ, color, alpha, bufferbuilder);
        colorVertex(bb.minX, bb.maxY, bb.maxZ, color, alpha, bufferbuilder);
        colorVertex(bb.minX, bb.minY, bb.maxZ, color, color.getAlpha(), bufferbuilder);
        colorVertex(bb.maxX, bb.minY, bb.maxZ, color, color.getAlpha(), bufferbuilder);
        colorVertex(bb.maxX, bb.maxY, bb.maxZ, color, alpha, bufferbuilder);
        colorVertex(bb.minX, bb.maxY, bb.maxZ, color, alpha, bufferbuilder);
        colorVertex(bb.maxX, bb.maxY, bb.maxZ, color, alpha, bufferbuilder);
        colorVertex(bb.maxX, bb.maxY, bb.minZ, color, alpha, bufferbuilder);
        colorVertex(bb.maxX, bb.minY, bb.minZ, color, color.getAlpha(), bufferbuilder);
        colorVertex(bb.maxX, bb.maxY, bb.minZ, color, alpha, bufferbuilder);
        colorVertex(bb.minX, bb.maxY, bb.minZ, color, alpha, bufferbuilder);
        tessellator.draw();
    }

    private static void colorVertex(double x, double y, double z, Colour color, int alpha, BufferBuilder bufferbuilder) {
        bufferbuilder.pos(x - RenderUtil.mc.getRenderManager().viewerPosX, y - RenderUtil.mc.getRenderManager().viewerPosY, z - RenderUtil.mc.getRenderManager().viewerPosZ).color(color.getRed(), color.getGreen(), color.getBlue(), alpha).endVertex();
    }

    private static void doVerticies(AxisAlignedBB axisAlignedBB, Colour color, int alpha, BufferBuilder bufferbuilder, int sides, boolean five) {
        if ((sides & 32) != 0 || sides == -1) {
            colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            if (five) {
                colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            }
        }

        if ((sides & 16) != 0 || sides == -1) {
            colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            if (five) {
                colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            }
        }

        if ((sides & 4) != 0 || sides == -1) {
            colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            if (five) {
                colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            }
        }

        if ((sides & 8) != 0 || sides == -1) {
            colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            if (five) {
                colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            }
        }

        if ((sides & 2) != 0 || sides == -1) {
            colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            if (five) {
                colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            }
        }

        if ((sides & 1) != 0 || sides == -1) {
            colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            if (five) {
                colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            }
        }

    }

    public static void drawBoundingBoxWithSides(BlockPos blockPos, int width, Colour color, int sides) {
        drawBoundingBoxWithSides(getBoundingBox(blockPos, 1.0D, 1.0D, 1.0D), width, color, color.getAlpha(), sides);
    }

    public static void drawBoundingBoxWithSides(BlockPos blockPos, int width, Colour color, int alpha, int sides) {
        drawBoundingBoxWithSides(getBoundingBox(blockPos, 1.0D, 1.0D, 1.0D), width, color, alpha, sides);
    }

    public static void drawBoundingBoxWithSides(AxisAlignedBB axisAlignedBB, int width, Colour color, int sides) {
        drawBoundingBoxWithSides(axisAlignedBB, width, color, color.getAlpha(), sides);
    }

    public static void drawBoundingBoxWithSides(AxisAlignedBB axisAlignedBB, int width, Colour color, int alpha, int sides) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        GlStateManager.glLineWidth((float) width);
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        doVerticies(axisAlignedBB, color, alpha, bufferbuilder, sides, true);
        tessellator.draw();
    }

    public static void drawText(BlockPos pos, String text, boolean custom) {
        if (pos != null && text != null) {
            GlStateManager.pushMatrix();
            glBillboardDistanceScaled((float) pos.getX() + 0.5F, (float) pos.getY() + 0.5F, (float) pos.getZ() + 0.5F, RenderUtil.mc.player, 1.0F);
            GlStateManager.disableDepth();
            GlStateManager.translate(-((double) OyVey.GUI_FONT_MANAGER.getTextWidth(text) / 2.0D), 0.0D, 0.0D);
            if (custom) {
                OyVey.GUI_FONT_MANAGER.drawStringWithShadow(text, 0.0F, 0.0F, -5592406);
            } else {
                RenderUtil.mc.fontRenderer.drawStringWithShadow(text, 0.0F, 0.0F, -5592406);
            }

            GlStateManager.popMatrix();
        }
    }

    public static void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player, float scale) {
        glBillboard(x, y, z);
        int distance = (int) player.getDistance((double) x, (double) y, (double) z);
        float scaleDistance = (float) distance / 2.0F / (2.0F + (2.0F - scale));

        if (scaleDistance < 1.0F) {
            scaleDistance = 1.0F;
        }

        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
    }

    public static void glBillboard(float x, float y, float z) {
        float scale = 0.02666667F;

        GlStateManager.translate((double) x - RenderUtil.mc.getRenderManager().renderPosX, (double) y - RenderUtil.mc.getRenderManager().renderPosY, (double) z - RenderUtil.mc.getRenderManager().renderPosZ);
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-RenderUtil.mc.player.rotationYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(RenderUtil.mc.player.rotationPitch, RenderUtil.mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);
    }

    public static void drawGlowBox(BlockPos blockPos, double height, Float lineWidth, Color color, Color outlineColor) {
        drawBoxESP(blockPos, outlineColor, lineWidth.floatValue(), true, false, outlineColor.getAlpha(), Double.valueOf(-1.0D));
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB((double) blockPos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double) blockPos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double) blockPos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double) (blockPos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double) (blockPos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double) (blockPos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);

        RenderBuilder.glSetup();
        RenderBuilder.glPrepare();
        drawSelectionGlowFilledBox(axisAlignedBB, height, new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()), new Color(color.getRed(), color.getGreen(), color.getBlue(), 0));
        RenderBuilder.glRestore();
        RenderBuilder.glRelease();
    }

    public static void drawSelectionGlowFilledBox(AxisAlignedBB axisAlignedBB, double height, Color startColor, Color endColor) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder BufferBuilder = Tessellator.getInstance().getBuffer();

        BufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        addChainedGlowBoxVertices(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.maxY + height, axisAlignedBB.maxZ, startColor, endColor);
        tessellator.draw();
    }

    public static void addChainedGlowBoxVertices(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color startColor, Color endColor) {
        BufferBuilder BufferBuilder = Tessellator.getInstance().getBuffer();

        BufferBuilder.pos(minX, minY, minZ).color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(maxX, minY, minZ).color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(maxX, minY, maxZ).color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(minX, minY, maxZ).color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(minX, maxY, minZ).color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(minX, maxY, maxZ).color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(maxX, maxY, maxZ).color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(maxX, maxY, minZ).color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(minX, minY, minZ).color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(minX, maxY, minZ).color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(maxX, maxY, minZ).color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(maxX, minY, minZ).color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(maxX, minY, minZ).color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(maxX, maxY, minZ).color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(maxX, maxY, maxZ).color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(maxX, minY, maxZ).color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(minX, minY, maxZ).color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(maxX, minY, maxZ).color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(maxX, maxY, maxZ).color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(minX, maxY, maxZ).color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(minX, minY, minZ).color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(minX, minY, maxZ).color((float) startColor.getRed() / 255.0F, (float) startColor.getGreen() / 255.0F, (float) startColor.getBlue() / 255.0F, (float) startColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(minX, maxY, maxZ).color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F).endVertex();
        BufferBuilder.pos(minX, maxY, minZ).color((float) endColor.getRed() / 255.0F, (float) endColor.getGreen() / 255.0F, (float) endColor.getBlue() / 255.0F, (float) endColor.getAlpha() / 255.0F).endVertex();
    }

    public static void drawGlError(BlockPos pos, double height, double length, double width, Color color) {
        AxisAlignedBB bb = new AxisAlignedBB((double) pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double) pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double) pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double) (pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double) (pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double) (pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);

        RenderUtil.camera.setPosition(((Entity) Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity())).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (RenderUtil.camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            Tessellator t = Tessellator.getInstance();
            BufferBuilder bufferbuilder = t.getBuffer();

            bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
            drawCornerVertices(bb.minX, bb.minY, bb.minZ, bb.maxX + length, bb.maxY + height, bb.maxZ + width, color);
            t.draw();
        }

    }

    public static void drawCorner(BlockPos pos, double height, double length, double width, Color color) {
        Tessellator t = Tessellator.getInstance();
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();

        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        AxisAlignedBB bb = getBoundingBox(pos);

        drawCornerVertices(bb.minX, bb.minY, bb.minZ, bb.maxX + length, bb.maxY + height, bb.maxZ + width, color);
        t.draw();
    }

    public static void drawCornerVertices(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color color) {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        buffer.pos(minX, minY, minZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(minX, minY, maxZ - 0.8D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, minY, maxZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(minX, minY, minZ + 0.8D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, minZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(maxX, minY, maxZ - 0.8D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, maxZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(maxX, minY, minZ + 0.8D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, minY, minZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(maxX - 0.8D, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, minY, maxZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(maxX - 0.8D, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, minZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(minX + 0.8D, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, maxZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(minX + 0.8D, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, minY, minZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(minX, minY + 0.2D, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, minY, maxZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(minX, minY + 0.2D, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, minZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(maxX, minY + 0.2D, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, maxZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(maxX, minY + 0.2D, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, minZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(minX, maxY, maxZ - 0.8D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, maxZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(minX, maxY, minZ + 0.8D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, minZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(maxX, maxY, maxZ - 0.8D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, maxZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(maxX, maxY, minZ + 0.8D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, minZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(maxX - 0.8D, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, maxZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(maxX - 0.8D, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, minZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(minX + 0.8D, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, maxZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(minX + 0.8D, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, minZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(minX, maxY - 0.2D, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, maxZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(minX, maxY - 0.2D, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, minZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(maxX, maxY - 0.2D, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, maxZ).color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 0.0F).endVertex();
        buffer.pos(maxX, maxY - 0.2D, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
    }

    public static void drawCircle(float x, float y, float z, float radius, Colour colour) {
        BlockPos pos = new BlockPos((double) x, (double) y, (double) z);
        AxisAlignedBB bb = new AxisAlignedBB((double) pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double) pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double) pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double) (pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double) (pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double) (pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);

        RenderUtil.camera.setPosition(((Entity) Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity())).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (RenderUtil.camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            drawCircleVertices(bb, radius, colour);
        }

    }

    public static void drawColumn(float x, float y, float z, float radius, Colour colour, int amount, double height) {
        double Hincrement = height / (double) amount;
        float Rincrement = radius / (float) amount * (float) height;
        BlockPos pos = new BlockPos((double) x, (double) y, (double) z);
        AxisAlignedBB bb = new AxisAlignedBB((double) pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double) pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double) pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double) (pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double) (pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double) (pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);

        RenderUtil.camera.setPosition(((Entity) Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity())).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (RenderUtil.camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            for (int i = 0; i <= amount; ++i) {
                bb = new AxisAlignedBB(bb.minX, bb.minY + Hincrement * (double) i, bb.minZ, bb.maxX, bb.maxY + Hincrement * (double) i, bb.maxZ);
                drawCircleVertices(bb, Rincrement * (float) i, colour);
            }
        }

    }

    public static void drawCircleVertices(AxisAlignedBB bb, float radius, Colour colour) {
        float r = (float) colour.getRed() / 255.0F;
        float g = (float) colour.getGreen() / 255.0F;
        float b = (float) colour.getBlue() / 255.0F;
        float a = (float) colour.getAlpha() / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(1.0F);

        for (int i = 0; i < 360; ++i) {
            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(bb.getCenter().x + Math.sin((double) i * 3.1415926D / 180.0D) * (double) radius, bb.minY, bb.getCenter().z + Math.cos((double) i * 3.1415926D / 180.0D) * (double) radius).color(r, g, b, a).endVertex();
            buffer.pos(bb.getCenter().x + Math.sin((double) (i + 1) * 3.1415926D / 180.0D) * (double) radius, bb.minY, bb.getCenter().z + Math.cos((double) (i + 1) * 3.1415926D / 180.0D) * (double) radius).color(r, g, b, a).endVertex();
            tessellator.draw();
        }

        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
