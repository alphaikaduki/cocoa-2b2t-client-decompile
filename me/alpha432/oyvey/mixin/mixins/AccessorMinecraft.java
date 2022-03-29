package me.alpha432.oyvey.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ Minecraft.class})
public interface AccessorMinecraft {

    @Accessor("timer")
    Timer getTimer();

    @Accessor("session")
    void setSession(Session session);

    @Accessor("renderPartialTicksPaused")
    float getRenderPartialTicksPaused();
}
