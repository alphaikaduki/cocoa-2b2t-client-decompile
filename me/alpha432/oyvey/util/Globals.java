package me.alpha432.oyvey.util;

import java.util.Random;
import net.minecraft.client.Minecraft;

public interface Globals {

    Minecraft mc = Minecraft.getMinecraft();
    Random random = new Random();
    char SECTIONSIGN = '§';

    default boolean nullCheck() {
        return Globals.mc.player == null || Globals.mc.world == null;
    }
}
