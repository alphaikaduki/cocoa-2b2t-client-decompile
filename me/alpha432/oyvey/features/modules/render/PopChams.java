package me.alpha432.oyvey.features.modules.render;

import com.mojang.authlib.GameProfile;
import java.awt.Color;
import java.util.HashMap;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.misc.PopCounter;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.NordTessellator;
import me.alpha432.oyvey.util.TotemPopCham;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class PopChams extends Module {

    public static HashMap TotemPopContainer = new HashMap();
    private static PopCounter INSTANCE = new PopCounter();
    public static final Setting self = new Setting("Self", Boolean.valueOf(false));
    public static final Setting rL = new Setting("RedLine", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255));
    public static final Setting gL = new Setting("GreenLine", Integer.valueOf(26), Integer.valueOf(0), Integer.valueOf(255));
    public static final Setting bL = new Setting("BlueLine", Integer.valueOf(42), Integer.valueOf(0), Integer.valueOf(255));
    public static final Setting aL = new Setting("AlphaLine", Integer.valueOf(42), Integer.valueOf(0), Integer.valueOf(255));
    public static final Setting rF = new Setting("RedFill", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255));
    public static final Setting gF = new Setting("GreenFill", Integer.valueOf(26), Integer.valueOf(0), Integer.valueOf(255));
    public static final Setting bF = new Setting("BlueFill", Integer.valueOf(42), Integer.valueOf(0), Integer.valueOf(255));
    public static final Setting aF = new Setting("AlphaFill", Integer.valueOf(42), Integer.valueOf(0), Integer.valueOf(255));
    public static final Setting fadestart = new Setting("FadeStart", Integer.valueOf(200), Integer.valueOf(0), Integer.valueOf(3000));
    public static final Setting fadetime = new Setting("FadeStart", Double.valueOf(0.5D), Double.valueOf(0.0D), Double.valueOf(2.0D));
    public static final Setting onlyOneEsp = new Setting("OnlyOneEsp", Boolean.valueOf(true));
    public static final Setting rainbow = new Setting("Rainbow", Boolean.valueOf(false));
    EntityOtherPlayerMP player;
    ModelPlayer playerModel;
    Long startTime;
    double alphaFill;
    double alphaLine;

    public PopChams() {
        super("PopChams", "Renders when some1 pops", Module.Category.RENDER, true, false, false);
        this.register(PopChams.self);
        this.register(PopChams.rL);
        this.register(PopChams.gL);
        this.register(PopChams.bL);
        this.register(PopChams.aL);
        this.register(PopChams.rF);
        this.register(PopChams.gF);
        this.register(PopChams.bF);
        this.register(PopChams.aF);
        this.register(PopChams.fadestart);
        this.register(PopChams.fadetime);
        this.register(PopChams.onlyOneEsp);
        this.register(PopChams.rainbow);
    }

    @SubscribeEvent
    public void onPacketReceived(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();

            if (packet.getOpCode() == 35 && packet.getEntity(PopChams.mc.world) != null && (((Boolean) PopChams.self.getValue()).booleanValue() || packet.getEntity(PopChams.mc.world).getEntityId() != PopChams.mc.player.getEntityId())) {
                GameProfile profile = new GameProfile(PopChams.mc.player.getUniqueID(), "");

                (this.player = new EntityOtherPlayerMP(PopChams.mc.world, profile)).copyLocationAndAnglesFrom(packet.getEntity(PopChams.mc.world));
                this.playerModel = new ModelPlayer(0.0F, false);
                this.startTime = Long.valueOf(System.currentTimeMillis());
                this.playerModel.bipedHead.showModel = false;
                this.playerModel.bipedBody.showModel = false;
                this.playerModel.bipedLeftArmwear.showModel = false;
                this.playerModel.bipedLeftLegwear.showModel = false;
                this.playerModel.bipedRightArmwear.showModel = false;
                this.playerModel.bipedRightLegwear.showModel = false;
                this.alphaFill = (double) ((Integer) PopChams.aF.getValue()).intValue();
                this.alphaLine = (double) ((Integer) PopChams.aL.getValue()).intValue();
                if (!((Boolean) PopChams.onlyOneEsp.getValue()).booleanValue()) {
                    new TotemPopCham(this.player, this.playerModel, this.startTime, this.alphaFill, this.alphaLine);
                }
            }
        }

    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (((Boolean) PopChams.onlyOneEsp.getValue()).booleanValue()) {
            if (this.player == null || PopChams.mc.world == null || PopChams.mc.player == null) {
                return;
            }

            GL11.glLineWidth(1.0F);
            Color lineColorS = new Color(((Integer) PopChams.rL.getValue()).intValue(), ((Integer) PopChams.bL.getValue()).intValue(), ((Integer) PopChams.gL.getValue()).intValue(), ((Integer) PopChams.aL.getValue()).intValue());
            Color fillColorS = new Color(((Integer) PopChams.rF.getValue()).intValue(), ((Integer) PopChams.bF.getValue()).intValue(), ((Integer) PopChams.gF.getValue()).intValue(), ((Integer) PopChams.aF.getValue()).intValue());
            int lineA = lineColorS.getAlpha();
            int fillA = fillColorS.getAlpha();
            long time = System.currentTimeMillis() - this.startTime.longValue() - ((Number) PopChams.fadestart.getValue()).longValue();

            if (System.currentTimeMillis() - this.startTime.longValue() > ((Number) PopChams.fadestart.getValue()).longValue()) {
                double lineColor = this.normalize((double) time, 0.0D, ((Number) PopChams.fadetime.getValue()).doubleValue());

                lineColor = MathHelper.clamp(lineColor, 0.0D, 1.0D);
                lineColor = -lineColor + 1.0D;
                lineA *= (int) lineColor;
                fillA *= (int) lineColor;
            }

            Color lineColor1 = newAlpha(lineColorS, lineA);
            Color fillColor = newAlpha(fillColorS, fillA);

            if (this.player != null && this.playerModel != null) {
                NordTessellator.prepareGL();
                GL11.glPushAttrib(1048575);
                GL11.glEnable(2881);
                GL11.glEnable(2848);
                if (this.alphaFill > 1.0D) {
                    this.alphaFill -= ((Double) PopChams.fadetime.getValue()).doubleValue();
                }

                Color fillFinal = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), (int) this.alphaFill);

                if (this.alphaLine > 1.0D) {
                    this.alphaLine -= ((Double) PopChams.fadetime.getValue()).doubleValue();
                }

                Color outlineFinal = new Color(lineColor1.getRed(), lineColor1.getGreen(), lineColor1.getBlue(), (int) this.alphaLine);

                glColor(fillFinal);
                GL11.glPolygonMode(1032, 6914);
                renderEntity(this.player, this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, (float) this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1.0F);
                glColor(outlineFinal);
                GL11.glPolygonMode(1032, 6913);
                renderEntity(this.player, this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, (float) this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1.0F);
                GL11.glPolygonMode(1032, 6914);
                GL11.glPopAttrib();
                NordTessellator.releaseGL();
            }
        }

    }

    double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }

    public static void renderEntity(EntityLivingBase entity, ModelBase modelBase, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (PopChams.mc.getRenderManager() != null) {
            float partialTicks = PopChams.mc.getRenderPartialTicks();
            double x = entity.posX - PopChams.mc.getRenderManager().viewerPosX;
            double y = entity.posY - PopChams.mc.getRenderManager().viewerPosY;
            double z = entity.posZ - PopChams.mc.getRenderManager().viewerPosZ;

            GlStateManager.pushMatrix();
            if (entity.isSneaking()) {
                y -= 0.125D;
            }

            float interpolateRotation = interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
            float interpolateRotation2 = interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
            float f = interpolateRotation2 - interpolateRotation;

            f = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
            renderLivingAt(x, y, z);
            float f8 = handleRotationFloat(entity, partialTicks);

            prepareRotations(entity);
            float f9 = prepareScale(entity, scale);

            GlStateManager.enableAlpha();
            modelBase.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
            modelBase.setRotationAngles(limbSwing, limbSwingAmount, f8, entity.rotationYaw, entity.rotationPitch, f9, entity);
            modelBase.render(entity, limbSwing, limbSwingAmount, f8, entity.rotationYaw, entity.rotationPitch, f9);
            GlStateManager.popMatrix();
        }
    }

    public static void prepareTranslate(EntityLivingBase entityIn, double x, double y, double z) {
        renderLivingAt(x - PopChams.mc.getRenderManager().viewerPosX, y - PopChams.mc.getRenderManager().viewerPosY, z - PopChams.mc.getRenderManager().viewerPosZ);
    }

    public static void renderLivingAt(double x, double y, double z) {
        GlStateManager.translate((float) x, (float) y, (float) z);
    }

    public static float prepareScale(EntityLivingBase entity, float scale) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        double widthX = entity.getRenderBoundingBox().maxX - entity.getRenderBoundingBox().minX;
        double widthZ = entity.getRenderBoundingBox().maxZ - entity.getRenderBoundingBox().minZ;

        GlStateManager.scale((double) scale + widthX, (double) (scale * entity.height), (double) scale + widthZ);
        float f = 0.0625F;

        GlStateManager.translate(0.0F, -1.501F, 0.0F);
        return 0.0625F;
    }

    public static void prepareRotations(EntityLivingBase entityLivingBase) {
        GlStateManager.rotate(180.0F - entityLivingBase.rotationYaw, 0.0F, 1.0F, 0.0F);
    }

    public static float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
        float f;

        for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F) {
            ;
        }

        while (f >= 180.0F) {
            f -= 360.0F;
        }

        return prevYawOffset + partialTicks * f;
    }

    public static Color newAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
    }

    public static float handleRotationFloat(EntityLivingBase livingBase, float partialTicks) {
        return 0.0F;
    }
}
