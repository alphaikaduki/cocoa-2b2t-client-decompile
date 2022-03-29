package me.alpha432.oyvey.features.modules.render;

import java.awt.Color;
import java.util.Iterator;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.event.events.RenderEntityModelEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.Colors;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class ESP extends Module {

    private static ESP INSTANCE = new ESP();
    private final Setting mode;
    private final Setting colorSync;
    private final Setting players;
    private final Setting animals;
    private final Setting mobs;
    private final Setting items;
    private final Setting xporbs;
    private final Setting xpbottles;
    private final Setting pearl;
    private final Setting red;
    private final Setting green;
    private final Setting blue;
    private final Setting boxAlpha;
    private final Setting alpha;
    private final Setting lineWidth;
    private final Setting colorFriends;
    private final Setting self;
    private final Setting onTop;
    private final Setting invisibles;

    public ESP() {
        super("ESP", "Renders a nice ESP.", Module.Category.RENDER, false, false, false);
        this.mode = this.register(new Setting("Mode", ESP.Mode.OUTLINE));
        this.colorSync = this.register(new Setting("Sync", Boolean.valueOf(false)));
        this.players = this.register(new Setting("Players", Boolean.valueOf(true)));
        this.animals = this.register(new Setting("Animals", Boolean.valueOf(false)));
        this.mobs = this.register(new Setting("Mobs", Boolean.valueOf(false)));
        this.items = this.register(new Setting("Items", Boolean.valueOf(false)));
        this.xporbs = this.register(new Setting("XpOrbs", Boolean.valueOf(false)));
        this.xpbottles = this.register(new Setting("XpBottles", Boolean.valueOf(false)));
        this.pearl = this.register(new Setting("Pearls", Boolean.valueOf(false)));
        this.red = this.register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
        this.green = this.register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
        this.blue = this.register(new Setting("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
        this.boxAlpha = this.register(new Setting("BoxAlpha", Integer.valueOf(120), Integer.valueOf(0), Integer.valueOf(255)));
        this.alpha = this.register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
        this.lineWidth = this.register(new Setting("LineWidth", Float.valueOf(2.0F), Float.valueOf(0.1F), Float.valueOf(5.0F)));
        this.colorFriends = this.register(new Setting("Friends", Boolean.valueOf(true)));
        this.self = this.register(new Setting("Self", Boolean.valueOf(true)));
        this.onTop = this.register(new Setting("onTop", Boolean.valueOf(true)));
        this.invisibles = this.register(new Setting("Invisibles", Boolean.valueOf(false)));
        this.setInstance();
    }

    public static ESP getInstance() {
        if (ESP.INSTANCE == null) {
            ESP.INSTANCE = new ESP();
        }

        return ESP.INSTANCE;
    }

    private void setInstance() {
        ESP.INSTANCE = this;
    }

    public void onRender3D(Render3DEvent event) {
        AxisAlignedBB bb;
        Vec3d interp;
        int i;
        Iterator iterator;
        Entity entity;

        if (((Boolean) this.items.getValue()).booleanValue()) {
            i = 0;
            iterator = ESP.mc.world.loadedEntityList.iterator();

            while (iterator.hasNext()) {
                entity = (Entity) iterator.next();
                if (entity instanceof EntityItem && ESP.mc.player.getDistanceSq(entity) < 2500.0D) {
                    interp = EntityUtil.getInterpolatedRenderPos(entity, ESP.mc.getRenderPartialTicks());
                    bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05D - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0D - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05D - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05D - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1D - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05D - entity.posZ + interp.z);
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0F);
                    RenderGlobal.renderFilledBox(bb, ((Boolean) this.colorSync.getValue()).booleanValue() ? (float) Colors.INSTANCE.getCurrentColor().getRed() / 255.0F : (float) ((Integer) this.red.getValue()).intValue() / 255.0F, ((Boolean) this.colorSync.getValue()).booleanValue() ? (float) Colors.INSTANCE.getCurrentColor().getGreen() / 255.0F : (float) ((Integer) this.green.getValue()).intValue() / 255.0F, ((Boolean) this.colorSync.getValue()).booleanValue() ? (float) Colors.INSTANCE.getCurrentColor().getBlue() / 255.0F : (float) ((Integer) this.blue.getValue()).intValue() / 255.0F, ((Boolean) this.colorSync.getValue()).booleanValue() ? (float) Colors.INSTANCE.getCurrentColor().getAlpha() : (float) ((Integer) this.boxAlpha.getValue()).intValue() / 255.0F);
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtil.drawBlockOutline(bb, ((Boolean) this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(((Integer) this.red.getValue()).intValue(), ((Integer) this.green.getValue()).intValue(), ((Integer) this.blue.getValue()).intValue(), ((Integer) this.alpha.getValue()).intValue()), 1.0F);
                    ++i;
                    if (i >= 50) {
                        break;
                    }
                }
            }
        }

        if (((Boolean) this.xporbs.getValue()).booleanValue()) {
            i = 0;
            iterator = ESP.mc.world.loadedEntityList.iterator();

            while (iterator.hasNext()) {
                entity = (Entity) iterator.next();
                if (entity instanceof EntityXPOrb && ESP.mc.player.getDistanceSq(entity) < 2500.0D) {
                    interp = EntityUtil.getInterpolatedRenderPos(entity, ESP.mc.getRenderPartialTicks());
                    bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05D - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0D - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05D - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05D - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1D - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05D - entity.posZ + interp.z);
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0F);
                    RenderGlobal.renderFilledBox(bb, ((Boolean) this.colorSync.getValue()).booleanValue() ? (float) Colors.INSTANCE.getCurrentColor().getRed() / 255.0F : (float) ((Integer) this.red.getValue()).intValue() / 255.0F, ((Boolean) this.colorSync.getValue()).booleanValue() ? (float) Colors.INSTANCE.getCurrentColor().getGreen() / 255.0F : (float) ((Integer) this.green.getValue()).intValue() / 255.0F, ((Boolean) this.colorSync.getValue()).booleanValue() ? (float) Colors.INSTANCE.getCurrentColor().getBlue() / 255.0F : (float) ((Integer) this.blue.getValue()).intValue() / 255.0F, ((Boolean) this.colorSync.getValue()).booleanValue() ? (float) Colors.INSTANCE.getCurrentColor().getAlpha() / 255.0F : (float) ((Integer) this.boxAlpha.getValue()).intValue() / 255.0F);
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtil.drawBlockOutline(bb, ((Boolean) this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(((Integer) this.red.getValue()).intValue(), ((Integer) this.green.getValue()).intValue(), ((Integer) this.blue.getValue()).intValue(), ((Integer) this.alpha.getValue()).intValue()), 1.0F);
                    ++i;
                    if (i >= 50) {
                        break;
                    }
                }
            }
        }

        if (((Boolean) this.pearl.getValue()).booleanValue()) {
            i = 0;
            iterator = ESP.mc.world.loadedEntityList.iterator();

            while (iterator.hasNext()) {
                entity = (Entity) iterator.next();
                if (entity instanceof EntityEnderPearl && ESP.mc.player.getDistanceSq(entity) < 2500.0D) {
                    interp = EntityUtil.getInterpolatedRenderPos(entity, ESP.mc.getRenderPartialTicks());
                    bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05D - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0D - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05D - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05D - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1D - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05D - entity.posZ + interp.z);
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0F);
                    RenderGlobal.renderFilledBox(bb, ((Boolean) this.colorSync.getValue()).booleanValue() ? (float) Colors.INSTANCE.getCurrentColor().getRed() / 255.0F : (float) ((Integer) this.red.getValue()).intValue() / 255.0F, ((Boolean) this.colorSync.getValue()).booleanValue() ? (float) Colors.INSTANCE.getCurrentColor().getGreen() / 255.0F : (float) ((Integer) this.green.getValue()).intValue() / 255.0F, ((Boolean) this.colorSync.getValue()).booleanValue() ? (float) Colors.INSTANCE.getCurrentColor().getBlue() / 255.0F : (float) ((Integer) this.blue.getValue()).intValue() / 255.0F, ((Boolean) this.colorSync.getValue()).booleanValue() ? (float) Colors.INSTANCE.getCurrentColor().getAlpha() / 255.0F : (float) ((Integer) this.boxAlpha.getValue()).intValue() / 255.0F);
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtil.drawBlockOutline(bb, ((Boolean) this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(((Integer) this.red.getValue()).intValue(), ((Integer) this.green.getValue()).intValue(), ((Integer) this.blue.getValue()).intValue(), ((Integer) this.alpha.getValue()).intValue()), 1.0F);
                    ++i;
                    if (i >= 50) {
                        break;
                    }
                }
            }
        }

        if (((Boolean) this.xpbottles.getValue()).booleanValue()) {
            i = 0;
            iterator = ESP.mc.world.loadedEntityList.iterator();

            while (iterator.hasNext()) {
                entity = (Entity) iterator.next();
                if (entity instanceof EntityExpBottle && ESP.mc.player.getDistanceSq(entity) < 2500.0D) {
                    interp = EntityUtil.getInterpolatedRenderPos(entity, ESP.mc.getRenderPartialTicks());
                    bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05D - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0D - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05D - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05D - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1D - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05D - entity.posZ + interp.z);
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0F);
                    RenderGlobal.renderFilledBox(bb, ((Boolean) this.colorSync.getValue()).booleanValue() ? (float) Colors.INSTANCE.getCurrentColor().getRed() / 255.0F : (float) ((Integer) this.red.getValue()).intValue() / 255.0F, ((Boolean) this.colorSync.getValue()).booleanValue() ? (float) Colors.INSTANCE.getCurrentColor().getGreen() / 255.0F : (float) ((Integer) this.green.getValue()).intValue() / 255.0F, ((Boolean) this.colorSync.getValue()).booleanValue() ? (float) Colors.INSTANCE.getCurrentColor().getBlue() / 255.0F : (float) ((Integer) this.blue.getValue()).intValue() / 255.0F, ((Boolean) this.colorSync.getValue()).booleanValue() ? (float) Colors.INSTANCE.getCurrentColor().getAlpha() / 255.0F : (float) ((Integer) this.boxAlpha.getValue()).intValue() / 255.0F);
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtil.drawBlockOutline(bb, ((Boolean) this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(((Integer) this.red.getValue()).intValue(), ((Integer) this.green.getValue()).intValue(), ((Integer) this.blue.getValue()).intValue(), ((Integer) this.alpha.getValue()).intValue()), 1.0F);
                    ++i;
                    if (i >= 50) {
                        break;
                    }
                }
            }
        }

    }

    public void onRenderModel(RenderEntityModelEvent event) {
        if (event.getStage() == 0 && event.entity != null && (!event.entity.isInvisible() || ((Boolean) this.invisibles.getValue()).booleanValue()) && (((Boolean) this.self.getValue()).booleanValue() || !event.entity.equals(ESP.mc.player)) && (((Boolean) this.players.getValue()).booleanValue() || !(event.entity instanceof EntityPlayer)) && (((Boolean) this.animals.getValue()).booleanValue() || !EntityUtil.isPassive(event.entity)) && (((Boolean) this.mobs.getValue()).booleanValue() || EntityUtil.isPassive(event.entity) || event.entity instanceof EntityPlayer)) {
            Color color = ((Boolean) this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(event.entity, ((Integer) this.red.getValue()).intValue(), ((Integer) this.green.getValue()).intValue(), ((Integer) this.blue.getValue()).intValue(), ((Integer) this.alpha.getValue()).intValue(), ((Boolean) this.colorFriends.getValue()).booleanValue());
            boolean fancyGraphics = ESP.mc.gameSettings.fancyGraphics;

            ESP.mc.gameSettings.fancyGraphics = false;
            float gamma = ESP.mc.gameSettings.gammaSetting;

            ESP.mc.gameSettings.gammaSetting = 10000.0F;
            if (((Boolean) this.onTop.getValue()).booleanValue() && (!Chams.getInstance().isEnabled() || !((Boolean) Chams.getInstance().colored.getValue()).booleanValue())) {
                event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            }

            if (this.mode.getValue() == ESP.Mode.OUTLINE) {
                RenderUtil.renderOne(((Float) this.lineWidth.getValue()).floatValue());
                event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
                GlStateManager.glLineWidth(((Float) this.lineWidth.getValue()).floatValue());
                RenderUtil.renderTwo();
                event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
                GlStateManager.glLineWidth(((Float) this.lineWidth.getValue()).floatValue());
                RenderUtil.renderThree();
                RenderUtil.renderFour(color);
                event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
                GlStateManager.glLineWidth(((Float) this.lineWidth.getValue()).floatValue());
                RenderUtil.renderFive();
            } else {
                GL11.glPushMatrix();
                GL11.glPushAttrib(1048575);
                if (this.mode.getValue() == ESP.Mode.WIREFRAME) {
                    GL11.glPolygonMode(1032, 6913);
                } else {
                    GL11.glPolygonMode(1028, 6913);
                }

                GL11.glDisable(3553);
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                GL11.glEnable(2848);
                GL11.glEnable(3042);
                GlStateManager.blendFunc(770, 771);
                GlStateManager.color((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
                GlStateManager.glLineWidth(((Float) this.lineWidth.getValue()).floatValue());
                event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }

            if (!((Boolean) this.onTop.getValue()).booleanValue() && (!Chams.getInstance().isEnabled() || !((Boolean) Chams.getInstance().colored.getValue()).booleanValue())) {
                event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            }

            try {
                ESP.mc.gameSettings.fancyGraphics = fancyGraphics;
                ESP.mc.gameSettings.gammaSetting = gamma;
            } catch (Exception exception) {
                ;
            }

            event.setCanceled(true);
        }
    }

    public static enum Mode {

        WIREFRAME, OUTLINE;
    }
}
