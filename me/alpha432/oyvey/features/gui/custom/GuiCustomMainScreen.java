package me.alpha432.oyvey.features.gui.custom;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Desktop.Action;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.net.URI;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiCustomMainScreen extends GuiScreen {

    private final ResourceLocation resourceLocation = new ResourceLocation("textures/back.png");
    private int y;
    private int x;
    private int singleplayerWidth;
    private int multiplayerWidth;
    private int settingsWidth;
    private int discordWidth;
    private int exitWidth;
    private int textHeight;
    private float xOffset;
    private float yOffset;
    private boolean particleSystem;

    public static void drawCompleteImage(float posX, float posY, float width, float height) {
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, 0.0F);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0F, 0.0F);
        GL11.glVertex3f(0.0F, 0.0F, 0.0F);
        GL11.glTexCoord2f(0.0F, 1.0F);
        GL11.glVertex3f(0.0F, height, 0.0F);
        GL11.glTexCoord2f(1.0F, 1.0F);
        GL11.glVertex3f(width, height, 0.0F);
        GL11.glTexCoord2f(1.0F, 0.0F);
        GL11.glVertex3f(width, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static boolean isHovered(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY < y + height;
    }

    private void playMusic() {
        if (!this.mc.soundHandler.isSoundPlaying(OyVey.SONG_MANAGER.getMenuSong())) {
            this.mc.soundHandler.playSound(OyVey.SONG_MANAGER.getMenuSong());
        }

    }

    public void initGui() {
        this.x = this.width / 14;
        this.y = this.height / 14 + 62;
        this.playMusic();
        this.buttonList.add(new GuiCustomMainScreen.TextButton(0, this.x, this.y + 20, "Single"));
        this.buttonList.add(new GuiCustomMainScreen.TextButton(0, this.x, this.y + 44, "Online"));
        this.buttonList.add(new GuiCustomMainScreen.TextButton(0, this.x, this.y + 66, "Settings"));
        this.buttonList.add(new GuiCustomMainScreen.TextButton(0, this.x, this.y + 88, "Discord"));
        this.buttonList.add(new GuiCustomMainScreen.TextButton(0, this.x, this.y + 132, "Log"));
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public void updateScreen() {
        super.updateScreen();
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(this.x, this.y + 20, OyVey.textManager.getStringWidth("Single") / 4, OyVey.textManager.getFontHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen(new GuiWorldSelection(this));
        } else if (isHovered(this.x, this.y + 44, OyVey.textManager.getStringWidth("Online") / 4, OyVey.textManager.getFontHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        } else if (isHovered(this.x, this.y + 66, OyVey.textManager.getStringWidth("Settings") / 4, OyVey.textManager.getFontHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        } else if (isHovered(this.x, this.y + 132, OyVey.textManager.getStringWidth("Log") / 4, OyVey.textManager.getFontHeight(), mouseX, mouseY)) {
            this.mc.shutdown();
        } else if (isHovered(this.x, this.y + 88, OyVey.textManager.getStringWidth("Discord") / 4, OyVey.textManager.getFontHeight(), mouseX, mouseY)) {
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI("https://discord.gg/ZxjwX4FaDh"));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.x = this.width / 16;
        this.y = this.height / 16 + 64;
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        this.mc.getTextureManager().bindTexture(this.resourceLocation);
        drawCompleteImage(-16.0F + this.xOffset, -9.0F + this.yOffset, (float) (this.width + 32), (float) (this.height + 18));
        RenderUtil.drawRect(this.xOffset, this.yOffset, 70.0F, 1000.0F, ColorUtil.toRGBA(20, 20, 20, 70));
        super.drawScreen(970, 540, partialTicks);
        OyVey.textManager.drawStringBig("KuroHack ", (float) this.x, (float) this.y - 10.0F, Color.WHITE.getRGB(), true);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public BufferedImage parseBackground(BufferedImage background) {
        int width = 1920;
        int srcWidth = background.getWidth();
        int srcHeight = background.getHeight();

        int height;

        for (height = 1080; width < srcWidth || height < srcHeight; height *= 2) {
            width *= 2;
        }

        BufferedImage imgNew = new BufferedImage(width, height, 2);
        Graphics g = imgNew.getGraphics();

        g.drawImage(background, 0, 0, (ImageObserver) null);
        g.dispose();
        return imgNew;
    }

    private static class TextButton extends GuiButton {

        public TextButton(int buttonId, int x, int y, String buttonText) {
            super(buttonId, x, y, OyVey.textManager.getStringWidth(buttonText), OyVey.textManager.getFontHeight(), buttonText);
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                this.enabled = true;
                this.hovered = (float) mouseX >= (float) this.x - (float) OyVey.textManager.getStringWidth(this.displayString) / 2.0F && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                OyVey.textManager.drawStringWithShadow(this.displayString, (float) this.x - (float) OyVey.textManager.getStringWidth(this.displayString) / 2.0F, (float) this.y, Color.WHITE.getRGB());
                if (this.hovered) {
                    RenderUtil.drawLine((float) this.x - 2.0F - (float) OyVey.textManager.getStringWidth(this.displayString) / 2.0F, (float) (this.y + 2 + OyVey.textManager.getFontHeight()), (float) this.x + (float) OyVey.textManager.getStringWidth(this.displayString) / 2.0F + 1.0F, (float) (this.y + 2 + OyVey.textManager.getFontHeight()), 1.0F, Color.WHITE.getRGB());
                    OyVey.textManager.drawStringSmall("Click me!", (float) this.x, (float) this.y - 10.0F, Color.WHITE.getRGB(), true);
                }
            }

        }

        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            return this.enabled && this.visible && (float) mouseX >= (float) this.x - (float) OyVey.textManager.getStringWidth(this.displayString) / 2.0F && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        }
    }
}
