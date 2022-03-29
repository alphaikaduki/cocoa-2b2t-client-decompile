package me.alpha432.oyvey.mixin.mixins;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ GuiMainMenu.class})
class GuiMainMenu2 extends GuiScreen {

    @Inject(
        method = { "initGui"},
        at = {             @At("RETURN")}
    )
    private void GuiMainMenu(CallbackInfo callbackInfo) {
        this.buttonList.add(new GuiButton(500, this.width / 2 + 104, this.height / 4 + 48 + 48, 98, 20, "login"));
    }

    @Inject(
        method = { "actionPerformed"},
        at = {             @At("HEAD")}
    )
    private void actionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == 500) {
            ;
        }

    }
}
