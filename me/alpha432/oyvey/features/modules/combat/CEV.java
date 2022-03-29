package me.alpha432.oyvey.features.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class CEV extends Module {

    private List placeList;
    private boolean placing;
    private boolean placedCrystal;
    private boolean breaking;
    private boolean broke;
    private EntityPlayer _target;
    private BlockPos b_crystal;
    private BlockPos breakPos;
    private int attempts;
    private Setting targetType;
    private Setting breakMode;
    private Setting rotate;
    private Setting startDelay;
    private Setting breakDelay;
    private Setting crystalDelay;
    private Setting hitDelay;
    private Setting nosleep;
    private int timer;

    public CEV() {
        super("CEV BREAK", "Attack Ceil", Module.Category.COMBAT, true, false, false);
        this.placeList = new ArrayList();
        this.placing = false;
        this.placedCrystal = false;
        this.breaking = false;
        this.broke = false;
        this._target = null;
        this.b_crystal = null;
        this.breakPos = null;
        this.attempts = 0;
        this.targetType = this.register(new Setting("Target", CEV.type.NEAREST));
        this.breakMode = this.register(new Setting("Break Mode", CEV.mode.Vanilla));
        this.rotate = this.register(new Setting("Rotate", Boolean.valueOf(true)));
        this.startDelay = this.register(new Setting("Start Delay", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(10)));
        this.breakDelay = this.register(new Setting("Break Delay", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(10)));
        this.crystalDelay = this.register(new Setting("Crystal Delay", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(10)));
        this.hitDelay = this.register(new Setting("Hit Delay", Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(10)));
        this.nosleep = this.register(new Setting("Block Delay", Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(10)));
        this.timer = 0;
    }

    public CEV(String name, String description, Module.Category category, boolean hasListener, boolean hidden, boolean alwaysListening) {
        super(name, description, category, hasListener, hidden, alwaysListening);
    }

    public void onEnable() {
        this.init();
    }

    private void init() {
        this.placeList = new ArrayList();
        this._target = null;
        this.b_crystal = null;
        this.placedCrystal = false;
        this.placing = false;
        this.breaking = false;
        this.broke = false;
        this.timer = 0;
        this.attempts = 0;
    }

    public void onTick() {
        int pix = this.findItem(Items.DIAMOND_PICKAXE);
        int crystal = this.findItem(Items.END_CRYSTAL);
        int obby = this.findMaterials(Blocks.OBSIDIAN);

        if (pix != -1 && crystal != -1 && obby != -1) {
            if (this._target == null) {
                if (this.targetType.getValue() == CEV.type.NEAREST) {
                    this._target = (EntityPlayer) CEV.mc.world.playerEntities.stream().filter(test<invokedynamic>()).min(Comparator.comparing(apply<invokedynamic>())).orElse((Object) null);
                }

                if (this._target == null) {
                    this.disable();
                    return;
                }
            }

            if (this.placeList.size() == 0 && !this.placing) {
                this.searchSpace();
                if (this.placeList.size() == 0) {
                    this.disable();
                    return;
                }
            }

            if (!this.placedCrystal) {
                if (this.timer < ((Integer) this.startDelay.getValue()).intValue()) {
                    ++this.timer;
                    return;
                }

                this.timer = 0;
                this.doPlace(obby, crystal);
            } else if (!this.breaking) {
                if (this.timer < ((Integer) this.breakDelay.getValue()).intValue()) {
                    ++this.timer;
                    return;
                }

                this.timer = 0;
                if (this.breakMode.getValue() == CEV.mode.Vanilla) {
                    CEV.mc.player.inventory.currentItem = pix;
                    CEV.mc.playerController.updateController();
                    CEV.mc.player.swingArm(EnumHand.MAIN_HAND);
                    CEV.mc.playerController.onPlayerDamageBlock(this.breakPos, EnumFacing.DOWN);
                } else {
                    CEV.mc.player.swingArm(EnumHand.MAIN_HAND);
                    CEV.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, this.breakPos, EnumFacing.DOWN));
                    CEV.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, this.breakPos, EnumFacing.DOWN));
                }

                this.breaking = true;
            } else if (this.breaking && !this.broke) {
                if (this.getBlock(this.breakPos) == Blocks.AIR) {
                    this.broke = true;
                }
            } else if (this.broke) {
                if (this.timer < ((Integer) this.crystalDelay.getValue()).intValue()) {
                    ++this.timer;
                    return;
                }

                this.timer = 0;
                Entity bcrystal = (Entity) CEV.mc.world.loadedEntityList.stream().filter(test<invokedynamic>()).min(Comparator.comparing(apply<invokedynamic>(this))).orElse((Object) null);

                if (bcrystal == null) {
                    if (this.attempts < ((Integer) this.hitDelay.getValue()).intValue()) {
                        ++this.attempts;
                        return;
                    }

                    if (this.attempts < ((Integer) this.nosleep.getValue()).intValue()) {
                        ++this.attempts;
                        return;
                    }

                    this.placedCrystal = false;
                    this.placeList.add(this.breakPos);
                    this.breaking = false;
                    this.broke = false;
                    this.attempts = 0;
                } else {
                    CEV.mc.player.connection.sendPacket(new CPacketUseEntity(bcrystal));
                    this.placedCrystal = false;
                    this.placeList.add(this.breakPos);
                    this.breaking = false;
                    this.broke = false;
                    this.attempts = 0;
                }
            }

        } else {
            this.disable();
        }
    }

    private void doPlace(int obby, int crystal) {
        this.placing = true;
        int oldslot;

        if (this.placeList.size() != 0) {
            oldslot = CEV.mc.player.inventory.currentItem;
            CEV.mc.player.inventory.currentItem = obby;
            CEV.mc.playerController.updateController();
            BlockUtil.placeBlock((BlockPos) this.placeList.get(0), EnumHand.MAIN_HAND, ((Boolean) this.rotate.getValue()).booleanValue(), false, false);
            this.placeList.remove(0);
            CEV.mc.player.inventory.currentItem = oldslot;
        } else if (!this.placedCrystal) {
            oldslot = CEV.mc.player.inventory.currentItem;
            if (crystal != 999) {
                CEV.mc.player.inventory.currentItem = crystal;
            }

            CEV.mc.playerController.updateController();
            CEV.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.b_crystal, EnumFacing.UP, CEV.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
            CEV.mc.player.inventory.currentItem = oldslot;
            this.placedCrystal = true;
        }

    }

    private void searchSpace() {
        BlockPos ppos = CEV.mc.player.getPosition();
        BlockPos tpos = new BlockPos(this._target.posX, this._target.posY, this._target.posZ);

        this.placeList = new ArrayList();
        BlockPos[] offset = new BlockPos[] { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)};

        if (this.getBlock(new BlockPos(tpos.getX(), tpos.getY() + 3, tpos.getZ())) == Blocks.AIR && this.getBlock(new BlockPos(tpos.getX(), tpos.getY() + 4, tpos.getZ())) == Blocks.AIR) {
            ArrayList posList = new ArrayList();

            for (int base = 0; base < offset.length; ++base) {
                BlockPos offsetPos = tpos.add(offset[base]);
                Block block = this.getBlock(offsetPos);

                if (block != Blocks.AIR && !(block instanceof BlockLiquid)) {
                    posList.add(offsetPos);
                }
            }

            BlockPos blockpos = (BlockPos) posList.stream().max(Comparator.comparing(apply<invokedynamic>(this))).orElse((Object) null);

            if (blockpos != null) {
                this.placeList.add(blockpos);
                this.placeList.add(blockpos.add(0, 1, 0));
                this.placeList.add(blockpos.add(0, 2, 0));
                this.placeList.add(tpos.add(0, 2, 0));
                this.breakPos = tpos.add(0, 2, 0);
                this.b_crystal = tpos.add(0, 2, 0);
            }
        }
    }

    private int findMaterials(Block b) {
        for (int i = 0; i < 9; ++i) {
            if (CEV.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && ((ItemBlock) CEV.mc.player.inventory.getStackInSlot(i).getItem()).getBlock() == b) {
                return i;
            }
        }

        return -1;
    }

    private int findItem(Item item) {
        if (item == Items.END_CRYSTAL && CEV.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            return 999;
        } else {
            for (int i = 0; i < 9; ++i) {
                if (CEV.mc.player.inventory.getStackInSlot(i).getItem() == item) {
                    return i;
                }
            }

            return -1;
        }
    }

    private Block getBlock(BlockPos b) {
        return CEV.mc.world.getBlockState(b).getBlock();
    }

    private Double lambda$searchSpace$4(BlockPos b) {
        return Double.valueOf(this._target.getDistance((double) b.getX(), (double) b.getY(), (double) b.getZ()));
    }

    private Float lambda$onTick$3(Entity c) {
        return Float.valueOf(c.getDistance(this._target));
    }

    private static boolean lambda$onTick$2(Entity e) {
        return e instanceof EntityEnderCrystal;
    }

    private static Float lambda$onTick$1(EntityPlayer p) {
        return Float.valueOf(p.getDistance(CEV.mc.player));
    }

    private static boolean lambda$onTick$0(EntityPlayer p) {
        return p.getEntityId() != CEV.mc.player.getEntityId();
    }

    public static enum mode {

        Vanilla, Packet;
    }

    public static enum type {

        NEAREST, LOOKING;
    }
}
