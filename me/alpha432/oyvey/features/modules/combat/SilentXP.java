package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Bind;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class SilentXP extends Module {

    public Setting mode;
    public Setting antiFriend;
    public Setting key;
    public Setting groundOnly;
    private boolean last;
    private boolean on;

    public SilentXP() {
        super("SilentXP", "Silent XP", Module.Category.PLAYER, false, false, false);
        this.mode = this.register(new Setting("Mode", SilentXP.Mode.MIDDLECLICK));
        this.antiFriend = this.register(new Setting("AntiFriend", Boolean.valueOf(true)));
        this.key = this.register(new Setting("Key", new Bind(-1), test<invokedynamic>(this)));
        this.groundOnly = this.register(new Setting("BelowHorizon", Boolean.valueOf(false)));
    }

    public void onUpdate() {
        if (!fullNullCheck()) {
            switch ((SilentXP.Mode) this.mode.getValue()) {
            case PRESS:
                if (((Bind) this.key.getValue()).isDown()) {
                    this.throwXP(false);
                }
                break;

            case TOGGLE:
                if (this.toggled()) {
                    this.throwXP(false);
                }
                break;

            default:
                if (((Boolean) this.groundOnly.getValue()).booleanValue() && SilentXP.mc.player.rotationPitch < 0.0F) {
                    return;
                }

                if (Mouse.isButtonDown(2)) {
                    this.throwXP(true);
                }
            }

        }
    }

    private boolean toggled() {
        if (((Bind) this.key.getValue()).getKey() == -1) {
            return false;
        } else {
            if (!Keyboard.isKeyDown(((Bind) this.key.getValue()).getKey())) {
                this.last = true;
            } else {
                if (Keyboard.isKeyDown(((Bind) this.key.getValue()).getKey()) && this.last && !this.on) {
                    this.last = false;
                    this.on = true;
                    return this.on;
                }

                if (Keyboard.isKeyDown(((Bind) this.key.getValue()).getKey()) && this.last && this.on) {
                    this.last = false;
                    this.on = false;
                    return this.on;
                }
            }

            return this.on;
        }
    }

    private void throwXP(boolean mcf) {
        if (mcf && ((Boolean) this.antiFriend.getValue()).booleanValue()) {
            RayTraceResult result = SilentXP.mc.objectMouseOver;

            if (SilentXP.mc.objectMouseOver != null && result.typeOfHit == Type.ENTITY && result.entityHit instanceof EntityPlayer) {
                return;
            }
        }

        int xpSlot = InventoryUtil.findHotbarBlock(ItemExpBottle.class);
        boolean offhand = SilentXP.mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE;

        if (xpSlot != -1 || offhand) {
            int oldslot = SilentXP.mc.player.inventory.currentItem;

            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(xpSlot, false);
            }

            SilentXP.mc.playerController.processRightClick(SilentXP.mc.player, SilentXP.mc.world, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(oldslot, false);
            }
        }

    }

    private boolean lambda$new$0(Bind v) {
        return this.mode.getValue() != SilentXP.Mode.MIDDLECLICK;
    }

    public static enum Mode {

        MIDDLECLICK, TOGGLE, PRESS;
    }
}
