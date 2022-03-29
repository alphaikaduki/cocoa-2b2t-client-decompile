package me.alpha432.oyvey.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.features.command.Command;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ReloadSoundCommand extends Command {

    public ReloadSoundCommand() {
        super("sound", new String[0]);
    }

    public void execute(String[] commands) {
        try {
            SoundManager e = (SoundManager) ObfuscationReflectionHelper.getPrivateValue(SoundHandler.class, ReloadSoundCommand.mc.getSoundHandler(), new String[] { "sndManager", "sndManager"});

            e.reloadSoundSystem();
            Command.sendMessage(ChatFormatting.GREEN + "Reloaded Sound System.");
        } catch (Exception exception) {
            System.out.println(ChatFormatting.RED + "Could not restart sound manager: " + exception.toString());
            exception.printStackTrace();
            Command.sendMessage(ChatFormatting.RED + "Couldnt Reload Sound System!");
        }

    }
}
