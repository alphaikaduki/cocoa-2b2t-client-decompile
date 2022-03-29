package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Fullbright extends Module {

    public Setting mode;
    public Setting effects;
    private float previousSetting;

    public Fullbright() {
        super("Fullbright", "Makes your game brighter.", Module.Category.RENDER, true, false, false);
        this.mode = this.register(new Setting("Mode", Fullbright.Mode.GAMMA));
        this.effects = this.register(new Setting("Effects", Boolean.valueOf(false)));
        this.previousSetting = 1.0F;
    }

    public void onEnable() {
        this.previousSetting = Fullbright.mc.gameSettings.gammaSetting;
    }

    public void onUpdate() {
        if (this.mode.getValue() == Fullbright.Mode.GAMMA) {
            Fullbright.mc.gameSettings.gammaSetting = 1000.0F;
        }

        if (this.mode.getValue() == Fullbright.Mode.POTION) {
            Fullbright.mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 5210));
        }

    }

    public void onDisable() {
        if (this.mode.getValue() == Fullbright.Mode.POTION) {
            Fullbright.mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
        }

        Fullbright.mc.gameSettings.gammaSetting = this.previousSetting;
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getStage() == 0 && event.getPacket() instanceof SPacketEntityEffect && ((Boolean) this.effects.getValue()).booleanValue()) {
            SPacketEntityEffect packet = (SPacketEntityEffect) event.getPacket();

            if (Fullbright.mc.player != null && packet.getEntityId() == Fullbright.mc.player.getEntityId() && (packet.getEffectId() == 9 || packet.getEffectId() == 15)) {
                event.setCanceled(true);
            }
        }

    }

    public static enum Mode {

        GAMMA, POTION;
    }
}
