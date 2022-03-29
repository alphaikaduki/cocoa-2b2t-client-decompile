package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoRespawn extends Module {

    public Setting antiDeathScreen = this.register(new Setting("AntiDeathScreen", Boolean.valueOf(true)));
    public Setting deathCoords = this.register(new Setting("DeathCoords", Boolean.valueOf(false)));
    public Setting respawn = this.register(new Setting("Respawn", Boolean.valueOf(true)));

    public AutoRespawn() {
        super("AutoRespawn", "Respawns you when you die.", Module.Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onDisplayDeathScreen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiGameOver) {
            if (((Boolean) this.deathCoords.getValue()).booleanValue() && event.getGui() instanceof GuiGameOver) {
                Command.sendMessage(String.format("君�?� x %d y %d z %d で死んだゾwww", new Object[] { Integer.valueOf((int) AutoRespawn.mc.player.posX), Integer.valueOf((int) AutoRespawn.mc.player.posY), Integer.valueOf((int) AutoRespawn.mc.player.posZ)}));
            }

            if (((Boolean) this.respawn.getValue()).booleanValue() && AutoRespawn.mc.player.getHealth() <= 0.0F || ((Boolean) this.antiDeathScreen.getValue()).booleanValue() && AutoRespawn.mc.player.getHealth() > 0.0F) {
                event.setCanceled(true);
                AutoRespawn.mc.player.respawnPlayer();
            }
        }

    }
}
