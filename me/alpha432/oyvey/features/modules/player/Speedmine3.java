package me.alpha432.oyvey.features.modules.player;

import java.awt.Color;
import java.util.Iterator;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.BlockEvent;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BlockUtil2;
import me.alpha432.oyvey.util.InventoryUtil2;
import me.alpha432.oyvey.util.MathUtil2;
import me.alpha432.oyvey.util.RenderUtil2;
import me.alpha432.oyvey.util.Timer2;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Speedmine3 extends Module {

    private static Speedmine3 INSTANCE = new Speedmine3();
    public final Timer2 timer = new Timer2();
    public Setting mode;
    public Setting range;
    public Setting tweaks;
    public Setting reset;
    public Setting noDelay;
    public Setting noSwing;
    public Setting allow;
    public Setting noBreakAnim;
    public Setting webswitch;
    public Setting silentSwitch;
    public Setting render1;
    public Setting damage;
    public Setting webSwitch;
    public Setting doubleBreak;
    public Setting render;
    private final Setting red;
    private final Setting green;
    private final Setting blue;
    public Setting box;
    private final Setting boxAlpha;
    public Setting outline;
    private final Setting lineWidth;
    public BlockPos currentPos;
    public IBlockState currentBlockState;
    public float breakTime;
    private boolean isMining;
    private BlockPos lastPos;
    private EnumFacing lastFacing;

    public Speedmine3() {
        super("Speedmine3", "Speeds up mining.", Module.Category.PLAYER, true, false, false);
        this.mode = this.register(new Setting("Mode", Speedmine.Mode.PACKET));
        this.range = this.register(new Setting("Range", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(7.0F)));
        this.tweaks = this.register(new Setting("Speed", "Tweaks", Double.valueOf(0.0D), Double.valueOf(0.0D)));
        this.reset = this.register(new Setting("Speed", "Reset", Double.valueOf(0.0D), Double.valueOf(0.0D)));
        this.noDelay = this.register(new Setting("Speed", "NoDelay", Double.valueOf(0.0D), Double.valueOf(0.0D)));
        this.noSwing = this.register(new Setting("Speed", "NoSwing", Double.valueOf(0.0D), Double.valueOf(0.0D)));
        this.allow = this.register(new Setting("Speed", "AllowMultiTask", Double.valueOf(0.0D), Double.valueOf(0.0D)));
        this.noBreakAnim = this.register(new Setting("Speed", "NoBreakAnim", Double.valueOf(0.0D), Double.valueOf(0.0D)));
        this.webswitch = this.register(new Setting("Speed", "WebSwitch", Double.valueOf(0.0D), Double.valueOf(0.0D)));
        this.silentSwitch = this.register(new Setting("Speed", "SilentSwitch", Double.valueOf(0.0D), Double.valueOf(0.0D)));
        this.render1 = this.register(new Setting("Speed", "Render1", Double.valueOf(0.0D), Double.valueOf(0.0D)));
        this.damage = this.register(new Setting("Damage", Float.valueOf(0.7F), Float.valueOf(0.0F), Float.valueOf(1.0F), test<invokedynamic>(this)));
        this.webSwitch = this.register(new Setting("WebSwitch", Boolean.valueOf(false)));
        this.doubleBreak = this.register(new Setting("DoubleBreak", Boolean.valueOf(false)));
        this.render = this.register(new Setting("Render", Boolean.valueOf(false)));
        this.red = this.register(new Setting("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
        this.green = this.register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
        this.blue = this.register(new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
        this.box = this.register(new Setting("Box", Boolean.valueOf(false), test<invokedynamic>(this)));
        this.boxAlpha = this.register(new Setting("BoxAlpha", Integer.valueOf(85), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this)));
        this.outline = this.register(new Setting("Outline", Boolean.valueOf(true), test<invokedynamic>(this)));
        this.lineWidth = this.register(new Setting("LineWidth", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(5.0F), test<invokedynamic>(this)));
        this.breakTime = -1.0F;
        this.setInstance();
    }

    public static Speedmine3 getInstance() {
        if (Speedmine3.INSTANCE == null) {
            Speedmine3.INSTANCE = new Speedmine3();
        }

        return Speedmine3.INSTANCE;
    }

    private void setInstance() {
        Speedmine3.INSTANCE = this;
    }

    public void onTick() {
        if (this.currentPos != null) {
            if (Speedmine3.mc.player != null && Speedmine3.mc.player.getDistanceSq(this.currentPos) > MathUtil2.square(((Float) this.range.getValue()).floatValue())) {
                this.currentPos = null;
                this.currentBlockState = null;
                return;
            }

            if (Speedmine.mc.player != null && ((Boolean) this.silentSwitch.getValue()).booleanValue() && this.timer.passedMs((long) ((int) (2000.0F * OyVey.serverManager.getTpsFactor()))) && this.getPickSlot() != -1) {
                Speedmine.mc.player.connection.sendPacket(new CPacketHeldItemChange(this.getPickSlot()));
            }

            if (Speedmine.mc.player != null && ((Boolean) this.silentSwitch.getValue()).booleanValue() && this.timer.passedMs((long) ((int) (2200.0F * OyVey.serverManager.getTpsFactor())))) {
                int oldSlot = Speedmine3.mc.player.inventory.currentItem;

                Speedmine.mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
            }

            if (fullNullCheck()) {
                return;
            }

            if (Speedmine.mc.world.getBlockState(this.currentPos).equals(this.currentBlockState) && Speedmine.mc.world.getBlockState(this.currentPos).getBlock() != Blocks.AIR) {
                if (((Boolean) this.webSwitch.getValue()).booleanValue() && this.currentBlockState.getBlock() == Blocks.WEB && Speedmine.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
                    InventoryUtil2.switchToHotbarSlot(ItemSword.class, false);
                }
            } else {
                this.currentPos = null;
                this.currentBlockState = null;
            }
        }

    }

    public void onUpdate() {
        if (!Speedmine.fullNullCheck()) {
            if (((Boolean) this.noDelay.getValue()).booleanValue()) {
                Speedmine.mc.playerController.blockHitDelay = 0;
            }

            if (this.isMining && this.lastPos != null && this.lastFacing != null && ((Boolean) this.noBreakAnim.getValue()).booleanValue()) {
                Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.ABORT_DESTROY_BLOCK, this.lastPos, this.lastFacing));
            }

            if (((Boolean) this.reset.getValue()).booleanValue() && Speedmine.mc.gameSettings.keyBindUseItem.isKeyDown() && !((Boolean) this.allow.getValue()).booleanValue()) {
                Speedmine.mc.playerController.isHittingBlock = false;
            }

        }
    }

    public void onRender3D(Render3DEvent render3DEvent) {
        if (((Boolean) this.render.getValue()).booleanValue() && this.currentPos != null) {
            Color color = new Color(((Integer) this.red.getValue()).intValue(), ((Integer) this.green.getValue()).intValue(), ((Integer) this.blue.getValue()).intValue(), ((Integer) this.boxAlpha.getValue()).intValue());

            RenderUtil2.boxESP(this.currentPos, color, ((Float) this.lineWidth.getValue()).floatValue(), ((Boolean) this.outline.getValue()).booleanValue(), ((Boolean) this.box.getValue()).booleanValue(), ((Integer) this.boxAlpha.getValue()).intValue(), true);
        }

    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (!Speedmine.fullNullCheck()) {
            if (event.getStage() == 0) {
                if (((Boolean) this.noSwing.getValue()).booleanValue() && event.getPacket() instanceof CPacketAnimation) {
                    event.setCanceled(true);
                }

                CPacketPlayerDigging packet;

                if (((Boolean) this.noBreakAnim.getValue()).booleanValue() && event.getPacket() instanceof CPacketPlayerDigging && (packet = (CPacketPlayerDigging) event.getPacket()) != null) {
                    packet.getPosition();

                    try {
                        Iterator iterator = Speedmine.mc.world.getEntitiesWithinAABBExcludingEntity((Entity) null, new AxisAlignedBB(packet.getPosition())).iterator();

                        while (iterator.hasNext()) {
                            Entity entity = (Entity) iterator.next();

                            if (entity instanceof EntityEnderCrystal) {
                                this.showAnimation();
                                return;
                            }
                        }
                    } catch (Exception exception) {
                        ;
                    }

                    if (packet.getAction().equals(Action.START_DESTROY_BLOCK)) {
                        this.showAnimation(true, packet.getPosition(), packet.getFacing());
                    }

                    if (packet.getAction().equals(Action.STOP_DESTROY_BLOCK)) {
                        this.showAnimation();
                    }
                }
            }

        }
    }

    @SubscribeEvent
    public void onBlockEvent(BlockEvent event) {
        if (!Speedmine.fullNullCheck()) {
            if (event.getStage() == 3 && Speedmine.mc.world.getBlockState(event.pos).getBlock() instanceof BlockEndPortalFrame) {
                Speedmine.mc.world.getBlockState(event.pos).getBlock().setHardness(50.0F);
            }

            if (event.getStage() == 3 && ((Boolean) this.reset.getValue()).booleanValue() && Speedmine.mc.playerController.curBlockDamageMP > 0.1F) {
                Speedmine.mc.playerController.isHittingBlock = true;
            }

            if (event.getStage() == 4 && ((Boolean) this.tweaks.getValue()).booleanValue()) {
                if (BlockUtil2.canBreak(event.pos)) {
                    if (((Boolean) this.reset.getValue()).booleanValue()) {
                        Speedmine.mc.playerController.isHittingBlock = false;
                    }

                    switch ((Speedmine.Mode) this.mode.getValue()) {
                    case PACKET:
                        if (this.currentPos == null) {
                            this.currentPos = event.pos;
                            this.currentBlockState = Speedmine.mc.world.getBlockState(this.currentPos);
                            ItemStack object = new ItemStack(Items.DIAMOND_PICKAXE);

                            this.breakTime = object.getDestroySpeed(this.currentBlockState) / 3.71F;
                            this.timer.reset();
                        }

                        Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, event.pos, event.facing));
                        Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                        event.setCanceled(true);
                        break;

                    case DAMAGE:
                        if (Speedmine.mc.playerController.curBlockDamageMP >= ((Float) this.damage.getValue()).floatValue()) {
                            Speedmine.mc.playerController.curBlockDamageMP = 1.0F;
                        }
                        break;

                    case INSTANT:
                        Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, event.pos, event.facing));
                        Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                        Speedmine.mc.playerController.onPlayerDestroyBlock(event.pos);
                        Speedmine.mc.world.setBlockToAir(event.pos);
                    }
                }

                BlockPos above;

                if (((Boolean) this.doubleBreak.getValue()).booleanValue() && BlockUtil2.canBreak(above = event.pos.add(0, 1, 0)) && Speedmine.mc.player.getDistance((double) above.getX(), (double) above.getY(), (double) above.getZ()) <= 5.0D) {
                    Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                    Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, above, event.facing));
                    Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, above, event.facing));
                    Speedmine.mc.playerController.onPlayerDestroyBlock(above);
                    Speedmine.mc.world.setBlockToAir(above);
                }
            }

        }
    }

    private int getPickSlot() {
        for (int i = 0; i < 9; ++i) {
            if (Speedmine.mc.player.inventory.getStackInSlot(i).getItem() == Items.DIAMOND_PICKAXE) {
                return i;
            }
        }

        return -1;
    }

    private void showAnimation(boolean isMining, BlockPos lastPos, EnumFacing lastFacing) {
        this.isMining = isMining;
        this.lastPos = lastPos;
        this.lastFacing = lastFacing;
    }

    public void showAnimation() {
        this.showAnimation(false, (BlockPos) null, (EnumFacing) null);
    }

    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    private boolean lambda$new$4(Object v) {
        return ((Boolean) this.outline.getValue()).booleanValue() && ((Boolean) this.render.getValue()).booleanValue();
    }

    private boolean lambda$new$3(Object v) {
        return ((Boolean) this.render.getValue()).booleanValue();
    }

    private boolean lambda$new$2(Object v) {
        return ((Boolean) this.box.getValue()).booleanValue() && ((Boolean) this.render.getValue()).booleanValue();
    }

    private boolean lambda$new$1(Object v) {
        return ((Boolean) this.render.getValue()).booleanValue();
    }

    private boolean lambda$new$0(Object v) {
        return this.mode.getValue() == Speedmine.Mode.DAMAGE;
    }

    public static enum Mode {

        PACKET, DAMAGE, INSTANT;
    }
}
