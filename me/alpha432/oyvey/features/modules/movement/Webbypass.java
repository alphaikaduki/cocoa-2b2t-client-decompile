package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.BlockCollisionBoundingBoxEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class Webbypass extends Module {

    public Setting disableBB = this.register(new Setting("AddBB", Boolean.valueOf(true)));
    public Setting bbOffset = this.register(new Setting("BBOffset", Float.valueOf(0.4F), Float.valueOf(-2.0F), Float.valueOf(2.0F)));
    public Setting onGround = this.register(new Setting("On Ground", Boolean.valueOf(true)));
    public Setting motionY = this.register(new Setting("Set MotionY", Float.valueOf(0.0F), Float.valueOf(0.0F), Float.valueOf(20.0F)));
    public Setting motionX = this.register(new Setting("Set MotionX", Float.valueOf(0.8F), Float.valueOf(-1.0F), Float.valueOf(5.0F)));

    public Webbypass() {
        super("Webbypass", "aw", Module.Category.MOVEMENT, true, false, false);
    }

    @SubscribeEvent
    public void bbEvent(BlockCollisionBoundingBoxEvent event) {
        if (!nullCheck()) {
            if (Webbypass.mc.world.getBlockState(event.getPos()).getBlock() instanceof BlockWeb && ((Boolean) this.disableBB.getValue()).booleanValue()) {
                event.setBoundingBox(Block.FULL_BLOCK_AABB.contract(0.0D, (double) ((Float) this.bbOffset.getValue()).floatValue(), 0.0D));
            }

        }
    }

    public void onUpdate() {
        if (Webbypass.mc.player.isInWeb && !OyVey.moduleManager.isModuleEnabled("Step") || Webbypass.mc.player.isInWeb && !OyVey.moduleManager.isModuleEnabled("StepTwo")) {
            if (Keyboard.isKeyDown(Webbypass.mc.gameSettings.keyBindSneak.keyCode)) {
                Webbypass.mc.player.isInWeb = true;
                Webbypass.mc.player.motionY *= (double) ((Float) this.motionY.getValue()).floatValue();
            } else if (((Boolean) this.onGround.getValue()).booleanValue()) {
                Webbypass.mc.player.onGround = false;
            }

            if (Keyboard.isKeyDown(Webbypass.mc.gameSettings.keyBindForward.keyCode) || Keyboard.isKeyDown(Webbypass.mc.gameSettings.keyBindBack.keyCode) || Keyboard.isKeyDown(Webbypass.mc.gameSettings.keyBindLeft.keyCode) || Keyboard.isKeyDown(Webbypass.mc.gameSettings.keyBindRight.keyCode)) {
                Webbypass.mc.player.isInWeb = false;
                Webbypass.mc.player.motionX *= (double) ((Float) this.motionX.getValue()).floatValue();
                Webbypass.mc.player.motionZ *= (double) ((Float) this.motionX.getValue()).floatValue();
            }
        }

    }
}
