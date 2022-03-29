package me.alpha432.oyvey.features.modules.player;

import com.mojang.authlib.GameProfile;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.UUID;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.PlayerLivingUpdateEvent;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.DamageUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FakePlayer extends Module {

    private static FakePlayer INSTANCE = new FakePlayer();
    private final Setting pops = this.register(new Setting("TotemPops", Boolean.valueOf(true)));
    private final Setting totemPopParticle = this.register(new Setting("TotemPopParticle", Boolean.valueOf(true)));
    private final Setting totemPopSound = this.register(new Setting("TotemPopSound", Boolean.valueOf(true)));
    public Setting move = this.register(new Setting("Move", Boolean.valueOf(true)));
    public Setting type;
    public Setting chaseX;
    public Setting chaseY;
    public Setting chaseZ;
    public EntityOtherPlayerMP fakePlayer;

    public FakePlayer() {
        super("FakePlayer", "Spawns a FakePlayer for testing.", Module.Category.PLAYER, true, false, false);
        this.type = this.register(new Setting("MovementMode", FakePlayer.Type.STATIC, test<invokedynamic>(this)));
        this.chaseX = this.register(new Setting("ChaseX", Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(120), test<invokedynamic>(this)));
        this.chaseY = this.register(new Setting("ChaseY", Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(120), test<invokedynamic>(this)));
        this.chaseZ = this.register(new Setting("ChaseZ", Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(120), test<invokedynamic>(this)));
    }

    public static FakePlayer getInstance() {
        if (FakePlayer.INSTANCE == null) {
            FakePlayer.INSTANCE = new FakePlayer();
        }

        return FakePlayer.INSTANCE;
    }

    public void onEnable() {
        if (FakePlayer.mc.world != null && FakePlayer.mc.player != null) {
            UUID playerUUID = FakePlayer.mc.player.getUniqueID();

            this.fakePlayer = new EntityOtherPlayerMP(FakePlayer.mc.world, new GameProfile(UUID.fromString(playerUUID.toString()), FakePlayer.mc.player.getDisplayNameString()));
            this.fakePlayer.copyLocationAndAnglesFrom(FakePlayer.mc.player);
            this.fakePlayer.inventory.copyInventory(FakePlayer.mc.player.inventory);
            FakePlayer.mc.world.addEntityToWorld(-7777, this.fakePlayer);
            Command.sendMessage(ChatFormatting.GREEN + "crepe :)");
        } else {
            this.disable();
        }

    }

    @SubscribeEvent
    public void onTick(PlayerLivingUpdateEvent event) {
        if (((Boolean) this.pops.getValue()).booleanValue()) {
            if (this.fakePlayer != null) {
                this.fakePlayer.inventory.offHandInventory.set(0, new ItemStack(Items.TOTEM_OF_UNDYING));
                if (this.fakePlayer.getHealth() <= 0.0F) {
                    this.fakePop(this.fakePlayer);
                    this.fakePlayer.setHealth(20.0F);
                }
            }

            this.travel(this.fakePlayer.moveStrafing, this.fakePlayer.moveVertical, this.fakePlayer.moveForward);
        }

    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (((FakePlayer.Type) this.type.getValue()).equals(FakePlayer.Type.CHASE)) {
            this.fakePlayer.posX = FakePlayer.mc.player.posX + (double) ((Integer) this.chaseX.getValue()).intValue();
            this.fakePlayer.posY = (double) ((Integer) this.chaseY.getValue()).intValue();
            this.fakePlayer.posZ = FakePlayer.mc.player.posZ + (double) ((Integer) this.chaseZ.getValue()).intValue();
        }

    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (this.fakePlayer != null) {
            if (event.getPacket() instanceof SPacketExplosion) {
                SPacketExplosion explosion = (SPacketExplosion) event.getPacket();

                if (this.fakePlayer.getDistance(explosion.getX(), explosion.getY(), explosion.getZ()) <= 15.0D) {
                    double damage = (double) DamageUtil.calculateDamage(explosion.getX(), explosion.getY(), explosion.getZ(), this.fakePlayer);

                    if (damage > 0.0D && ((Boolean) this.pops.getValue()).booleanValue()) {
                        this.fakePlayer.setHealth((float) ((double) this.fakePlayer.getHealth() - MathHelper.clamp(damage, 0.0D, 999.0D)));
                    }
                }
            }

        }
    }

    public void travel(float strafe, float vertical, float forward) {
        double d0 = this.fakePlayer.posY;
        float f1 = 0.8F;
        float f2 = 0.02F;
        float f3 = (float) EnchantmentHelper.getDepthStriderModifier(this.fakePlayer);

        if (f3 > 3.0F) {
            f3 = 3.0F;
        }

        if (!this.fakePlayer.onGround) {
            f3 *= 0.5F;
        }

        if (f3 > 0.0F) {
            f1 += (0.54600006F - f1) * f3 / 3.0F;
            f2 += (this.fakePlayer.getAIMoveSpeed() - f2) * f3 / 4.0F;
        }

        this.fakePlayer.moveRelative(strafe, vertical, forward, f2);
        this.fakePlayer.move(MoverType.SELF, this.fakePlayer.motionX, this.fakePlayer.motionY, this.fakePlayer.motionZ);
        this.fakePlayer.motionX *= (double) f1;
        this.fakePlayer.motionY *= 0.800000011920929D;
        this.fakePlayer.motionZ *= (double) f1;
        if (!this.fakePlayer.hasNoGravity()) {
            this.fakePlayer.motionY -= 0.02D;
        }

        if (this.fakePlayer.collidedHorizontally && this.fakePlayer.isOffsetPositionInLiquid(this.fakePlayer.motionX, this.fakePlayer.motionY + 0.6000000238418579D - this.fakePlayer.posY + d0, this.fakePlayer.motionZ)) {
            this.fakePlayer.motionY = 0.30000001192092896D;
        }

    }

    public void onDisable() {
        if (this.fakePlayer != null && FakePlayer.mc.world != null) {
            FakePlayer.mc.world.removeEntityFromWorld(-7777);
            Command.sendMessage(ChatFormatting.GREEN + "crepe ;;");
            this.fakePlayer = null;
        }

    }

    private void fakePop(Entity entity) {
        if (((Boolean) this.totemPopParticle.getValue()).booleanValue()) {
            FakePlayer.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.TOTEM, 30);
        }

        if (((Boolean) this.totemPopSound.getValue()).booleanValue()) {
            FakePlayer.mc.world.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0F, 1.0F, false);
        }

    }

    private boolean lambda$new$3(Object v) {
        return ((Boolean) this.move.getValue()).booleanValue() && this.type.getValue() == FakePlayer.Type.CHASE;
    }

    private boolean lambda$new$2(Object v) {
        return ((Boolean) this.move.getValue()).booleanValue() && this.type.getValue() == FakePlayer.Type.CHASE;
    }

    private boolean lambda$new$1(Object v) {
        return ((Boolean) this.move.getValue()).booleanValue() && this.type.getValue() == FakePlayer.Type.CHASE;
    }

    private boolean lambda$new$0(FakePlayer.Type v) {
        return ((Boolean) this.move.getValue()).booleanValue();
    }

    public static enum Type {

        CHASE, STATIC;
    }
}
