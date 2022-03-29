package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;

public class MultiTask extends Module {

    private static MultiTask INSTANCE = new MultiTask();

    public MultiTask() {
        super("MultiTask", "Allows you to eat while mining.", Module.Category.PLAYER, false, false, false);
        this.setInstance();
    }

    public static MultiTask getInstance() {
        if (MultiTask.INSTANCE == null) {
            MultiTask.INSTANCE = new MultiTask();
        }

        return MultiTask.INSTANCE;
    }

    private void setInstance() {
        MultiTask.INSTANCE = this;
    }
}
