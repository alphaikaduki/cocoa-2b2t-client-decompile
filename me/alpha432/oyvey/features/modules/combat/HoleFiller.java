package me.alpha432.oyvey.features.modules.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import me.alpha432.oyvey.util.TestUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class HoleFiller extends Module {

    private static final BlockPos[] surroundOffset = BlockUtil.toBlockPos(EntityUtil.getOffsets(0, true));
    private static HoleFiller INSTANCE = new HoleFiller();
    private final Setting range = this.register(new Setting("PlaceRange", Integer.valueOf(8), Integer.valueOf(0), Integer.valueOf(10)));
    private final Setting delay = this.register(new Setting("Delay", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(250)));
    private final Setting blocksPerTick = this.register(new Setting("BlocksPerTick", Integer.valueOf(20), Integer.valueOf(8), Integer.valueOf(30)));
    private final Timer offTimer = new Timer();
    private final Timer timer = new Timer();
    private final Map retries = new HashMap();
    private final Timer retryTimer = new Timer();
    private int blocksThisTick = 0;
    private ArrayList holes = new ArrayList();
    private int trie;

    public HoleFiller() {
        super("HoleFiller", "Fills holes around you.", Module.Category.COMBAT, true, false, true);
        this.setInstance();
    }

    public static HoleFiller getInstance() {
        if (HoleFiller.INSTANCE == null) {
            HoleFiller.INSTANCE = new HoleFiller();
        }

        return HoleFiller.INSTANCE;
    }

    private void setInstance() {
        HoleFiller.INSTANCE = this;
    }

    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
        }

        this.offTimer.reset();
        this.trie = 0;
    }

    public void onTick() {
        if (this.isOn()) {
            this.doHoleFill();
        }

    }

    public void onDisable() {
        this.retries.clear();
    }

    private void doHoleFill() {
        if (!this.check()) {
            this.holes = new ArrayList();
            Iterable blocks = BlockPos.getAllInBox(HoleFiller.mc.player.getPosition().add(-((Integer) this.range.getValue()).intValue(), -((Integer) this.range.getValue()).intValue(), -((Integer) this.range.getValue()).intValue()), HoleFiller.mc.player.getPosition().add(((Integer) this.range.getValue()).intValue(), ((Integer) this.range.getValue()).intValue(), ((Integer) this.range.getValue()).intValue()));
            Iterator iterator = blocks.iterator();

            while (iterator.hasNext()) {
                BlockPos pos = (BlockPos) iterator.next();

                if (!HoleFiller.mc.world.getBlockState(pos).getMaterial().blocksMovement() && !HoleFiller.mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial().blocksMovement()) {
                    boolean solidNeighbours = HoleFiller.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK | HoleFiller.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN && HoleFiller.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK | HoleFiller.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN && HoleFiller.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK | HoleFiller.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN && HoleFiller.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK | HoleFiller.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN && HoleFiller.mc.world.getBlockState(pos.add(0, 0, 0)).getMaterial() == Material.AIR && HoleFiller.mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial() == Material.AIR && HoleFiller.mc.world.getBlockState(pos.add(0, 2, 0)).getMaterial() == Material.AIR;

                    if (solidNeighbours) {
                        this.holes.add(pos);
                    }
                }
            }

            this.holes.forEach(this::placeBlock);
            this.toggle();
        }
    }

    private void placeBlock(BlockPos pos) {
        Iterator obbySlot = HoleFiller.mc.world.getEntitiesWithinAABBExcludingEntity((Entity) null, new AxisAlignedBB(pos)).iterator();

        Entity eChestSot;

        do {
            if (!obbySlot.hasNext()) {
                if (this.blocksThisTick < ((Integer) this.blocksPerTick.getValue()).intValue()) {
                    int i = InventoryUtil.findHotbarBlock(BlockObsidian.class);
                    int j = InventoryUtil.findHotbarBlock(BlockEnderChest.class);

                    if (i == -1 && j == -1) {
                        this.toggle();
                    }

                    int originalSlot = HoleFiller.mc.player.inventory.currentItem;

                    HoleFiller.mc.player.inventory.currentItem = i == -1 ? j : i;
                    HoleFiller.mc.playerController.updateController();
                    TestUtil.placeBlock(pos);
                    if (HoleFiller.mc.player.inventory.currentItem != originalSlot) {
                        HoleFiller.mc.player.inventory.currentItem = originalSlot;
                        HoleFiller.mc.playerController.updateController();
                    }

                    this.timer.reset();
                    ++this.blocksThisTick;
                }

                return;
            }

            eChestSot = (Entity) obbySlot.next();
        } while (!(eChestSot instanceof EntityLivingBase));

    }

    private boolean check() {
        if (fullNullCheck()) {
            this.disable();
            return true;
        } else {
            this.blocksThisTick = 0;
            if (this.retryTimer.passedMs(2000L)) {
                this.retries.clear();
                this.retryTimer.reset();
            }

            return !this.timer.passedMs((long) ((Integer) this.delay.getValue()).intValue());
        }
    }
}
