package me.alpha432.oyvey.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.ClientEvent;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.client.settings.GameSettings.Options;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClickGui extends Module {

    private static ClickGui INSTANCE = new ClickGui();
    public Setting prefix = this.register(new Setting("Prefix", "."));
    public Setting customFov = this.register(new Setting("CustomFov", Boolean.valueOf(false)));
    public Setting fov = this.register(new Setting("Fov", Float.valueOf(150.0F), Float.valueOf(-180.0F), Float.valueOf(180.0F)));
    public Setting dark = this.register(new Setting("Darken", Boolean.valueOf(false)));
    public Setting red = this.register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting green = this.register(new Setting("Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting blue = this.register(new Setting("Blue", Integer.valueOf(247), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting hoverAlpha = this.register(new Setting("Alpha", Integer.valueOf(102), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting topRed = this.register(new Setting("SecondRed", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting topGreen = this.register(new Setting("SecondGreen", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting topBlue = this.register(new Setting("SecondBlue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting alpha = this.register(new Setting("HoverAlpha", Integer.valueOf(164), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting rainbow = this.register(new Setting("Rainbow", Boolean.valueOf(false)));
    public Setting rainbowModeHud;
    public Setting rainbowModeA;
    public Setting rainbowHue;
    public Setting rainbowBrightness;
    public Setting rainbowSaturation;
    public Setting particles;
    public Setting particleLength;
    public Setting particlered;
    public Setting particlegreen;
    public Setting particleblue;
    private OyVeyGui click;

    public ClickGui() {
        super("GUI", "Opens the ClickGui", Module.Category.CLIENT, true, false, false);
        this.rainbowModeHud = this.register(new Setting("HRainbowMode", ClickGui.rainbowMode.Static, test<invokedynamic>(this)));
        this.rainbowModeA = this.register(new Setting("ARainbowMode", ClickGui.rainbowModeArray.Static, test<invokedynamic>(this)));
        this.rainbowHue = this.register(new Setting("Delay", Integer.valueOf(240), Integer.valueOf(0), Integer.valueOf(600), test<invokedynamic>(this)));
        this.rainbowBrightness = this.register(new Setting("Brightness ", Float.valueOf(150.0F), Float.valueOf(1.0F), Float.valueOf(255.0F), test<invokedynamic>(this)));
        this.rainbowSaturation = this.register(new Setting("Saturation", Float.valueOf(150.0F), Float.valueOf(1.0F), Float.valueOf(255.0F), test<invokedynamic>(this)));
        this.particles = this.register(new Setting("Particles", Boolean.valueOf(false)));
        this.particleLength = this.register(new Setting("ParticleLength", Integer.valueOf(80), Integer.valueOf(0), Integer.valueOf(300), test<invokedynamic>(this)));
        this.particlered = this.register(new Setting("ParticleRed", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this)));
        this.particlegreen = this.register(new Setting("ParticleGreen", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this)));
        this.particleblue = this.register(new Setting("ParticleBlue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this)));
        this.setInstance();
    }

    public static ClickGui getInstance() {
        if (ClickGui.INSTANCE == null) {
            ClickGui.INSTANCE = new ClickGui();
        }

        return ClickGui.INSTANCE;
    }

    private void setInstance() {
        ClickGui.INSTANCE = this;
    }

    public void onUpdate() {
        if (((Boolean) this.customFov.getValue()).booleanValue()) {
            ClickGui.mc.gameSettings.setOptionFloatValue(Options.FOV, ((Float) this.fov.getValue()).floatValue());
        }

    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                OyVey.commandManager.setPrefix((String) this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + ChatFormatting.DARK_GRAY + OyVey.commandManager.getPrefix());
            }

            OyVey.colorManager.setColor(((Integer) this.red.getPlannedValue()).intValue(), ((Integer) this.green.getPlannedValue()).intValue(), ((Integer) this.blue.getPlannedValue()).intValue(), ((Integer) this.hoverAlpha.getPlannedValue()).intValue());
        }

    }

    public void onEnable() {
        ClickGui.mc.displayGuiScreen(OyVeyGui.getClickGui());
    }

    public void onLoad() {
        OyVey.colorManager.setColor(((Integer) this.red.getValue()).intValue(), ((Integer) this.green.getValue()).intValue(), ((Integer) this.blue.getValue()).intValue(), ((Integer) this.hoverAlpha.getValue()).intValue());
        OyVey.commandManager.setPrefix((String) this.prefix.getValue());
    }

    public void onTick() {
        if (!(ClickGui.mc.currentScreen instanceof OyVeyGui)) {
            this.disable();
        }

    }

    private boolean lambda$new$8(Object v) {
        return ((Boolean) this.particles.getValue()).booleanValue();
    }

    private boolean lambda$new$7(Object v) {
        return ((Boolean) this.particles.getValue()).booleanValue();
    }

    private boolean lambda$new$6(Object v) {
        return ((Boolean) this.particles.getValue()).booleanValue();
    }

    private boolean lambda$new$5(Object v) {
        return ((Boolean) this.particles.getValue()).booleanValue();
    }

    private boolean lambda$new$4(Object v) {
        return ((Boolean) this.rainbow.getValue()).booleanValue();
    }

    private boolean lambda$new$3(Object v) {
        return ((Boolean) this.rainbow.getValue()).booleanValue();
    }

    private boolean lambda$new$2(Object v) {
        return ((Boolean) this.rainbow.getValue()).booleanValue();
    }

    private boolean lambda$new$1(Object v) {
        return ((Boolean) this.rainbow.getValue()).booleanValue();
    }

    private boolean lambda$new$0(Object v) {
        return ((Boolean) this.rainbow.getValue()).booleanValue();
    }

    public static enum Gui {

        NEW, OLD;
    }

    public static enum rainbowMode {

        Static, Sideway;
    }

    public static enum rainbowModeArray {

        Static, Up;
    }
}
