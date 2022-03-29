package me.alpha432.oyvey.features.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.DamageUtil;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.RotationUtil;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import org.lwjgl.opengl.GL11;

public class TestNametags extends Module {

    public static TestNametags INSTANCE;
    private final Setting health;
    private final Setting armor;
    private final Setting reverseArmor;
    private final Setting topEnchant;
    private final Setting scaling;
    private final Setting invisibles;
    private final Setting ping;
    private final Setting gamemode;
    private final Setting entityID;
    private final Setting rect;
    private final Setting outline;
    private final Setting redSetting;
    private final Setting greenSetting;
    private final Setting blueSetting;
    private final Setting alphaSetting;
    private final Setting lineWidth;
    private final Setting sneak;
    private final Setting heldStackName;
    private final Setting whiter;
    private final Setting onlyFov;
    private final Setting scaleing;
    private final Setting factor;
    private final Setting smartScale;

    public TestNametags() {
        super("Nametag", "Let\'s try to fix nametags!", Module.Category.RENDER, true, false, true);
        this.health = this.register(new Setting("Health", Boolean.TRUE));
        this.armor = this.register(new Setting("Armor", Boolean.TRUE));
        this.reverseArmor = this.register(new Setting("ReverseArmor", Boolean.FALSE));
        this.topEnchant = this.register(new Setting("TopEnchant", Boolean.FALSE));
        this.scaling = this.register(new Setting("Size", Float.valueOf(0.3F), Float.valueOf(0.1F), Float.valueOf(20.0F)));
        this.invisibles = this.register(new Setting("Invisibles", Boolean.FALSE));
        this.ping = this.register(new Setting("Ping", Boolean.TRUE));
        this.gamemode = this.register(new Setting("Gamemode", Boolean.FALSE));
        this.entityID = this.register(new Setting("ID", Boolean.FALSE));
        this.rect = this.register(new Setting("Rectangle", Boolean.TRUE));
        this.outline = this.register(new Setting("Outline", Boolean.FALSE, (v) -> {
            return ((Boolean) this.rect.getValue()).booleanValue();
        }));
        this.redSetting = this.register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), (v) -> {
            return ((Boolean) this.outline.getValue()).booleanValue();
        }));
        this.greenSetting = this.register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), (v) -> {
            return ((Boolean) this.outline.getValue()).booleanValue();
        }));
        this.blueSetting = this.register(new Setting("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), (v) -> {
            return ((Boolean) this.outline.getValue()).booleanValue();
        }));
        this.alphaSetting = this.register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), (v) -> {
            return ((Boolean) this.outline.getValue()).booleanValue();
        }));
        this.lineWidth = this.register(new Setting("LineWidth", Float.valueOf(1.5F), Float.valueOf(0.1F), Float.valueOf(5.0F), (v) -> {
            return ((Boolean) this.outline.getValue()).booleanValue();
        }));
        this.sneak = this.register(new Setting("SneakColor", Boolean.FALSE));
        this.heldStackName = this.register(new Setting("StackName", Boolean.FALSE));
        this.whiter = this.register(new Setting("White", Boolean.FALSE));
        this.onlyFov = this.register(new Setting("OnlyFov", Boolean.FALSE));
        this.scaleing = this.register(new Setting("Scale", Boolean.FALSE));
        this.factor = this.register(new Setting("Factor", Float.valueOf(0.3F), Float.valueOf(0.1F), Float.valueOf(1.0F), (v) -> {
            return ((Boolean) this.scaleing.getValue()).booleanValue();
        }));
        this.smartScale = this.register(new Setting("SmartScale", Boolean.FALSE, (v) -> {
            return ((Boolean) this.scaleing.getValue()).booleanValue();
        }));
        TestNametags.INSTANCE = this;
    }

    public static TestNametags getInstance() {
        if (TestNametags.INSTANCE == null) {
            TestNametags.INSTANCE = new TestNametags();
        }

        return TestNametags.INSTANCE;
    }

    public void onRender3D(Render3DEvent event) {
        if (!fullNullCheck()) {
            Iterator iterator = TestNametags.mc.world.playerEntities.iterator();

            while (iterator.hasNext()) {
                EntityPlayer player = (EntityPlayer) iterator.next();

                if (player != null && !player.equals(TestNametags.mc.player) && player.isEntityAlive() && (!player.isInvisible() || ((Boolean) this.invisibles.getValue()).booleanValue()) && (!((Boolean) this.onlyFov.getValue()).booleanValue() || RotationUtil.isInFov(player))) {
                    double x = this.interpolate(player.lastTickPosX, player.posX, event.getPartialTicks()) - TestNametags.mc.getRenderManager().renderPosX;
                    double y = this.interpolate(player.lastTickPosY, player.posY, event.getPartialTicks()) - TestNametags.mc.getRenderManager().renderPosY;
                    double z = this.interpolate(player.lastTickPosZ, player.posZ, event.getPartialTicks()) - TestNametags.mc.getRenderManager().renderPosZ;

                    this.renderProperNameTag(player, x, y, z, event.getPartialTicks());
                }
            }
        }

    }

    private void renderProperNameTag(EntityPlayer player, double x, double y, double z, float delta) {
        double tempY = y + (player.isSneaking() ? 0.5D : 0.7D);
        Entity camera = TestNametags.mc.getRenderViewEntity();

        assert camera != null;

        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;

        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
        String displayTag = this.getDisplayTag(player);
        double distance = camera.getDistance(x + TestNametags.mc.getRenderManager().viewerPosX, y + TestNametags.mc.getRenderManager().viewerPosY, z + TestNametags.mc.getRenderManager().viewerPosZ);
        int width = this.renderer.getStringWidth(displayTag) / 2;
        double scale = (0.0018D + (double) ((Float) this.scaling.getValue()).floatValue() * distance * (double) ((Float) this.factor.getValue()).floatValue()) / 1000.0D;

        if (distance <= 8.0D && ((Boolean) this.smartScale.getValue()).booleanValue()) {
            scale = 0.0245D;
        }

        if (!((Boolean) this.scaleing.getValue()).booleanValue()) {
            scale = (double) ((Float) this.scaling.getValue()).floatValue() / 100.0D;
        }

        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0F, -1500000.0F);
        GlStateManager.disableLighting();
        GlStateManager.translate((float) x, (float) tempY + 1.4F, (float) z);
        GlStateManager.rotate(-TestNametags.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        float f = TestNametags.mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F;

        GlStateManager.rotate(TestNametags.mc.getRenderManager().playerViewX, f, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        if (((Boolean) this.rect.getValue()).booleanValue()) {
            this.drawRect((float) (-width - 2), (float) (-(TestNametags.mc.fontRenderer.FONT_HEIGHT + 1)), (float) width + 2.0F, 1.5F, 1426063360);
            if (((Boolean) this.outline.getValue()).booleanValue()) {
                ;
            }
        }

        GlStateManager.enableAlpha();
        ItemStack renderMainHand = player.getHeldItemMainhand().copy();

        if (renderMainHand.hasEffect() && !(renderMainHand.getItem() instanceof ItemTool) && renderMainHand.getItem() instanceof ItemArmor) {
            ;
        }

        int xOffset;

        if (((Boolean) this.heldStackName.getValue()).booleanValue() && !renderMainHand.isEmpty && renderMainHand.getItem() != Items.AIR) {
            String armorInventory = renderMainHand.getDisplayName();

            xOffset = this.renderer.getStringWidth(armorInventory) / 2;
            GL11.glPushMatrix();
            GL11.glScalef(0.75F, 0.75F, 0.0F);
            this.renderer.drawStringWithShadow(armorInventory, (float) (-xOffset), -(this.getBiggestArmorTag(player) + 20.0F), -1);
            GL11.glScalef(1.5F, 1.5F, 1.0F);
            GL11.glPopMatrix();
        }

        ArrayList armorInventory1 = new ArrayList(player.inventory.armorInventory);

        if (((Boolean) this.reverseArmor.getValue()).booleanValue()) {
            Collections.reverse(armorInventory1);
        }

        GlStateManager.pushMatrix();
        xOffset = -8;
        Iterator renderOffhand = armorInventory1.iterator();

        while (renderOffhand.hasNext()) {
            ItemStack stack = (ItemStack) renderOffhand.next();

            if (stack != null) {
                xOffset -= 8;
            }
        }

        xOffset -= 8;
        ItemStack renderOffhand1 = player.getHeldItemOffhand().copy();

        if (renderOffhand1.hasEffect() && !(renderOffhand1.getItem() instanceof ItemTool) && renderOffhand1.getItem() instanceof ItemArmor) {
            ;
        }

        this.renderItemStack(player, renderOffhand1, xOffset, -26, ((Boolean) this.armor.getValue()).booleanValue());
        xOffset += 16;
        Iterator stack2 = armorInventory1.iterator();

        while (stack2.hasNext()) {
            ItemStack stack1 = (ItemStack) stack2.next();

            if (stack1 != null) {
                ItemStack armourStack = stack1.copy();

                if (armourStack.hasEffect() && !(armourStack.getItem() instanceof ItemTool) && armourStack.getItem() instanceof ItemArmor) {
                    ;
                }

                this.renderItemStack(player, armourStack, xOffset, -26, ((Boolean) this.armor.getValue()).booleanValue());
                xOffset += 16;
            }
        }

        this.renderItemStack(player, renderMainHand, xOffset, -26, ((Boolean) this.armor.getValue()).booleanValue());
        GlStateManager.popMatrix();
        this.renderer.drawStringWithShadow(displayTag, (float) (-width), (float) (-(this.renderer.getFontHeight() - 1)), this.getDisplayColour(player));
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0F, 1500000.0F);
        GlStateManager.popMatrix();
    }

    private void renderNameTag(EntityPlayer player, double x, double y, double z, float partialTicks) {
        double tempY = y + (player.isSneaking() ? 0.5D : 0.7D);
        Entity camera = TestNametags.mc.getRenderViewEntity();

        assert camera != null;

        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;

        camera.posX = this.interpolate(camera.prevPosX, camera.posX, partialTicks);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, partialTicks);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, partialTicks);
        double distance = camera.getDistance(x + TestNametags.mc.getRenderManager().viewerPosX, y + TestNametags.mc.getRenderManager().viewerPosY, z + TestNametags.mc.getRenderManager().viewerPosZ);
        int width = TestNametags.mc.fontRenderer.getStringWidth(this.getDisplayTag(player)) / 2;
        double scale = (0.0018D + (double) ((Float) this.scaling.getValue()).floatValue() * distance) / 50.0D;

        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0F, -1500000.0F);
        GlStateManager.disableLighting();
        GlStateManager.translate((float) x, (float) tempY + 1.4F, (float) z);
        GlStateManager.rotate(-TestNametags.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        float thirdPersonOffset = TestNametags.mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F;

        GlStateManager.rotate(TestNametags.mc.getRenderManager().playerViewX, thirdPersonOffset, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        this.drawRect((float) (-width - 2), (float) (-(TestNametags.mc.fontRenderer.FONT_HEIGHT + 1)), (float) width + 2.0F, 1.5F, 1426063360);
        GlStateManager.enableAlpha();
        TestNametags.mc.fontRenderer.drawStringWithShadow(this.getDisplayTag(player), (float) (-width), (float) (-(TestNametags.mc.fontRenderer.FONT_HEIGHT - 1)), this.getNameColor(player).getRGB());
        if (((Boolean) this.armor.getValue()).booleanValue()) {
            GlStateManager.pushMatrix();
            double changeValue = 16.0D;
            byte xOffset = 0;
            int xOffset1 = (int) ((double) xOffset - changeValue / 2.0D * (double) player.inventory.armorInventory.size());

            xOffset1 = (int) ((double) xOffset1 - changeValue / 2.0D);
            xOffset1 = (int) ((double) xOffset1 - changeValue / 2.0D);
            if (!player.getHeldItemMainhand().isEmpty()) {
                ;
            }

            xOffset1 = (int) ((double) xOffset1 + changeValue);

            for (Iterator iterator = player.inventory.armorInventory.iterator(); iterator.hasNext(); xOffset1 = (int) ((double) xOffset1 + changeValue)) {
                ItemStack stack = (ItemStack) iterator.next();

                if (!stack.isEmpty()) {
                    ;
                }
            }

            if (!player.getHeldItemOffhand().isEmpty()) {
                ;
            }

            GlStateManager.popMatrix();
        }

        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0F, 1500000.0F);
        GlStateManager.popMatrix();
    }

    public void drawRect(float x, float y, float w, float h, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth(((Float) this.lineWidth.getValue()).floatValue());
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

    public void drawOutlineRect(float x, float y, float w, float h, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth(((Float) this.lineWidth.getValue()).floatValue());
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) x, (double) h, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double) w, (double) h, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double) w, (double) y, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double) x, (double) y, 0.0D).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    private Color getNameColor(Entity entity) {
        return Color.WHITE;
    }

    private void renderItemStack(EntityPlayer player, ItemStack stack, int x, int y, boolean item) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        TestNametags.mc.getRenderItem().zLevel = -150.0F;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        if (item) {
            TestNametags.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
            TestNametags.mc.getRenderItem().renderItemOverlays(TestNametags.mc.fontRenderer, stack, x, y);
        }

        TestNametags.mc.getRenderItem().zLevel = 0.0F;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.disableDepth();
        this.renderEnchantmentText(player, stack, x, y);
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        GlStateManager.popMatrix();
    }

    private boolean shouldMoveArmor(EntityPlayer player) {
        Iterator renderMainHand = player.inventory.armorInventory.iterator();

        NBTTagList enchants;

        do {
            ItemStack renderOffHand;

            if (!renderMainHand.hasNext()) {
                ItemStack renderMainHand1 = player.getHeldItemMainhand().copy();

                if (renderMainHand1.hasEffect()) {
                    return true;
                }

                renderOffHand = player.getHeldItemOffhand().copy();
                return renderMainHand1.hasEffect();
            }

            renderOffHand = (ItemStack) renderMainHand.next();
            enchants = renderOffHand.getEnchantmentTagList();
        } while (enchants.tagCount() == 0);

        return true;
    }

    private void renderEnchantmentText(EntityPlayer player, ItemStack stack, int x, int y) {
        int enchantmentY = (int) ((float) (y - 8) - (((Boolean) this.topEnchant.getValue()).booleanValue() ? this.getBiggestArmorTag(player) - this.getEnchantHeight(stack) : 0.0F));

        if (stack.getItem() == Items.GOLDEN_APPLE && stack.hasEffect()) {
            this.renderer.drawStringWithShadow("god", (float) (x * 2), (float) enchantmentY, -3977919);
            enchantmentY -= 8;
        }

        if (DamageUtil.hasDurability(stack)) {
            int percent = DamageUtil.getRoundedDamage(stack);
            String color = percent >= 60 ? "§a" : (percent >= 25 ? "§e" : "§c");

            this.renderer.drawStringWithShadow(color + percent + "%", (float) (x * 2), (float) enchantmentY, -1);
        }

    }

    private float getEnchantHeight(ItemStack stack) {
        float enchantHeight = 0.0F;
        NBTTagList enchants = stack.getEnchantmentTagList();

        for (int index = 0; index < enchants.tagCount(); ++index) {
            short id = enchants.getCompoundTagAt(index).getShort("id");
            Enchantment enc = Enchantment.getEnchantmentByID(id);

            if (enc != null) {
                enchantHeight += 8.0F;
            }
        }

        return enchantHeight;
    }

    private float getBiggestArmorTag(EntityPlayer player) {
        float enchantmentY = 0.0F;
        boolean arm = false;
        Iterator renderMainHand = player.inventory.armorInventory.iterator();

        Enchantment enc;
        int index;
        short id1;

        while (renderMainHand.hasNext()) {
            ItemStack encY = (ItemStack) renderMainHand.next();
            float enchants = 0.0F;

            if (encY != null) {
                NBTTagList id = encY.getEnchantmentTagList();

                for (index = 0; index < id.tagCount(); ++index) {
                    id1 = id.getCompoundTagAt(index).getShort("id");
                    enc = Enchantment.getEnchantmentByID(id1);
                    if (enc != null) {
                        enchants += 8.0F;
                        arm = true;
                    }
                }
            }

            if (enchants > enchantmentY) {
                enchantmentY = enchants;
            }
        }

        ItemStack itemstack = player.getHeldItemMainhand().copy();
        float f;
        NBTTagList nbttaglist;

        if (itemstack.hasEffect()) {
            f = 0.0F;
            nbttaglist = itemstack.getEnchantmentTagList();

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                id1 = nbttaglist.getCompoundTagAt(i).getShort("id");
                Enchantment enc2 = Enchantment.getEnchantmentByID(id1);

                if (enc2 != null) {
                    f += 8.0F;
                    arm = true;
                }
            }

            if (f > enchantmentY) {
                enchantmentY = f;
            }
        }

        ItemStack renderOffHand;

        if ((renderOffHand = player.getHeldItemOffhand().copy()).hasEffect()) {
            f = 0.0F;
            nbttaglist = renderOffHand.getEnchantmentTagList();

            for (index = 0; index < nbttaglist.tagCount(); ++index) {
                short short0 = nbttaglist.getCompoundTagAt(index).getShort("id");

                enc = Enchantment.getEnchantmentByID(short0);
                if (enc != null) {
                    f += 8.0F;
                    arm = true;
                }
            }

            if (f > enchantmentY) {
                enchantmentY = f;
            }
        }

        return (float) (arm ? 0 : 20) + enchantmentY;
    }

    private String getDisplayTag(EntityPlayer player) {
        String name = player.getDisplayName().getFormattedText();

        if (name.contains(TestNametags.mc.getSession().getUsername())) {
            name = "You";
        }

        if (!((Boolean) this.health.getValue()).booleanValue()) {
            return name;
        } else {
            float health = EntityUtil.getHealth(player);
            String s;

            if (health > 18.0F) {
                s = "§a";
            } else if (health > 16.0F) {
                s = "§2";
            } else if (health > 12.0F) {
                s = "§e";
            } else if (health > 8.0F) {
                s = "§6";
            } else {
                s = health > 5.0F ? "§c" : "§4";
            }

            String pingStr = "";

            if (((Boolean) this.ping.getValue()).booleanValue()) {
                try {
                    int responseTime = ((NetHandlerPlayClient) Objects.requireNonNull(TestNametags.mc.getConnection())).getPlayerInfo(player.getUniqueID()).getResponseTime();

                    pingStr = pingStr + responseTime + "ms ";
                } catch (Exception exception) {
                    ;
                }
            }

            return name;
        }
    }

    private int getDisplayColour(EntityPlayer player) {
        int colour = -5592406;

        if (((Boolean) this.whiter.getValue()).booleanValue()) {
            colour = -1;
        }

        if (OyVey.friendManager.isFriend(player)) {
            return -11157267;
        } else {
            if (player.isInvisible()) {
                colour = -1113785;
            } else if (player.isSneaking() && ((Boolean) this.sneak.getValue()).booleanValue()) {
                colour = -6481515;
            }

            return colour;
        }
    }

    private double interpolate(double previous, double current, float partialTicks) {
        return previous + (current - previous) * (double) partialTicks;
    }
}
