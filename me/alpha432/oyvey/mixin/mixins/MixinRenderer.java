package me.alpha432.oyvey.mixin.mixins;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ Render.class})
abstract class MixinRenderer {

    @Shadow
    protected boolean renderOutlines;
    @Shadow
    @Final
    protected RenderManager renderManager;

    @Shadow
    protected abstract boolean bindEntityTexture(EntityItem entityitem);

    @Shadow
    protected abstract int getTeamColor(EntityItem entityitem);
}
