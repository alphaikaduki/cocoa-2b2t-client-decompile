package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.features.modules.misc.ToolTips;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
    value = { GuiScreen.class},
    priority = 1998
)
public class MixinGuiScreen extends Gui {

    @Inject(
        method = { "renderToolTip"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void renderToolTipHook(ItemStack stack, int x, int y, CallbackInfo info) {
        if (ToolTips.getInstance().isOn() && stack.getItem() instanceof ItemShulkerBox) {
            ToolTips.getInstance().renderShulkerToolTip(stack, x, y, (String) null);
            info.cancel();
        }

    }
}