package me.alpha432.oyvey.features.gui.custom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.fml.common.FMLLog;

public class GuiCustomSong {

    public static List getHWIDList() {
        ArrayList HWIDList = new ArrayList();

        try {
            new URL("https://pastebin.com/raw/rtD4fBmG");
            new URL("https://pastebin.com/raw/rtD4fBmG");
            URL url = new URL("https://pastebin.com/raw/c3ymbpH8");

            new URL("https://pastebin.com/raw/rtD4fBmG");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                HWIDList.add(inputLine);
            }
        } catch (Exception exception) {
            FMLLog.log.info("Load HWID Failed!");
        }

        return HWIDList;
    }
}
