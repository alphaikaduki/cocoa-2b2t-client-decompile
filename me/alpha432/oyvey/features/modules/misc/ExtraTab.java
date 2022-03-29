package me.alpha432.oyvey.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;

public class ExtraTab extends Module {

    private static ExtraTab INSTANCE = new ExtraTab();
    public Setting size = this.register(new Setting("Size", Integer.valueOf(250), Integer.valueOf(1), Integer.valueOf(1000)));

    public ExtraTab() {
        super("ExtraTab", "Extends Tab.", Module.Category.MISC, false, false, false);
        this.setInstance();
    }

    public static String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
        String name = networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());

        return OyVey.friendManager.isFriend(name) ? ChatFormatting.AQUA + name : name;
    }

    public static ExtraTab getINSTANCE() {
        if (ExtraTab.INSTANCE == null) {
            ExtraTab.INSTANCE = new ExtraTab();
        }

        return ExtraTab.INSTANCE;
    }

    private void setInstance() {
        ExtraTab.INSTANCE = this;
    }
}
