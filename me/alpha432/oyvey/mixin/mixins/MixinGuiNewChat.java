package me.alpha432.oyvey.mixin.mixins;

import java.util.List;
import me.alpha432.oyvey.features.modules.misc.Chat;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ GuiNewChat.class})
public class MixinGuiNewChat extends Gui {

    @Shadow
    @Final
    public List drawnChatLines;
    private ChatLine chatLine;

    @Redirect(
        method = { "drawChat"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"
            )
    )
    private void drawRectHook(int left, int top, int right, int bottom, int color) {
        Gui.drawRect(left, top, right, bottom, Chat.getInstance().isOn() && ((Boolean) Chat.getInstance().clean.getValue()).booleanValue() ? 0 : color);
    }

    @Redirect(
        method = { "setChatLine"},
        at =             @At(
                value = "INVOKE",
                target = "Ljava/util/List;size()I",
                ordinal = 0,
                remap = false
            )
    )
    public int drawnChatLinesSize(List list) {
        return Chat.getInstance().isOn() && ((Boolean) Chat.getInstance().infinite.getValue()).booleanValue() ? -2147483647 : list.size();
    }

    @Redirect(
        method = { "setChatLine"},
        at =             @At(
                value = "INVOKE",
                target = "Ljava/util/List;size()I",
                ordinal = 2,
                remap = false
            )
    )
    public int chatLinesSize(List list) {
        return Chat.getInstance().isOn() && ((Boolean) Chat.getInstance().infinite.getValue()).booleanValue() ? -2147483647 : list.size();
    }
}
