package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.event.events.PlayerDamageBlockEvent;
import me.alpha432.oyvey.event.events.ProcessRightClickBlockEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
    value = { PlayerControllerMP.class},
    priority = Integer.MAX_VALUE
)
public abstract class MixinPlayerControllerMP {

    @Shadow
    public abstract void syncCurrentPlayItem();

    @Inject(
        method = { "onPlayerDamageBlock"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    private void onPlayerDamageBlockHook(BlockPos pos, EnumFacing face, CallbackInfoReturnable info) {
        PlayerDamageBlockEvent event = new PlayerDamageBlockEvent(0, pos, face);

        MinecraftForge.EVENT_BUS.post(event);
    }

    @Inject(
        method = { "clickBlock"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    private void clickBlockHook(BlockPos pos, EnumFacing face, CallbackInfoReturnable info) {
        PlayerDamageBlockEvent event = new PlayerDamageBlockEvent(1, pos, face);

        MinecraftForge.EVENT_BUS.post(event);
    }

    @Inject(
        method = { "processRightClickBlock"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void processRightClickBlock(EntityPlayerSP player, WorldClient worldIn, BlockPos pos, EnumFacing direction, Vec3d vec, EnumHand hand, CallbackInfoReturnable cir) {
        ProcessRightClickBlockEvent event = new ProcessRightClickBlockEvent(pos, hand, Minecraft.instance.player.getHeldItem(hand));

        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            cir.cancel();
        }

    }
}
