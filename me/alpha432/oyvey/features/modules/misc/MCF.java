package me.alpha432.oyvey.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import org.lwjgl.input.Mouse;

public class MCF extends Module {

    private boolean clicked = false;

    public MCF() {
        super("friend", "Middleclick Friends.", Module.Category.MISC, true, false, false);
    }

    public void onUpdate() {
        if (Mouse.isButtonDown(2)) {
            if (!this.clicked && MCF.mc.currentScreen == null) {
                this.onClick();
            }

            this.clicked = true;
        } else {
            this.clicked = false;
        }

    }

    private void onClick() {
        RayTraceResult result = MCF.mc.objectMouseOver;

        if (result != null && result.typeOfHit == Type.ENTITY) {
            Entity entity = result.entityHit;

            if (result.entityHit instanceof EntityPlayer) {
                if (OyVey.friendManager.isFriend(entity.getName())) {
                    OyVey.friendManager.removeFriend(entity.getName());
                    Command.sendMessage(ChatFormatting.RED + entity.getName() + ChatFormatting.RED + " has been unfriended.");
                } else {
                    OyVey.friendManager.addFriend(entity.getName());
                    Command.sendMessage(ChatFormatting.AQUA + entity.getName() + ChatFormatting.AQUA + " has been friended.");
                }
            }
        }

        this.clicked = true;
    }
}
