package me.alpha432.oyvey.mixin;

import me.alpha432.oyvey.features.gui.custom.GuiCustomNewChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ GuiIngame.class})
public class MixinGuiIngame extends Gui {

    @Shadow
    @Final
    public GuiNewChat persistantChatGUI;

    @Inject(
        method = { "<init>"},
        at = {             @At("RETURN")}
    )
    public void init(Minecraft mcIn, CallbackInfo ci) {
        this.persistantChatGUI = new GuiCustomNewChat(mcIn);
    }

    @Inject(
        method = { "renderPortal"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    protected void renderPortalHook(float n, ScaledResolution scaledResolution, CallbackInfo info) {
        info.cancel();
    }

    @Inject(
        method = { "renderPumpkinOverlay"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    protected void renderPumpkinOverlayHook(ScaledResolution scaledRes, CallbackInfo info) {
        info.cancel();
    }
}
