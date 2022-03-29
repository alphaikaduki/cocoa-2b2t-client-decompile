package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class SelfAnvil extends Module {

    private final Setting rotate = this.register(new Setting("Rotate", Boolean.valueOf(true)));
    private final Setting onlyHole = this.register(new Setting("HoleOnly", Boolean.valueOf(false)));
    private final Setting helpingBlocks = this.register(new Setting("HelpingBlocks", Boolean.valueOf(true)));
    private final Setting chat = this.register(new Setting("Chat Msgs", Boolean.valueOf(true)));
    private final Setting packet = this.register(new Setting("Packet", Boolean.valueOf(false)));
    private final Setting blocksPerTick = this.register(new Setting("Blocks/Tick", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(8)));
    private BlockPos placePos;
    private BlockPos playerPos;
    private int blockSlot;
    private int obbySlot;
    private int lastBlock;
    private int blocksThisTick;

    public SelfAnvil() {
        super("SelfAnvil", "funne falling block", Module.Category.COMBAT, true, false, false);
    }

    public void onEnable() {
        this.playerPos = new BlockPos(SelfAnvil.mc.player.posX, SelfAnvil.mc.player.posY, SelfAnvil.mc.player.posZ);
        this.placePos = this.playerPos.offset(EnumFacing.UP, 2);
        this.blockSlot = this.findBlockSlot();
        this.obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        this.lastBlock = SelfAnvil.mc.player.inventory.currentItem;
        if (!this.doFirstChecks()) {
            this.disable();
        }

    }

    public void onTick() {
        this.blocksThisTick = 0;
        this.doSelfAnvil();
    }

    private void doSelfAnvil() {
        if (((Boolean) this.helpingBlocks.getValue()).booleanValue() && BlockUtil.isPositionPlaceable(this.placePos, false, true) == 2) {
            InventoryUtil.switchToHotbarSlot(this.obbySlot, false);
            this.doHelpBlocks();
        }

        if (this.blocksThisTick < ((Integer) this.blocksPerTick.getValue()).intValue() && BlockUtil.isPositionPlaceable(this.placePos, false, true) == 3) {
            InventoryUtil.switchToHotbarSlot(this.blockSlot, false);
            BlockUtil.placeBlock(this.placePos, EnumHand.MAIN_HAND, ((Boolean) this.rotate.getValue()).booleanValue(), ((Boolean) this.packet.getValue()).booleanValue(), false);
            InventoryUtil.switchToHotbarSlot(this.lastBlock, false);
            SelfAnvil.mc.player.connection.sendPacket(new CPacketEntityAction(SelfAnvil.mc.player, Action.STOP_SNEAKING));
            this.disable();
        }

    }

    private void doHelpBlocks() {
        if (this.blocksThisTick < ((Integer) this.blocksPerTick.getValue()).intValue()) {
            EnumFacing[] aenumfacing = EnumFacing.values();
            int i = aenumfacing.length;

            int j;
            EnumFacing side1;

            for (j = 0; j < i; ++j) {
                side1 = aenumfacing[j];
                if (side1 != EnumFacing.DOWN && BlockUtil.isPositionPlaceable(this.placePos.offset(side1), false, true) == 3) {
                    BlockUtil.placeBlock(this.placePos.offset(side1), EnumHand.MAIN_HAND, ((Boolean) this.rotate.getValue()).booleanValue(), ((Boolean) this.packet.getValue()).booleanValue(), false);
                    ++this.blocksThisTick;
                    return;
                }
            }

            aenumfacing = EnumFacing.values();
            i = aenumfacing.length;

            EnumFacing[] aenumfacing1;
            int k;
            int l;
            EnumFacing side2;

            for (j = 0; j < i; ++j) {
                side1 = aenumfacing[j];
                if (side1 != EnumFacing.DOWN) {
                    aenumfacing1 = EnumFacing.values();
                    k = aenumfacing1.length;

                    for (l = 0; l < k; ++l) {
                        side2 = aenumfacing1[l];
                        if (BlockUtil.isPositionPlaceable(this.placePos.offset(side1).offset(side2), false, true) == 3) {
                            BlockUtil.placeBlock(this.placePos.offset(side1).offset(side2), EnumHand.MAIN_HAND, ((Boolean) this.rotate.getValue()).booleanValue(), ((Boolean) this.packet.getValue()).booleanValue(), false);
                            ++this.blocksThisTick;
                            return;
                        }
                    }
                }
            }

            aenumfacing = EnumFacing.values();
            i = aenumfacing.length;

            for (j = 0; j < i; ++j) {
                side1 = aenumfacing[j];
                aenumfacing1 = EnumFacing.values();
                k = aenumfacing1.length;

                for (l = 0; l < k; ++l) {
                    side2 = aenumfacing1[l];
                    EnumFacing[] aenumfacing2 = EnumFacing.values();
                    int i1 = aenumfacing2.length;

                    for (int j1 = 0; j1 < i1; ++j1) {
                        EnumFacing side3 = aenumfacing2[j1];

                        if (BlockUtil.isPositionPlaceable(this.placePos.offset(side1).offset(side2).offset(side3), false, true) == 3) {
                            BlockUtil.placeBlock(this.placePos.offset(side1).offset(side2).offset(side3), EnumHand.MAIN_HAND, ((Boolean) this.rotate.getValue()).booleanValue(), ((Boolean) this.packet.getValue()).booleanValue(), false);
                            ++this.blocksThisTick;
                            return;
                        }
                    }
                }
            }

        }
    }

    private int findBlockSlot() {
        for (int i = 0; i < 9; ++i) {
            ItemStack item = SelfAnvil.mc.player.inventory.getStackInSlot(i);

            if (item.getItem() instanceof ItemBlock) {
                Block block = Block.getBlockFromItem(SelfAnvil.mc.player.inventory.getStackInSlot(i).getItem());

                if (block instanceof BlockFalling) {
                    return i;
                }
            }
        }

        return -1;
    }

    private boolean doFirstChecks() {
        int canPlace = BlockUtil.isPositionPlaceable(this.placePos, false, true);

        if (!fullNullCheck() && SelfAnvil.mc.world.isAirBlock(this.playerPos)) {
            if (!BlockUtil.isBothHole(this.playerPos) && ((Boolean) this.onlyHole.getValue()).booleanValue()) {
                return false;
            } else if (this.blockSlot == -1) {
                if (((Boolean) this.chat.getValue()).booleanValue()) {
                    Command.sendMessage("<" + this.getDisplayName() + "> §cNo Anvils in hotbar.");
                }

                return false;
            } else {
                if (canPlace == 2) {
                    if (!((Boolean) this.helpingBlocks.getValue()).booleanValue()) {
                        if (((Boolean) this.chat.getValue()).booleanValue()) {
                            Command.sendMessage("<" + this.getDisplayName() + "> §cNowhere to place.");
                        }

                        return false;
                    }

                    if (this.obbySlot == -1) {
                        if (((Boolean) this.chat.getValue()).booleanValue()) {
                            Command.sendMessage("<" + this.getDisplayName() + "> §cNo Obsidian in hotbar.");
                        }

                        return false;
                    }
                } else if (canPlace != 3) {
                    if (((Boolean) this.chat.getValue()).booleanValue()) {
                        Command.sendMessage("<" + this.getDisplayName() + "> §cNot enough room.");
                    }

                    return false;
                }

                return true;
            }
        } else {
            return false;
        }
    }
}
