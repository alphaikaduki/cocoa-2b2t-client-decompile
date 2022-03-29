package me.alpha432.oyvey.features.modules.client;

import java.awt.Color;
import java.util.Objects;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class WaterMarkNew extends Module {

    public WaterMarkNew() {
        super("WaterMark2", "new troll", Module.Category.CLIENT, true, false, false);
    }

    @SubscribeEvent
    public void onRender(Post event) {
        if (!nullCheck()) {
            if (event.getType() == ElementType.HOTBAR) {
                String ping = this.getPing(WaterMarkNew.mc.player) + "PING";
                String fpsText = Minecraft.debugFPS + "FPS ";
                String server = Minecraft.getMinecraft().isSingleplayer() ? "SinglePlayer".toLowerCase() : WaterMarkNew.mc.getCurrentServerData().serverIP.toLowerCase();
                String text = "Crepe || " + server + " || " + ping + " || " + fpsText;
                float width = (float) (Minecraft.getMinecraft().fontRenderer.getStringWidth(text) + 6);
                byte height = 20;
                byte posX = 2;
                byte posY = 2;

                RenderUtil.drawRectangleCorrectly(posX - 4, posY - 4, (int) (width + 10.0F), height + 6, ColorUtil.toRGBA(0, 0, 0, 255));
                RenderUtil.drawRectangleCorrectly(posX - 4, posY - 4, (int) (width + 11.0F), height + 7, ColorUtil.toRGBA(25, 25, 25, 255));
                drawRect((double) posX, (double) posY, (double) ((float) posX + width + 2.0F), (double) (posY + height), (new Color(0, 0, 0, 178)).getRGB());
                drawRect((double) posX + 2.5D, (double) posY + 2.5D, (double) ((float) posX + width) - 0.5D, (double) posY + 4.5D, (new Color(0, 0, 0, 165)).getRGB());
                drawGradientSideways(4.0D, (double) (posY + 3), (double) (4.0F + width / 3.0F), (double) (posY + 4), (new Color(255, 255, 255, 255)).getRGB(), (new Color(255, 255, 255, 255)).getRGB());
                drawGradientSideways((double) (4.0F + width / 3.0F), (double) (posY + 3), (double) (4.0F + width / 3.0F * 2.0F), (double) (posY + 4), (new Color(255, 255, 255, 255)).getRGB(), (new Color(255, 252, 252, 255)).getRGB());
                drawGradientSideways((double) (4.0F + width / 3.0F * 2.0F), (double) (posY + 3), (double) (width / 3.0F * 3.0F + 1.0F), (double) (posY + 4), (new Color(255, 255, 255, 255)).getRGB(), (new Color(255, 255, 255, 255)).getRGB());
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, (float) (4 + posX), (float) (8 + posY), -1);
            }

        }
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        double f3;

        if (left < right) {
            f3 = left;
            left = right;
            right = f3;
        }

        if (top < bottom) {
            f3 = top;
            top = bottom;
            bottom = f3;
        }

        float f31 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f31);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(left, bottom, 0.0D).endVertex();
        bufferBuilder.pos(right, bottom, 0.0D).endVertex();
        bufferBuilder.pos(right, top, 0.0D).endVertex();
        bufferBuilder.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float) (col1 >> 24 & 255) / 255.0F;
        float f1 = (float) (col1 >> 16 & 255) / 255.0F;
        float f2 = (float) (col1 >> 8 & 255) / 255.0F;
        float f3 = (float) (col1 & 255) / 255.0F;
        float f4 = (float) (col2 >> 24 & 255) / 255.0F;
        float f5 = (float) (col2 >> 16 & 255) / 255.0F;
        float f6 = (float) (col2 >> 8 & 255) / 255.0F;
        float f7 = (float) (col2 & 255) / 255.0F;

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    private int getPing(EntityPlayer player) {
        int ping = 0;

        try {
            ping = (int) MathUtil.clamp((float) ((NetHandlerPlayClient) Objects.requireNonNull(WaterMarkNew.mc.getConnection())).getPlayerInfo(player.getUniqueID()).getResponseTime(), 1.0F, 9999999.0F);
        } catch (NullPointerException nullpointerexception) {
            ;
        }

        return ping;
    }
}
