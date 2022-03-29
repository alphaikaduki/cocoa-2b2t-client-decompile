package me.alpha432.oyvey.mixin.mixins;

import java.util.Random;
import me.alpha432.oyvey.features.modules.render.ItemPhysics;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ RenderEntityItem.class})
public abstract class MixinRenderEntityItem extends MixinRenderer {

    private final Minecraft mc = Minecraft.getMinecraft();
    @Shadow
    @Final
    private RenderItem itemRenderer;
    @Shadow
    @Final
    private Random random;
    private long tick;

    @Shadow
    public abstract int getModelCount(ItemStack itemstack);

    @Shadow
    public abstract boolean shouldSpreadItems();

    @Shadow
    public abstract boolean shouldBob();

    @Shadow
    protected abstract ResourceLocation getEntityTexture(EntityItem entityitem);

    private double formPositive(float rotationPitch) {
        return rotationPitch > 0.0F ? (double) rotationPitch : (double) (-rotationPitch);
    }

    @Overwrite
    private int transformModelCount(EntityItem itemIn, double x, double y, double z, float p_177077_8_, IBakedModel p_177077_9_) {
        ItemStack itemstack;
        boolean flag;
        int i;
        float f1;
        float f2;
        float f3;

        if (ItemPhysics.INSTANCE.isEnabled()) {
            itemstack = itemIn.getItem();
            itemstack.getItem();
            flag = p_177077_9_.isAmbientOcclusion();
            i = this.getModelCount(itemstack);
            f1 = 0.0F;
            GlStateManager.translate((float) x, (float) y + 0.0F + 0.1F, (float) z);
            f2 = 0.0F;
            if (flag || this.mc.getRenderManager().options != null && this.mc.getRenderManager().options.fancyGraphics) {
                GlStateManager.rotate(f2, 0.0F, 1.0F, 0.0F);
            }

            if (!flag) {
                f2 = -0.0F * (float) (i - 1) * 0.5F;
                f3 = -0.0F * (float) (i - 1) * 0.5F;
                float f5 = -0.046875F * (float) (i - 1) * 0.5F;

                GlStateManager.translate(f2, f3, f5);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            return i;
        } else {
            itemstack = itemIn.getItem();
            itemstack.getItem();
            flag = p_177077_9_.isGui3d();
            i = this.getModelCount(itemstack);
            f1 = this.shouldBob() ? MathHelper.sin(((float) itemIn.getAge() + p_177077_8_) / 10.0F + itemIn.hoverStart) * 0.1F + 0.1F : 0.0F;
            f2 = p_177077_9_.getItemCameraTransforms().getTransform(TransformType.GROUND).scale.y;
            GlStateManager.translate((float) x, (float) y + f1 + 0.25F * f2, (float) z);
            if (flag || this.renderManager.options != null) {
                f3 = (((float) itemIn.getAge() + p_177077_8_) / 20.0F + itemIn.hoverStart) * 57.295776F;
                GlStateManager.rotate(f3, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            return i;
        }
    }

    @Overwrite
    public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks) {
        IBakedModel ibakedmodel;
        int j;
        int i;

        if (ItemPhysics.INSTANCE.isEnabled()) {
            double d0 = (double) (System.nanoTime() - this.tick) / 3000000.0D;

            if (!this.mc.inGameHasFocus) {
                d0 = 0.0D;
            }

            ItemStack itemstack = entity.getItem();

            itemstack.getItem();
            this.random.setSeed(187L);
            this.mc.getRenderManager().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.mc.getRenderManager().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.pushMatrix();
            ibakedmodel = this.itemRenderer.getItemModelMesher().getItemModel(itemstack);
            j = this.transformModelCount(entity, x, y, z, partialTicks, ibakedmodel);
            BlockPos blockpos = new BlockPos(entity);

            if (entity.rotationPitch > 360.0F) {
                entity.rotationPitch = 0.0F;
            }

            if (!Double.isNaN(entity.posX) && !Double.isNaN(entity.posY) && !Double.isNaN(entity.posZ) && entity.world != null) {
                if (entity.onGround) {
                    if (entity.rotationPitch != 0.0F && entity.rotationPitch != 90.0F && entity.rotationPitch != 180.0F && entity.rotationPitch != 270.0F) {
                        double d1 = this.formPositive(entity.rotationPitch);
                        double d2 = this.formPositive(entity.rotationPitch - 90.0F);
                        double flag3 = this.formPositive(entity.rotationPitch - 180.0F);
                        double d4 = this.formPositive(entity.rotationPitch - 270.0F);

                        if (d1 <= d2 && d1 <= flag3 && d1 <= d4) {
                            if (entity.rotationPitch < 0.0F) {
                                entity.rotationPitch += (float) d0;
                            } else {
                                entity.rotationPitch -= (float) d0;
                            }
                        }

                        if (d2 < d1 && d2 <= flag3 && d2 <= d4) {
                            if (entity.rotationPitch - 90.0F < 0.0F) {
                                entity.rotationPitch += (float) d0;
                            } else {
                                entity.rotationPitch -= (float) d0;
                            }
                        }

                        if (flag3 < d2 && flag3 < d1 && flag3 <= d4) {
                            if (entity.rotationPitch - 180.0F < 0.0F) {
                                entity.rotationPitch += (float) d0;
                            } else {
                                entity.rotationPitch -= (float) d0;
                            }
                        }

                        if (d4 < d2 && d4 < flag3 && d4 < d1) {
                            if (entity.rotationPitch - 270.0F < 0.0F) {
                                entity.rotationPitch += (float) d0;
                            } else {
                                entity.rotationPitch -= (float) d0;
                            }
                        }
                    }
                } else {
                    BlockPos blockpos1 = new BlockPos(entity);

                    blockpos1.add(0, 1, 0);
                    Material material = entity.world.getBlockState(blockpos1).getMaterial();
                    Material material1 = entity.world.getBlockState(blockpos).getMaterial();
                    boolean flag = entity.isInsideOfMaterial(Material.WATER);
                    boolean flag1 = entity.isInWater();

                    if (flag | material == Material.WATER | material1 == Material.WATER | flag1) {
                        entity.rotationPitch += (float) (d0 / 4.0D);
                    } else {
                        entity.rotationPitch += (float) (d0 * 2.0D);
                    }
                }
            }

            GL11.glRotatef(entity.rotationYaw, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(entity.rotationPitch + 90.0F, 1.0F, 0.0F, 0.0F);

            for (i = 0; i < j; ++i) {
                if (ibakedmodel.isAmbientOcclusion()) {
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(((Float) ItemPhysics.INSTANCE.Scaling.getValue()).floatValue(), ((Float) ItemPhysics.INSTANCE.Scaling.getValue()).floatValue(), ((Float) ItemPhysics.INSTANCE.Scaling.getValue()).floatValue());
                    this.itemRenderer.renderItem(itemstack, ibakedmodel);
                    GlStateManager.popMatrix();
                } else {
                    GlStateManager.pushMatrix();
                    if (i > 0 && this.shouldSpreadItems()) {
                        GlStateManager.translate(0.0F, 0.0F, 0.046875F * (float) i);
                    }

                    this.itemRenderer.renderItem(itemstack, ibakedmodel);
                    if (!this.shouldSpreadItems()) {
                        GlStateManager.translate(0.0F, 0.0F, 0.046875F);
                    }

                    GlStateManager.popMatrix();
                }
            }

            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            this.mc.getRenderManager().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.mc.getRenderManager().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        } else {
            ItemStack itemstack = entity.getItem();
            int i = itemstack.isEmpty() ? 187 : Item.getIdFromItem(itemstack.getItem()) + itemstack.getMetadata();

            this.random.setSeed((long) i);
            boolean flag = false;

            if (this.bindEntityTexture(entity)) {
                this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).setBlurMipmap(false, false);
                flag = true;
            }

            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
            GlStateManager.pushMatrix();
            ibakedmodel = this.itemRenderer.getItemModelWithOverrides(itemstack, entity.world, (EntityLivingBase) null);
            j = this.transformModelCount(entity, x, y, z, partialTicks, ibakedmodel);
            boolean flag1 = ibakedmodel.isGui3d();
            float transformedModel;
            float f10;

            if (!flag1) {
                float k = -0.0F * (float) (j - 1) * 0.5F;

                transformedModel = -0.0F * (float) (j - 1) * 0.5F;
                f10 = -0.09375F * (float) (j - 1) * 0.5F;
                GlStateManager.translate(k, transformedModel, f10);
            }

            if (this.renderOutlines) {
                GlStateManager.enableColorMaterial();
                GlStateManager.enableOutlineMode(this.getTeamColor(entity));
            }

            for (i = 0; i < j; ++i) {
                GlStateManager.pushMatrix();
                IBakedModel ibakedmodel;

                if (flag1) {
                    if (i > 0) {
                        transformedModel = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        float f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;

                        GlStateManager.translate(this.shouldSpreadItems() ? transformedModel : 0.0F, this.shouldSpreadItems() ? f10 : 0.0F, f6);
                    }

                    ibakedmodel = ForgeHooksClient.handleCameraTransforms(ibakedmodel, TransformType.GROUND, false);
                    this.itemRenderer.renderItem(itemstack, ibakedmodel);
                    GlStateManager.popMatrix();
                } else {
                    if (i > 0) {
                        transformedModel = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                        f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                        GlStateManager.translate(transformedModel, f10, 0.0F);
                    }

                    ibakedmodel = ForgeHooksClient.handleCameraTransforms(ibakedmodel, TransformType.GROUND, false);
                    this.itemRenderer.renderItem(itemstack, ibakedmodel);
                    GlStateManager.popMatrix();
                    GlStateManager.translate(0.0F, 0.0F, 0.09375F);
                }
            }

            if (this.renderOutlines) {
                GlStateManager.disableOutlineMode();
                GlStateManager.disableColorMaterial();
            }

            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            this.bindEntityTexture(entity);
            if (flag) {
                this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).restoreLastBlurMipmap();
            }

        }
    }
}
