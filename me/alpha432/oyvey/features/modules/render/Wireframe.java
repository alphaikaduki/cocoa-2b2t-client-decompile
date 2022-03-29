package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraftforge.client.event.RenderPlayerEvent.Pre;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Wireframe extends Module {

    private static Wireframe INSTANCE = new Wireframe();
    public final Setting alpha = this.register(new Setting("PAlpha", Float.valueOf(255.0F), Float.valueOf(0.1F), Float.valueOf(255.0F)));
    public final Setting cAlpha = this.register(new Setting("CAlpha", Float.valueOf(255.0F), Float.valueOf(0.1F), Float.valueOf(255.0F)));
    public final Setting lineWidth = this.register(new Setting("PLineWidth", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(3.0F)));
    public final Setting crystalLineWidth = this.register(new Setting("CLineWidth", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(3.0F)));
    public Setting mode;
    public Setting cMode;
    public Setting players;
    public Setting playerModel;
    public Setting crystals;
    public Setting crystalModel;

    public Wireframe() {
        super("Wireframe", "Draws a wireframe esp around other players.", Module.Category.RENDER, false, false, false);
        this.mode = this.register(new Setting("PMode", Wireframe.RenderMode.SOLID));
        this.cMode = this.register(new Setting("CMode", Wireframe.RenderMode.SOLID));
        this.players = this.register(new Setting("Players", Boolean.FALSE));
        this.playerModel = this.register(new Setting("PlayerModel", Boolean.FALSE));
        this.crystals = this.register(new Setting("Crystals", Boolean.FALSE));
        this.crystalModel = this.register(new Setting("CrystalModel", Boolean.FALSE));
        this.setInstance();
    }

    public static Wireframe getINSTANCE() {
        if (Wireframe.INSTANCE == null) {
            Wireframe.INSTANCE = new Wireframe();
        }

        return Wireframe.INSTANCE;
    }

    private void setInstance() {
        Wireframe.INSTANCE = this;
    }

    @SubscribeEvent
    public void onRenderPlayerEvent(Pre event) {
        event.getEntityPlayer().hurtTime = 0;
    }

    public static enum RenderMode {

        SOLID, WIREFRAME;
    }
}
