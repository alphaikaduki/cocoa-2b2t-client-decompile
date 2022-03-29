package me.alpha432.oyvey.features.modules.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.InventoryUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Module {

    private final Setting delay = this.register(new Setting("Delay", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500)));
    private final Setting curse = this.register(new Setting("Vanishing", Boolean.valueOf(false)));
    private final Setting mendingTakeOff = this.register(new Setting("AutoMend", Boolean.valueOf(false)));
    private final Setting closestEnemy = this.register(new Setting("Enemy", Integer.valueOf(8), Integer.valueOf(1), Integer.valueOf(20), (v) -> {
        return ((Boolean) this.mendingTakeOff.getValue()).booleanValue();
    }));
    private final Setting repair = this.register(new Setting("Repair%", Integer.valueOf(80), Integer.valueOf(1), Integer.valueOf(100), (v) -> {
        return ((Boolean) this.mendingTakeOff.getValue()).booleanValue();
    }));
    private final Setting actions = this.register(new Setting("Packets", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(12)));
    private final Timer timer = new Timer();
    private final Queue taskList = new ConcurrentLinkedQueue();
    private final List doneSlots = new ArrayList();
    boolean flag;

    public AutoArmor() {
        super("AutoArmor", "Puts Armor on for you.", Module.Category.COMBAT, true, false, false);
    }

    public void onLogin() {
        this.timer.reset();
    }

    public void onDisable() {
        this.taskList.clear();
        this.doneSlots.clear();
        this.flag = false;
    }

    public void onLogout() {
        this.taskList.clear();
        this.doneSlots.clear();
    }

    public void onTick() {
        if (!fullNullCheck() && (!(AutoArmor.mc.currentScreen instanceof GuiContainer) || AutoArmor.mc.currentScreen instanceof GuiInventory)) {
            int i;

            if (this.taskList.isEmpty()) {
                if (((Boolean) this.mendingTakeOff.getValue()).booleanValue() && InventoryUtil.holdingItem(ItemExpBottle.class) && AutoArmor.mc.gameSettings.keyBindUseItem.isKeyDown() && AutoArmor.mc.world.playerEntities.stream().noneMatch((e) -> {
                    return e != AutoArmor.mc.player && !OyVey.friendManager.isFriend(e.getName()) && AutoArmor.mc.player.getDistance(e) <= (float) ((Integer) this.closestEnemy.getValue()).intValue();
                }) && !this.flag) {
                    int takeOff = 0;
                    Iterator itemStack1 = this.getArmor().entrySet().iterator();

                    int dam;
                    ItemStack itemStack3;
                    float itemStack4;

                    while (itemStack1.hasNext()) {
                        Entry itemStack2 = (Entry) itemStack1.next();

                        itemStack3 = (ItemStack) itemStack2.getValue();
                        itemStack4 = (float) ((Integer) this.repair.getValue()).intValue() / 100.0F;
                        dam = Math.round((float) itemStack3.getMaxDamage() * itemStack4);
                        if (dam < itemStack3.getMaxDamage() - itemStack3.getItemDamage()) {
                            ++takeOff;
                        }
                    }

                    if (takeOff == 4) {
                        this.flag = true;
                    }

                    if (!this.flag) {
                        ItemStack itemstack = AutoArmor.mc.player.inventoryContainer.getSlot(5).getStack();

                        if (!itemstack.isEmpty) {
                            float f = (float) ((Integer) this.repair.getValue()).intValue() / 100.0F;
                            int i = Math.round((float) itemstack.getMaxDamage() * f);

                            if (i < itemstack.getMaxDamage() - itemstack.getItemDamage()) {
                                this.takeOffSlot(5);
                            }
                        }

                        ItemStack itemstack1 = AutoArmor.mc.player.inventoryContainer.getSlot(6).getStack();

                        if (!itemstack1.isEmpty) {
                            itemStack4 = (float) ((Integer) this.repair.getValue()).intValue() / 100.0F;
                            int goods4 = Math.round((float) itemstack1.getMaxDamage() * itemStack4);

                            if (goods4 < itemstack1.getMaxDamage() - itemstack1.getItemDamage()) {
                                this.takeOffSlot(6);
                            }
                        }

                        itemStack3 = AutoArmor.mc.player.inventoryContainer.getSlot(7).getStack();
                        if (!itemStack3.isEmpty) {
                            itemStack4 = (float) ((Integer) this.repair.getValue()).intValue() / 100.0F;
                            dam = Math.round((float) itemStack3.getMaxDamage() * itemStack4);
                            if (dam < itemStack3.getMaxDamage() - itemStack3.getItemDamage()) {
                                this.takeOffSlot(7);
                            }
                        }

                        ItemStack itemstack2 = AutoArmor.mc.player.inventoryContainer.getSlot(8).getStack();

                        if (!itemstack2.isEmpty) {
                            float percent = (float) ((Integer) this.repair.getValue()).intValue() / 100.0F;
                            int dam4 = Math.round((float) itemstack2.getMaxDamage() * percent);

                            if (dam4 < itemstack2.getMaxDamage() - itemstack2.getItemDamage()) {
                                this.takeOffSlot(8);
                            }
                        }
                    }

                    return;
                }

                this.flag = false;
                ItemStack helm = AutoArmor.mc.player.inventoryContainer.getSlot(5).getStack();
                int slot4;

                if (helm.getItem() == Items.AIR && (slot4 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.HEAD, ((Boolean) this.curse.getValue()).booleanValue(), true)) != -1) {
                    this.getSlotOn(5, slot4);
                }

                int slot3;

                if (AutoArmor.mc.player.inventoryContainer.getSlot(6).getStack().getItem() == Items.AIR && (slot3 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.CHEST, ((Boolean) this.curse.getValue()).booleanValue(), true)) != -1) {
                    this.getSlotOn(6, slot3);
                }

                int slot2;

                if (AutoArmor.mc.player.inventoryContainer.getSlot(7).getStack().getItem() == Items.AIR && (slot2 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.LEGS, ((Boolean) this.curse.getValue()).booleanValue(), true)) != -1) {
                    this.getSlotOn(7, slot2);
                }

                if (AutoArmor.mc.player.inventoryContainer.getSlot(8).getStack().getItem() == Items.AIR && (i = InventoryUtil.findArmorSlot(EntityEquipmentSlot.FEET, ((Boolean) this.curse.getValue()).booleanValue(), true)) != -1) {
                    this.getSlotOn(8, i);
                }
            }

            if (this.timer.passedMs((long) ((int) ((float) ((Integer) this.delay.getValue()).intValue() * OyVey.serverManager.getTpsFactor())))) {
                if (!this.taskList.isEmpty()) {
                    for (i = 0; i < ((Integer) this.actions.getValue()).intValue(); ++i) {
                        InventoryUtil.Task task = (InventoryUtil.Task) this.taskList.poll();

                        if (task != null) {
                            task.run();
                        }
                    }
                }

                this.timer.reset();
            }

        }
    }

    private void takeOffSlot(int slot) {
        if (this.taskList.isEmpty()) {
            int target = -1;
            Iterator iterator = InventoryUtil.findEmptySlots(true).iterator();

            while (iterator.hasNext()) {
                int i = ((Integer) iterator.next()).intValue();

                if (!this.doneSlots.contains(Integer.valueOf(target))) {
                    target = i;
                    this.doneSlots.add(Integer.valueOf(i));
                }
            }

            if (target != -1) {
                this.taskList.add(new InventoryUtil.Task(slot));
                this.taskList.add(new InventoryUtil.Task(target));
                this.taskList.add(new InventoryUtil.Task());
            }
        }

    }

    private void getSlotOn(int slot, int target) {
        if (this.taskList.isEmpty()) {
            this.doneSlots.remove(Integer.valueOf(target));
            this.taskList.add(new InventoryUtil.Task(target));
            this.taskList.add(new InventoryUtil.Task(slot));
            this.taskList.add(new InventoryUtil.Task());
        }

    }

    private Map getArmor() {
        return this.getInventorySlots(5, 8);
    }

    private Map getInventorySlots(int current, int last) {
        HashMap fullInventorySlots;

        for (fullInventorySlots = new HashMap(); current <= last; ++current) {
            fullInventorySlots.put(Integer.valueOf(current), AutoArmor.mc.player.inventoryContainer.getInventory().get(current));
        }

        return fullInventorySlots;
    }
}
