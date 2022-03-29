package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.ClientEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.TextUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Management extends Module {

    private static Management INSTANCE = new Management();
    public Setting betterFrames = this.register(new Setting("BetterMaxFPS", Boolean.valueOf(false)));
    public Setting commandBracket = this.register(new Setting("Bracket", "<"));
    public Setting commandBracket2 = this.register(new Setting("Bracket2", ">"));
    public Setting command = this.register(new Setting("Command", "Phobos.eu"));
    public Setting rainbowPrefix = this.register(new Setting("RainbowPrefix", Boolean.valueOf(false)));
    public Setting bracketColor;
    public Setting commandColor;
    public Setting betterFPS;
    public Setting potions;
    public Setting textRadarUpdates;
    public Setting respondTime;
    public Setting moduleListUpdates;
    public Setting holeRange;
    public Setting holeUpdates;
    public Setting holeSync;
    public Setting safety;
    public Setting safetyCheck;
    public Setting holeThread;
    public Setting speed;
    public Setting oneDot15;
    public Setting tRadarInv;
    public Setting unfocusedCpu;
    public Setting cpuFPS;
    public Setting baritoneTimeOut;
    public Setting oneChunk;

    public Management() {
        super("Management", "ClientManagement", Module.Category.CLIENT, false, false, true);
        this.bracketColor = this.register(new Setting("BColor", TextUtil.Color.BLUE));
        this.commandColor = this.register(new Setting("CColor", TextUtil.Color.BLUE));
        this.betterFPS = this.register(new Setting("MaxFPS", Integer.valueOf(300), Integer.valueOf(30), Integer.valueOf(1000), test<invokedynamic>(this)));
        this.potions = this.register(new Setting("Potions", Boolean.valueOf(true)));
        this.textRadarUpdates = this.register(new Setting("TRUpdates", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(1000)));
        this.respondTime = this.register(new Setting("SeverTime", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(1000)));
        this.moduleListUpdates = this.register(new Setting("ALUpdates", Integer.valueOf(1000), Integer.valueOf(0), Integer.valueOf(1000)));
        this.holeRange = this.register(new Setting("HoleRange", Float.valueOf(6.0F), Float.valueOf(1.0F), Float.valueOf(256.0F)));
        this.holeUpdates = this.register(new Setting("HoleUpdates", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(1000)));
        this.holeSync = this.register(new Setting("HoleSync", Integer.valueOf(10000), Integer.valueOf(1), Integer.valueOf(10000)));
        this.safety = this.register(new Setting("SafetyPlayer", Boolean.valueOf(false)));
        this.safetyCheck = this.register(new Setting("SafetyCheck", Integer.valueOf(50), Integer.valueOf(1), Integer.valueOf(150)));
        this.holeThread = this.register(new Setting("HoleThread", Management.ThreadMode.WHILE));
        this.speed = this.register(new Setting("Speed", Boolean.valueOf(true)));
        this.oneDot15 = this.register(new Setting("1.15", Boolean.valueOf(false)));
        this.tRadarInv = this.register(new Setting("TRadarInv", Boolean.valueOf(true)));
        this.unfocusedCpu = this.register(new Setting("UnfocusedCPU", Boolean.valueOf(false)));
        this.cpuFPS = this.register(new Setting("UnfocusedFPS", Integer.valueOf(60), Integer.valueOf(1), Integer.valueOf(60), test<invokedynamic>(this)));
        this.baritoneTimeOut = this.register(new Setting("Baritone", Integer.valueOf(5), Integer.valueOf(1), Integer.valueOf(20)));
        this.oneChunk = this.register(new Setting("OneChunk", Boolean.valueOf(false)));
        this.setInstance();
    }

    public static Management getInstance() {
        if (Management.INSTANCE == null) {
            Management.INSTANCE = new Management();
        }

        return Management.INSTANCE;
    }

    private void setInstance() {
        Management.INSTANCE = this;
    }

    public void onLoad() {
        OyVey.commandManager.setClientMessage(this.getCommandMessage());
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2) {
            if (((Boolean) this.oneChunk.getPlannedValue()).booleanValue()) {
                Management.mc.gameSettings.renderDistanceChunks = 1;
            }

            if (event.getSetting() != null && this.equals(event.getSetting().getFeature())) {
                if (event.getSetting().equals(this.holeThread)) {
                    ;
                }

                OyVey.commandManager.setClientMessage(this.getCommandMessage());
            }
        }

    }

    public String getCommandMessage() {
        if (((Boolean) this.rainbowPrefix.getPlannedValue()).booleanValue()) {
            StringBuilder stringBuilder = new StringBuilder(this.getRawCommandMessage());

            stringBuilder.insert(0, "§+");
            stringBuilder.append("§r");
            return stringBuilder.toString();
        } else {
            return TextUtil.coloredString((String) this.commandBracket.getPlannedValue(), (TextUtil.Color) this.bracketColor.getPlannedValue()) + TextUtil.coloredString((String) this.command.getPlannedValue(), (TextUtil.Color) this.commandColor.getPlannedValue()) + TextUtil.coloredString((String) this.commandBracket2.getPlannedValue(), (TextUtil.Color) this.bracketColor.getPlannedValue());
        }
    }

    public String getRainbowCommandMessage() {
        StringBuilder stringBuilder = new StringBuilder(this.getRawCommandMessage());

        stringBuilder.insert(0, "§+");
        stringBuilder.append("§r");
        return stringBuilder.toString();
    }

    public String getRawCommandMessage() {
        return (String) this.commandBracket.getValue() + (String) this.command.getValue() + (String) this.commandBracket2.getValue();
    }

    private boolean lambda$new$1(Object v) {
        return ((Boolean) this.unfocusedCpu.getValue()).booleanValue();
    }

    private boolean lambda$new$0(Object v) {
        return ((Boolean) this.betterFrames.getValue()).booleanValue();
    }

    public static enum ThreadMode {

        POOL, WHILE, NONE;
    }
}
