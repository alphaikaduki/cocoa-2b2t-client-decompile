package me.alpha432.oyvey.util.song;

import javax.annotation.Nullable;
import me.alpha432.oyvey.features.gui.custom.Globals;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.ISound.AttenuationType;
import net.minecraft.client.audio.Sound.Type;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class HotelRoom implements Globals {

    public static final ISound sound = new ISound() {
        private final int pitch = 1;
        private final int volume = 1;

        public ResourceLocation getSoundLocation() {
            return HotelRoom.loc;
        }

        @Nullable
        public SoundEventAccessor createAccessor(SoundHandler soundHandler) {
            return new SoundEventAccessor(HotelRoom.loc, "Pitbull");
        }

        public Sound getSound() {
            return new Sound("hotelroom", 1.0F, 1.0F, 1, Type.SOUND_EVENT, false);
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
            return 1.0F;
        }

        public float getPitch() {
            return 1.0F;
        }

        public float getXPosF() {
            return 1.0F;
        }

        public float getYPosF() {
            return 0.0F;
        }

        public float getZPosF() {
            return 0.0F;
        }

        public AttenuationType getAttenuationType() {
            return AttenuationType.LINEAR;
        }
    };
    private static final String song = "hotelroom";
    private static final ResourceLocation loc = new ResourceLocation("sounds/hotelroom.ogg");
}
