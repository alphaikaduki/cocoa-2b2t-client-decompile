package me.alpha432.oyvey.features.modules.player;

import java.awt.Color;
import java.util.function.Predicate;
import me.alpha432.oyvey.event.events.PlayerDamageBlockEvent;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.event.events2.PacketEventM;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MoonBreak extends Module {

    private static MoonBreak INSTANCE = new MoonBreak();
    private Setting creativeMode = this.register(new Setting("CreativeMode", Boolean.valueOf(true)));
    private Setting ghostHand = this.register(new Setting("GhostHand", Boolean.valueOf(true), (v) -> {
        return ((Boolean) this.creativeMode.getValue()).booleanValue();
    }));
    private Setting fastUpdate = this.register(new Setting("FastUpDate", Boolean.valueOf(true), (v) -> {
        return ((Boolean) this.creativeMode.getValue()).booleanValue() && !((Boolean) this.ghostHand.getValue()).booleanValue();
    }));
    private Setting render = this.register(new Setting("Render", Boolean.valueOf(true)));
    private boolean $Cancel = false;
    private BlockPos breakPos;
    private EnumFacing $Facing;

    public MoonBreak() {
        super("BREAK", "MoonBreak", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    public static MoonBreak getInstance() {
        if (MoonBreak.INSTANCE == null) {
            MoonBreak.INSTANCE = new MoonBreak();
        }

        return MoonBreak.INSTANCE;
    }

    private void setInstance() {
        MoonBreak.INSTANCE = this;
    }

    public void onUpdate() {
        if (!fullNullCheck()) {
            if (this.breakPos != null && ((Boolean) this.creativeMode.getValue()).booleanValue() && MoonBreak.mc.world.getBlockState(this.breakPos).getBlock() != Blocks.AIR) {
                if (((Boolean) this.ghostHand.getValue()).booleanValue() && InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) != -1) {
                    int slotMain = MoonBreak.mc.player.inventory.currentItem;

                    MoonBreak.mc.player.inventory.currentItem = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
                    MoonBreak.mc.playerController.updateController();
                    MoonBreak.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, this.breakPos, this.$Facing));
                    MoonBreak.mc.player.inventory.currentItem = slotMain;
                    MoonBreak.mc.playerController.updateController();
                } else {
                    MoonBreak.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, this.breakPos, this.$Facing));
                    if (((Boolean) this.fastUpdate.getValue()).booleanValue()) {
                        MoonBreak.mc.world.setBlockToAir(this.breakPos);
                    }
                }
            }

            MoonBreak.mc.playerController.blockHitDelay = 0;
        }

    }

    public void onRender3D(Render3DEvent event) {
        if (!fullNullCheck() && this.breakPos != null && ((Boolean) this.render.getValue()).booleanValue()) {
            if (MoonBreak.mc.world.getBlockState(this.breakPos).getBlock() != Blocks.AIR) {
                RenderUtil.drawBoxESP(this.breakPos, new Color(255, 0, 0, 255), false, new Color(255, 0, 0, 255), 1.0F, true, true, 84, false);
            } else {
                RenderUtil.drawBoxESP(this.breakPos, new Color(0, 255, 0, 255), false, new Color(0, 255, 0, 255), 1.0F, true, true, 84, false);
            }
        }

    }

    @SubscribeEvent
    public void onPacketSend(PacketEventM.Send event) {
        if (!fullNullCheck() && event.getPacket() instanceof CPacketPlayerDigging) {
            CPacketPlayerDigging packet = (CPacketPlayerDigging) event.getPacket();

            if (packet.getAction() == Action.START_DESTROY_BLOCK && this.$Cancel) {
                event.setCanceled(true);
            }
        }

    }

    @SubscribeEvent
    public void onBlockEvent(PlayerDamageBlockEvent event) {
        if (!fullNullCheck()) {
            if (event.getStage() == 1 && MoonBreak.mc.playerController.curBlockDamageMP > 0.1F) {
                MoonBreak.mc.playerController.isHittingBlock = true;
            }

            if (event.getStage() == 0 && BlockUtil.canBreak(event.pos)) {
                MoonBreak.mc.playerController.isHittingBlock = false;
                if (this.breakPos != null && new BlockPos(event.pos.getX(), event.pos.getY(), event.pos.getZ()) == new BlockPos(this.breakPos.getX(), this.breakPos.getY(), this.breakPos.getZ())) {
                    this.$Cancel = true;
                } else {
                    this.$Cancel = false;
                    MoonBreak.mc.player.swingArm(EnumHand.MAIN_HAND);
                    MoonBreak.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, event.pos, event.facing));
                }

                MoonBreak.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                this.breakPos = event.pos;
                this.$Facing = event.facing;
                event.setCanceled(true);
            }
        }

    }

    public String getDisplayInfo() {
        return ((Boolean) this.ghostHand.getValue()).booleanValue() ? "Ghost" : "Normal";
    }
}
