package me.alpha432.oyvey.mixin;

import java.util.Map;
import me.alpha432.oyvey.OyVey;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

public class OyVeyLoader implements IFMLLoadingPlugin {

    private static boolean isObfuscatedEnvironment = false;

    public OyVeyLoader() {
        OyVey.LOGGER.info("\n\nLoading mixins by Alpha432");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.crepe.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("name");
        OyVey.LOGGER.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }

    public String[] getASMTransformerClass() {
        return new String[0];
    }

    public String getModContainerClass() {
        return null;
    }

    public String getSetupClass() {
        return null;
    }

    public void injectData(Map data) {
        OyVeyLoader.isObfuscatedEnvironment = ((Boolean) data.get("runtimeDeobfuscationEnabled")).booleanValue();
    }

    public String getAccessTransformerClass() {
        return null;
    }
}
