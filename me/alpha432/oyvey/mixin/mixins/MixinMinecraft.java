package me.alpha432.oyvey.mixin.mixins;

import javax.annotation.Nullable;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.KeyEvent;
import me.alpha432.oyvey.features.gui.custom.GuiCustomMainScreen;
import me.alpha432.oyvey.features.modules.client.MainMenu;
import me.alpha432.oyvey.features.modules.client.Management;
import me.alpha432.oyvey.features.modules.player.MultiTask;
import me.alpha432.oyvey.features.modules.render.NoRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.crash.CrashReport;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Minecraft.class})
public abstract class MixinMinecraft {

    @Inject(
        method = { "shutdownMinecraftApplet"},
        at = {             @At("HEAD")}
    )
    private void stopClient(CallbackInfo callbackInfo) {
        this.unload();
    }

    @Shadow
    public abstract void displayGuiScreen(@Nullable GuiScreen guiscreen);

    @Inject(
        method = { "runTickKeyboard"},
        at = {             @At(
                value = "INVOKE",
                remap = false,
                target = "Lorg/lwjgl/input/Keyboard;getEventKey()I",
                ordinal = 0,
                shift = At.Shift.BEFORE
            )}
    )
    private void onKeyboard(CallbackInfo callbackInfo) {
        int i = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();

        if (Keyboard.getEventKeyState()) {
            KeyEvent event = new KeyEvent(i);

            MinecraftForge.EVENT_BUS.post(event);
        }

    }

    @Inject(
        method = { "runTick()V"},
        at = {             @At("RETURN")}
    )
    @Redirect(
        method = { "runTick"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/multiplayer/WorldClient;doVoidFogParticles(III)V"
            )
    )
    public void doVoidFogParticlesHook(WorldClient world, int x, int y, int z) {
        NoRender.getInstance().doVoidFogParticles(x, y, z);
    }

    @Redirect(
        method = { "runGameLoop"},
        at =             @At(
                value = "INVOKE",
                target = "Lorg/lwjgl/opengl/Display;sync(I)V",
                remap = false
            )
    )
    public void syncHook(int maxFps) {
        if (((Boolean) Management.getInstance().betterFrames.getValue()).booleanValue()) {
            Display.sync(((Integer) Management.getInstance().betterFPS.getValue()).intValue());
        } else {
            Display.sync(maxFps);
        }

    }

    @Inject(
        method = { "runTick()V"},
        at = {             @At("RETURN")}
    )
    private void runTick(CallbackInfo callbackInfo) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu && ((Boolean) MainMenu.INSTANCE.mainScreen.getValue()).booleanValue()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiCustomMainScreen());
        }

    }

    @Inject(
        method = { "displayGuiScreen"},
        at = {             @At("HEAD")}
    )
    private void displayGuiScreen(GuiScreen screen, CallbackInfo ci) {
        if (screen instanceof GuiMainMenu) {
            this.displayGuiScreen(new GuiCustomMainScreen());
        }

    }

    @Redirect(
        method = { "run"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"
            )
    )
    public void displayCrashReportHook(Minecraft minecraft, CrashReport crashReport) {
        this.unload();
    }

    @Inject(
        method = { "displayGuiScreen"},
        at = {             @At("HEAD")}
    )
    private void unload() {
        System.out.println("Shutting down: saving configuration");
        OyVey.onUnload();
        System.out.println("Configuration saved.");
    }

    @Redirect(
        method = { "sendClickBlockToController"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/entity/EntityPlayerSP;isHandActive()Z"
            )
    )
    private boolean isHandActiveWrapper(EntityPlayerSP playerSP) {
        return !MultiTask.getInstance().isOn() && playerSP.isHandActive();
    }

    @Redirect(
        method = { "rightClickMouse"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;getIsHittingBlock()Z",
                ordinal = 0
            )
    )
    private boolean isHittingBlockHook(PlayerControllerMP playerControllerMP) {
        return !MultiTask.getInstance().isOn() && playerControllerMP.getIsHittingBlock();
    }

    @Inject(
        method = { "middleClickMouse"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void middleClickMouse(CallbackInfo cancel) {
        cancel.cancel();
    }
}
