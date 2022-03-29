package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.events.RenderItemEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ViewModel extends Module {

    private static ViewModel INSTANCE = new ViewModel();
    public Setting settings;
    public Setting noEatAnimation;
    public Setting eatX;
    public Setting eatY;
    public Setting doBob;
    public Setting mainX;
    public Setting mainY;
    public Setting mainZ;
    public Setting offX;
    public Setting offY;
    public Setting offZ;
    public Setting mainRotX;
    public Setting mainRotY;
    public Setting mainRotZ;
    public Setting offRotX;
    public Setting offRotY;
    public Setting offRotZ;
    public Setting offScaleX;
    public Setting offScaleY;
    public Setting offScaleZ;

    public ViewModel() {
        super("ItemModel", "Cool", Module.Category.RENDER, true, false, false);
        this.settings = this.register(new Setting("Settings", ViewModel.Settings.TRANSLATE));
        this.noEatAnimation = this.register(new Setting("NoEatAnimation", Boolean.valueOf(false), test<invokedynamic>(this)));
        this.eatX = this.register(new Setting("EatX", Double.valueOf(1.0D), Double.valueOf(-2.0D), Double.valueOf(5.0D), test<invokedynamic>(this)));
        this.eatY = this.register(new Setting("EatY", Double.valueOf(1.0D), Double.valueOf(-2.0D), Double.valueOf(5.0D), test<invokedynamic>(this)));
        this.doBob = this.register(new Setting("ItemBob", Boolean.valueOf(true), test<invokedynamic>(this)));
        this.mainX = this.register(new Setting("MainX", Double.valueOf(1.2D), Double.valueOf(-2.0D), Double.valueOf(4.0D), test<invokedynamic>(this)));
        this.mainY = this.register(new Setting("MainY", Double.valueOf(-0.95D), Double.valueOf(-3.0D), Double.valueOf(3.0D), test<invokedynamic>(this)));
        this.mainZ = this.register(new Setting("MainZ", Double.valueOf(-1.45D), Double.valueOf(-5.0D), Double.valueOf(5.0D), test<invokedynamic>(this)));
        this.offX = this.register(new Setting("OffX", Double.valueOf(1.2D), Double.valueOf(-2.0D), Double.valueOf(4.0D), test<invokedynamic>(this)));
        this.offY = this.register(new Setting("OffY", Double.valueOf(-0.95D), Double.valueOf(-3.0D), Double.valueOf(3.0D), test<invokedynamic>(this)));
        this.offZ = this.register(new Setting("OffZ", Double.valueOf(-1.45D), Double.valueOf(-5.0D), Double.valueOf(5.0D), test<invokedynamic>(this)));
        this.mainRotX = this.register(new Setting("MainRotationX", Integer.valueOf(0), Integer.valueOf(-36), Integer.valueOf(36), test<invokedynamic>(this)));
        this.mainRotY = this.register(new Setting("MainRotationY", Integer.valueOf(0), Integer.valueOf(-36), Integer.valueOf(36), test<invokedynamic>(this)));
        this.mainRotZ = this.register(new Setting("MainRotationZ", Integer.valueOf(0), Integer.valueOf(-36), Integer.valueOf(36), test<invokedynamic>(this)));
        this.offRotX = this.register(new Setting("OffRotationX", Integer.valueOf(0), Integer.valueOf(-36), Integer.valueOf(36), test<invokedynamic>(this)));
        this.offRotY = this.register(new Setting("OffRotationY", Integer.valueOf(0), Integer.valueOf(-36), Integer.valueOf(36), test<invokedynamic>(this)));
        this.offRotZ = this.register(new Setting("OffRotationZ", Integer.valueOf(0), Integer.valueOf(-36), Integer.valueOf(36), test<invokedynamic>(this)));
        this.offScaleX = this.register(new Setting("OffScaleX", Double.valueOf(1.0D), Double.valueOf(0.1D), Double.valueOf(5.0D), test<invokedynamic>(this)));
        this.offScaleY = this.register(new Setting("OffScaleY", Double.valueOf(1.0D), Double.valueOf(0.1D), Double.valueOf(5.0D), test<invokedynamic>(this)));
        this.offScaleZ = this.register(new Setting("OffScaleZ", Double.valueOf(1.0D), Double.valueOf(0.1D), Double.valueOf(5.0D), test<invokedynamic>(this)));
        this.setInstance();
    }

    public static ViewModel getInstance() {
        if (ViewModel.INSTANCE == null) {
            ViewModel.INSTANCE = new ViewModel();
        }

        return ViewModel.INSTANCE;
    }

    private void setInstance() {
        ViewModel.INSTANCE = this;
    }

    @SubscribeEvent
    public void onItemRender(RenderItemEvent event) {
        event.setMainX(((Double) this.mainX.getValue()).doubleValue());
        event.setMainY(((Double) this.mainY.getValue()).doubleValue());
        event.setMainZ(((Double) this.mainZ.getValue()).doubleValue());
        event.setOffX(-((Double) this.offX.getValue()).doubleValue());
        event.setOffY(((Double) this.offY.getValue()).doubleValue());
        event.setOffZ(((Double) this.offZ.getValue()).doubleValue());
        event.setMainRotX((double) (((Integer) this.mainRotX.getValue()).intValue() * 5));
        event.setMainRotY((double) (((Integer) this.mainRotY.getValue()).intValue() * 5));
        event.setMainRotZ((double) (((Integer) this.mainRotZ.getValue()).intValue() * 5));
        event.setOffRotX((double) (((Integer) this.offRotX.getValue()).intValue() * 5));
        event.setOffRotY((double) (((Integer) this.offRotY.getValue()).intValue() * 5));
        event.setOffRotZ((double) (((Integer) this.offRotZ.getValue()).intValue() * 5));
        event.setOffHandScaleX(((Double) this.offScaleX.getValue()).doubleValue());
        event.setOffHandScaleY(((Double) this.offScaleY.getValue()).doubleValue());
        event.setOffHandScaleZ(((Double) this.offScaleZ.getValue()).doubleValue());
    }

    private boolean lambda$new$18(Double v) {
        return this.settings.getValue() == ViewModel.Settings.SCALE;
    }

    private boolean lambda$new$17(Double v) {
        return this.settings.getValue() == ViewModel.Settings.SCALE;
    }

    private boolean lambda$new$16(Double v) {
        return this.settings.getValue() == ViewModel.Settings.SCALE;
    }

    private boolean lambda$new$15(Integer v) {
        return this.settings.getValue() == ViewModel.Settings.ROTATE;
    }

    private boolean lambda$new$14(Integer v) {
        return this.settings.getValue() == ViewModel.Settings.ROTATE;
    }

    private boolean lambda$new$13(Integer v) {
        return this.settings.getValue() == ViewModel.Settings.ROTATE;
    }

    private boolean lambda$new$12(Integer v) {
        return this.settings.getValue() == ViewModel.Settings.ROTATE;
    }

    private boolean lambda$new$11(Integer v) {
        return this.settings.getValue() == ViewModel.Settings.ROTATE;
    }

    private boolean lambda$new$10(Integer v) {
        return this.settings.getValue() == ViewModel.Settings.ROTATE;
    }

    private boolean lambda$new$9(Double v) {
        return this.settings.getValue() == ViewModel.Settings.TRANSLATE;
    }

    private boolean lambda$new$8(Double v) {
        return this.settings.getValue() == ViewModel.Settings.TRANSLATE;
    }

    private boolean lambda$new$7(Double v) {
        return this.settings.getValue() == ViewModel.Settings.TRANSLATE;
    }

    private boolean lambda$new$6(Double v) {
        return this.settings.getValue() == ViewModel.Settings.TRANSLATE;
    }

    private boolean lambda$new$5(Double v) {
        return this.settings.getValue() == ViewModel.Settings.TRANSLATE;
    }

    private boolean lambda$new$4(Double v) {
        return this.settings.getValue() == ViewModel.Settings.TRANSLATE;
    }

    private boolean lambda$new$3(Boolean v) {
        return this.settings.getValue() == ViewModel.Settings.TWEAKS;
    }

    private boolean lambda$new$2(Double v) {
        return this.settings.getValue() == ViewModel.Settings.TWEAKS && !((Boolean) this.noEatAnimation.getValue()).booleanValue();
    }

    private boolean lambda$new$1(Double v) {
        return this.settings.getValue() == ViewModel.Settings.TWEAKS && !((Boolean) this.noEatAnimation.getValue()).booleanValue();
    }

    private boolean lambda$new$0(Boolean v) {
        return this.settings.getValue() == ViewModel.Settings.TWEAKS;
    }

    private static enum Settings {

        TRANSLATE, ROTATE, SCALE, TWEAKS;
    }
}
