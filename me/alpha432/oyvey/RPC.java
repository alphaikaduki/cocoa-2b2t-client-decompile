package me.alpha432.oyvey;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class RPC {

    private static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static final DiscordRPC discordRPC = DiscordRPC.INSTANCE;

    public static void startRPC() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();

        eventHandlers.disconnected = (var1, var2) -> {
            System.out.println("Discord RPC disconnected, var1: " + i + ", var2: " + s);
        };
        String discordID = "926165595144196117";

        RPC.discordRPC.Discord_Initialize(discordID, eventHandlers, true, (String) null);
        RPC.discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        RPC.discordRichPresence.details = "á´?Ê?á´?á´˜á´? á´?ÊŸÉªá´?É´á´? Buy here https://discord.gg/YZ9ghVWw";
        RPC.discordRichPresence.largeImageKey = "main";
        RPC.discordRichPresence.largeImageText = "";
        RPC.discordRichPresence.largeImageText = "https://discord.gg/YZ9ghVWw";
        RPC.discordRichPresence.smallImageText = "discord server https://discord.gg/YZ9ghVWw";
        RPC.discordRichPresence.state = null;
        RPC.discordRPC.Discord_UpdatePresence(RPC.discordRichPresence);
    }

    public static void stopRPC() {
        RPC.discordRPC.Discord_Shutdown();
        RPC.discordRPC.Discord_ClearPresence();
    }
}
