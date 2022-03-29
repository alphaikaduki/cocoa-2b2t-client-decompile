package me.alpha432.oyvey.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.modules.client.Managers;
import me.alpha432.oyvey.features.modules.combat.AutoCrystal;
import me.alpha432.oyvey.util.BlockUtil3;
import me.alpha432.oyvey.util.DamageUtil;
import me.alpha432.oyvey.util.DamageUtill;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class SafetyManager extends Feature implements Runnable {

    private final Timer syncTimer = new Timer();
    private final AtomicBoolean SAFE = new AtomicBoolean(false);
    private ScheduledExecutorService service;

    public void run() {
        if (AutoCrystal.getInstance().isOff() || AutoCrystal.getInstance().threadMode.getValue() == AutoCrystal.ThreadMode.NONE) {
            this.doSafetyCheck();
        }

    }

    public void doSafetyCheck() {
        if (!fullNullCheck()) {
            boolean safe = true;
            EntityPlayer closest = ((Boolean) Managers.getInstance().safety.getValue()).booleanValue() ? EntityUtil.getClosestEnemy(18.0D) : null;

            if (((Boolean) Managers.getInstance().safety.getValue()).booleanValue() && closest == null) {
                this.SAFE.set(true);
                return;
            }

            ArrayList crystals = new ArrayList(SafetyManager.mc.world.loadedEntityList);
            Iterator iterator = crystals.iterator();

            while (iterator.hasNext()) {
                Entity pos = (Entity) iterator.next();

                if (pos instanceof EntityEnderCrystal && (double) DamageUtill.calculateDamage(pos, SafetyManager.mc.player) > 4.0D && (closest == null || closest.getDistanceSq(pos) < 40.0D)) {
                    safe = false;
                    break;
                }
            }

            if (safe) {
                iterator = BlockUtil3.possiblePlacePositions(4.0F, false, ((Boolean) Managers.getInstance().oneDot15.getValue()).booleanValue()).iterator();

                while (iterator.hasNext()) {
                    BlockPos pos1 = (BlockPos) iterator.next();

                    if ((double) DamageUtil.calculateDamage(pos1, SafetyManager.mc.player) > 4.0D && (closest == null || closest.getDistanceSq(pos1) < 40.0D)) {
                        safe = false;
                        break;
                    }
                }
            }

            this.SAFE.set(safe);
        }

    }

    public void onUpdate() {
        this.run();
    }

    public String getSafetyString() {
        return this.SAFE.get() ? "§aSecure" : "§cUnsafe";
    }

    public boolean isSafe() {
        return this.SAFE.get();
    }

    public ScheduledExecutorService getService() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        service.scheduleAtFixedRate(this, 0L, (long) ((Integer) Managers.getInstance().safetyCheck.getValue()).intValue(), TimeUnit.MILLISECONDS);
        return service;
    }
}
