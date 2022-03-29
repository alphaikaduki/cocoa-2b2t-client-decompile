package me.alpha432.oyvey.features.modules.misc;

import java.util.HashMap;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.entity.player.EntityPlayer;

public class PopCounter extends Module {

    public static HashMap TotemPopContainer = new HashMap();
    private static PopCounter INSTANCE = new PopCounter();

    public PopCounter() {
        super("PopCounter", "Counts other players totem pops.", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    public static PopCounter getInstance() {
        if (PopCounter.INSTANCE == null) {
            PopCounter.INSTANCE = new PopCounter();
        }

        return PopCounter.INSTANCE;
    }

    private void setInstance() {
        PopCounter.INSTANCE = this;
    }

    public void onEnable() {
        PopCounter.TotemPopContainer.clear();
    }

    public void onDeath(EntityPlayer player) {
        if (PopCounter.TotemPopContainer.containsKey(player.getName())) {
            int l_Count = ((Integer) PopCounter.TotemPopContainer.get(player.getName())).intValue();

            PopCounter.TotemPopContainer.remove(player.getName());
            if (l_Count == 1) {
                ;
            }
        }

    }

    public void onTotemPop(EntityPlayer player) {
        if (!fullNullCheck()) {
            if (!PopCounter.mc.player.equals(player)) {
                int l_Count = 1;

                if (PopCounter.TotemPopContainer.containsKey(player.getName())) {
                    l_Count = ((Integer) PopCounter.TotemPopContainer.get(player.getName())).intValue();
                    HashMap hashmap = PopCounter.TotemPopContainer;
                    String s = player.getName();

                    ++l_Count;
                    hashmap.put(s, Integer.valueOf(l_Count));
                } else {
                    PopCounter.TotemPopContainer.put(player.getName(), Integer.valueOf(l_Count));
                }

                if (l_Count == 1) {
                    ;
                }

            }
        }
    }
}
