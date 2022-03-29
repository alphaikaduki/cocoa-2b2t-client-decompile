package me.alpha432.oyvey.features.modules.render;

import java.util.function.Predicate;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class Chams extends Module {

    private static Chams INSTANCE = new Chams();
    public Setting colorSync = this.register(new Setting("Sync", Boolean.valueOf(false)));
    public Setting colored = this.register(new Setting("Colored", Boolean.valueOf(false)));
    public Setting textured = this.register(new Setting("Textured", Boolean.valueOf(false)));
    public Setting rainbow;
    public Setting saturation;
    public Setting brightness;
    public Setting speed;
    public Setting xqz;
    public Setting red;
    public Setting green;
    public Setting blue;
    public Setting alpha;
    public Setting hiddenRed;
    public Setting hiddenGreen;
    public Setting hiddenBlue;
    public Setting hiddenAlpha;

    public Chams() {
        super("Chams", "Renders players through walls.", Module.Category.RENDER, false, false, false);
        this.rainbow = this.register(new Setting("Rainbow", Boolean.FALSE, (v) -> {
            return ((Boolean) this.colored.getValue()).booleanValue();
        }));
        this.saturation = this.register(new Setting("Saturation", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(100), (v) -> {
            return ((Boolean) this.colored.getValue()).booleanValue() && ((Boolean) this.rainbow.getValue()).booleanValue();
        }));
        this.brightness = this.register(new Setting("Brightness", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(100), (v) -> {
            return ((Boolean) this.colored.getValue()).booleanValue() && ((Boolean) this.rainbow.getValue()).booleanValue();
        }));
        this.speed = this.register(new Setting("Speed", Integer.valueOf(40), Integer.valueOf(1), Integer.valueOf(100), (v) -> {
            return ((Boolean) this.colored.getValue()).booleanValue() && ((Boolean) this.rainbow.getValue()).booleanValue();
        }));
        this.xqz = this.register(new Setting("XQZ", Boolean.FALSE, (v) -> {
            return ((Boolean) this.colored.getValue()).booleanValue() && !((Boolean) this.rainbow.getValue()).booleanValue();
        }));
        this.red = this.register(new Setting("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), (v) -> {
            return ((Boolean) this.colored.getValue()).booleanValue() && !((Boolean) this.rainbow.getValue()).booleanValue();
        }));
        this.green = this.register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), (v) -> {
            return ((Boolean) this.colored.getValue()).booleanValue() && !((Boolean) this.rainbow.getValue()).booleanValue();
        }));
        this.blue = this.register(new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), (v) -> {
            return ((Boolean) this.colored.getValue()).booleanValue() && !((Boolean) this.rainbow.getValue()).booleanValue();
        }));
        this.alpha = this.register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), (v) -> {
            return ((Boolean) this.colored.getValue()).booleanValue();
        }));
        this.hiddenRed = this.register(new Setting("Hidden Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), (v) -> {
            return ((Boolean) this.colored.getValue()).booleanValue() && ((Boolean) this.xqz.getValue()).booleanValue() && !((Boolean) this.rainbow.getValue()).booleanValue();
        }));
        this.hiddenGreen = this.register(new Setting("Hidden Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), (v) -> {
            return ((Boolean) this.colored.getValue()).booleanValue() && ((Boolean) this.xqz.getValue()).booleanValue() && !((Boolean) this.rainbow.getValue()).booleanValue();
        }));
        this.hiddenBlue = this.register(new Setting("Hidden Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), (v) -> {
            return ((Boolean) this.colored.getValue()).booleanValue() && ((Boolean) this.xqz.getValue()).booleanValue() && !((Boolean) this.rainbow.getValue()).booleanValue();
        }));
        this.hiddenAlpha = this.register(new Setting("Hidden Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), (v) -> {
            return ((Boolean) this.colored.getValue()).booleanValue() && ((Boolean) this.xqz.getValue()).booleanValue() && !((Boolean) this.rainbow.getValue()).booleanValue();
        }));
        this.setInstance();
    }

    public static Chams getInstance() {
        if (Chams.INSTANCE == null) {
            Chams.INSTANCE = new Chams();
        }

        return Chams.INSTANCE;
    }

    private void setInstance() {
        Chams.INSTANCE = this;
    }
}
