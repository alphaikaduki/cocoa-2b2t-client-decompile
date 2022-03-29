package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.features.modules.render.NoRender;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ EntityRenderer.class})
public abstract class MixinEntityRenderer {

    @Shadow
    public ItemStack itemActivationItem;
    @Shadow
    @Final
    public Minecraft mc;
    private boolean injection = true;

    @Shadow
    public abstract void getMouseOver(float f);

    @Inject(
        method = { "renderItemActivation"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void renderItemActivationHook(CallbackInfo info) {
        if (this.itemActivationItem != null && NoRender.getInstance().isOn() && ((Boolean) NoRender.getInstance().totemPops.getValue()).booleanValue() && this.itemActivationItem.getItem() == Items.TOTEM_OF_UNDYING) {
            info.cancel();
        }

    }

    @Inject(
        method = { "updateLightmap"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    private void updateLightmap(float partialTicks, CallbackInfo info) {
        if (NoRender.getInstance().isOn() && (NoRender.getInstance().skylight.getValue() == NoRender.Skylight.ENTITY || NoRender.getInstance().skylight.getValue() == NoRender.Skylight.ALL)) {
            info.cancel();
        }

    }

    @Redirect(
        method = { "setupCameraTransform"},
        at =             @At(
                value = "FIELD",
                target = "Lnet/minecraft/client/entity/EntityPlayerSP;prevTimeInPortal:F"
            )
    )
    public float prevTimeInPortalHook(EntityPlayerSP entityPlayerSP) {
        return NoRender.getInstance().isOn() && ((Boolean) NoRender.getInstance().nausea.getValue()).booleanValue() ? -3.4028235E38F : entityPlayerSP.prevTimeInPortal;
    }

    @Inject(
        method = { "setupFog"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void setupFogHook(int startCoords, float partialTicks, CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().fog.getValue() == NoRender.Fog.NOFOG) {
            info.cancel();
        }

    }

    @Redirect(
        method = { "setupFog"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/renderer/ActiveRenderInfo;getBlockStateAtEntityViewpoint(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;F)Lnet/minecraft/block/state/IBlockState;"
            )
    )
    public IBlockState getBlockStateAtEntityViewpointHook(World worldIn, Entity entityIn, float p_186703_2_) {
        return NoRender.getInstance().isOn() && NoRender.getInstance().fog.getValue() == NoRender.Fog.AIR ? Blocks.AIR.defaultBlockState : ActiveRenderInfo.getBlockStateAtEntityViewpoint(worldIn, entityIn, p_186703_2_);
    }

    @Inject(
        method = { "hurtCameraEffect"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void hurtCameraEffectHook(float ticks, CallbackInfo info) {
        if (NoRender.getInstance().isOn() && ((Boolean) NoRender.getInstance().hurtcam.getValue()).booleanValue()) {
            info.cancel();
        }

    }
}
