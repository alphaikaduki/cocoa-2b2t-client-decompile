package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class ItemPhysics extends Module {

    public static ItemPhysics INSTANCE = new ItemPhysics();
    public final Setting Scaling = this.register(new Setting("Scaling", Float.valueOf(0.5F), Float.valueOf(0.0F), Float.valueOf(10.0F)));

    public ItemPhysics() {
        super("ItemPhysics", "Apply physics to items.", Module.Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static ItemPhysics getInstance() {
        if (ItemPhysics.INSTANCE == null) {
            ItemPhysics.INSTANCE = new ItemPhysics();
        }

        return ItemPhysics.INSTANCE;
    }

    private void setInstance() {
        ItemPhysics.INSTANCE = this;
    }
}
