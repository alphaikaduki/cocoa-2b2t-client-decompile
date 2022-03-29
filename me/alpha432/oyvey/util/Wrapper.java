package me.alpha432.oyvey.util;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;

public class Wrapper {

    public static final Minecraft mc = Minecraft.getMinecraft();

    @Nullable
    public static EntityPlayerSP getPlayer() {
        return Wrapper.mc.player;
    }

    @Nullable
    public static WorldClient getWorld() {
        return Wrapper.mc.world;
    }

    public static FontRenderer getFontRenderer() {
        return Wrapper.mc.fontRenderer;
    }
}
