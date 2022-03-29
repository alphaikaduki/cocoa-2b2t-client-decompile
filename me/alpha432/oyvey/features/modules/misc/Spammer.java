package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.Timerm;
import net.minecraft.network.play.client.CPacketChatMessage;
import org.apache.commons.lang3.RandomStringUtils;

public class Spammer extends Module {

    private final Timerm timer = new Timerm();
    private Setting custom = this.register(new Setting("Custom", "Spammer"));
    private Setting random = this.register(new Setting("Random", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(20)));
    private Setting delay = this.register(new Setting("Delay", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(5000)));

    public Spammer() {
        super("spammer", "Message", Module.Category.MISC, true, false, false);
    }

    public void onTick() {
        if (!fullNullCheck() && this.timer.passedMs((long) ((Integer) this.delay.getValue()).intValue())) {
            Spammer.mc.player.connection.sendPacket(new CPacketChatMessage((String) this.custom.getValue() + RandomStringUtils.randomAlphanumeric(((Integer) this.random.getValue()).intValue())));
            this.timer.reset();
        }

    }
}
