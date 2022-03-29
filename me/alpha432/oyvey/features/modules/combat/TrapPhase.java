package me.alpha432.oyvey.features.modules.combat;

import java.util.function.Predicate;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import me.alpha432.oyvey.util.TimerK;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class TrapPhase extends Module {

    private int stage = 0;
    public TimerK delayTimer;
    private Setting isJump = this.register(new Setting("Jump", Boolean.valueOf(false)));
    private Setting jumpOffset = this.register(new Setting("JumpOffset", Double.valueOf(0.3D), Double.valueOf(0.0D), Double.valueOf(10.0D), (v) -> {
        return !((Boolean) this.isJump.getValue()).booleanValue();
    }));
    private Setting delay = this.register(new Setting("Delay", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(50)));
    private Setting toggle = this.register(new Setting("Toggle", Boolean.valueOf(true)));

    public TrapPhase() {
        super("TrapPhase", "", Module.Category.COMBAT, true, false, false);
    }

    public void onEnable() {
        this.stage = 0;
        this.delayTimer = new TimerK();
    }

    public void onTick() {
        if (this.delayTimer == null) {
            this.delayTimer = new TimerK();
        }

        int oldSlot = TrapPhase.mc.player.inventory.currentItem;
        int trapSlot = InventoryUtil.findHotbarBlock(Blocks.IRON_TRAPDOOR);

        if (trapSlot != -1) {
            switch (this.stage) {
            case 0:
                if (!((Boolean) this.isJump.getValue()).booleanValue()) {
                    TrapPhase.mc.player.setPosition(TrapPhase.mc.player.posX, Math.floor(TrapPhase.mc.player.posY) + ((Double) this.jumpOffset.getValue()).doubleValue(), TrapPhase.mc.player.posZ);
                } else {
                    TrapPhase.mc.player.jump();
                }

                this.stage = 1;
                TrapPhase.mc.playerController.updateController();
                break;

            case 1:
                if (this.delayTimer.passedD((double) ((Integer) this.delay.getValue()).intValue())) {
                    this.delayTimer.reset();
                    TrapPhase.mc.player.connection.sendPacket(new CPacketEntityAction(TrapPhase.mc.player, Action.STOP_SNEAKING));
                    TrapPhase.mc.player.setSneaking(false);
                    TrapPhase.mc.getConnection().sendPacket(new CPacketHeldItemChange(trapSlot));
                    BlockUtil.placeTrapdoor(new BlockPos(TrapPhase.mc.player.posX, TrapPhase.mc.player.posY - 1.0D, TrapPhase.mc.player.posZ), EnumHand.MAIN_HAND, false, true, false);
                    BlockUtil.placeTrapdoor(new BlockPos(TrapPhase.mc.player.posX, TrapPhase.mc.player.posY, TrapPhase.mc.player.posZ), EnumHand.MAIN_HAND, false, true, false);
                    TrapPhase.mc.getConnection().sendPacket(new CPacketHeldItemChange(oldSlot));
                    TrapPhase.mc.player.setPosition(TrapPhase.mc.player.posX, Math.floor(TrapPhase.mc.player.posY), TrapPhase.mc.player.posZ);
                    TrapPhase.mc.player.motionY = -10.0D;
                    this.stage = 2;
                    if (((Boolean) this.toggle.getValue()).booleanValue()) {
                        this.disable();
                        this.stage = 0;
                    }
                }
            }

            if (this.stage == 2 && ((Boolean) this.toggle.getValue()).booleanValue()) {
                this.disable();
                this.stage = 0;
            }
        } else {
            TrapPhase.mc.player.sendMessage(new Command.ChatMessage("TRAP持ってねえじ�?んw"));
            this.disable();
        }

    }
}
