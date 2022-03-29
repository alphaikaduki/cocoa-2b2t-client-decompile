package me.alpha432.oyvey.util.song;

import javax.annotation.Nullable;
import me.alpha432.oyvey.features.gui.custom.Globals;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.ISound.AttenuationType;
import net.minecraft.client.audio.Sound.Type;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class FireBall implements Globals {

    public static final ISound sound = new ISound() {
        private final int pitch = 1;
        private final int volume = 1000000;

        public ResourceLocation getSoundLocation() {
            return FireBall.loc;
        }

        @Nullable
        public SoundEventAccessor createAccessor(SoundHandler soundHandler) {
            return new SoundEventAccessor(FireBall.loc, "Pitbull");
        }

        public Sound getSound() {
            return new Sound("fireball", 1000000.0F, 1.0F, 1, Type.SOUND_EVENT, false);
        }

        public SoundCategory getCategory() {
            return SoundCategory.VOICE;
        }

        public boolean canRepeat() {
            return true;
        }

        public int getRepeatDelay() {
            return 2;
        }

        public float getVolume() {
            return 1000000.0F;
        }

        public float getPitch() {
            return 1.0F;
        }

        public float getXPosF() {
            Minecraft mc = Minecraft.getMinecraft();

            return mc.player != null ? (float) mc.player.posX : 0.0F;
        }

        public float getYPosF() {
            return Globals.mc.player != null ? (float) Globals.mc.player.posY : 0.0F;
        }

        public float getZPosF() {
            return Globals.mc.player != null ? (float) Globals.mc.player.posZ : 0.0F;
        }

        public AttenuationType getAttenuationType() {
            return AttenuationType.LINEAR;
        }
    };
    private static final String song = "fireball";
    private static final ResourceLocation loc = new ResourceLocation("sounds/fireball.ogg");
}
