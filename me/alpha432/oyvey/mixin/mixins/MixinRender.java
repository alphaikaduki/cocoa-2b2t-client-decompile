package me.alpha432.oyvey.mixin.mixins;

import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({ Render.class})
public class MixinRender {

    @Overwrite
    public boolean shouldRender(Entity livingEntity, ICamera camera, double camX, double camY, double camZ) {
        try {
            AxisAlignedBB ignored = livingEntity.getRenderBoundingBox().grow(0.5D);

            if ((ignored.hasNaN() || ignored.getAverageEdgeLength() == 0.0D) && livingEntity != null) {
                ignored = new AxisAlignedBB(livingEntity.posX - 2.0D, livingEntity.posY - 2.0D, livingEntity.posZ - 2.0D, livingEntity.posX + 2.0D, livingEntity.posY + 2.0D, livingEntity.posZ + 2.0D);
            }

            return livingEntity.isInRangeToRender3d(camX, camY, camZ) && (livingEntity.ignoreFrustumCheck || camera.isBoundingBoxInFrustum(ignored));
        } catch (Exception exception) {
            return false;
        }
    }
}
