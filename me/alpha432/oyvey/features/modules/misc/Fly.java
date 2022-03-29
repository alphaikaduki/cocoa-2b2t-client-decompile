package me.alpha432.oyvey.features.modules.misc;

import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
    modid = "crepehwid",
    name = "CrepeHWID",
    version = "1.0"
)
public class Fly {

    public static final String MODID = "crepehwid";
    public static final String NAME = "CrepeHWID";
    public static final String VERSION = "1.0";
    public static List hwidList = new ArrayList();
    public static final String KEY = "verify";
    public static final String HWID_URL = "https://pastebin.com/raw/rtD4fBmG";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        this.Verify();
    }

    public void Verify() {}
}
