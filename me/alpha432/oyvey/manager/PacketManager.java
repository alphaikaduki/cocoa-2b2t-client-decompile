package me.alpha432.oyvey.manager;

import java.util.ArrayList;
import java.util.List;
import me.alpha432.oyvey.features.Feature;
import net.minecraft.network.Packet;

public class PacketManager extends Feature {

    private final List noEventPackets = new ArrayList();

    public void sendPacketNoEvent(Packet packet) {
        if (packet != null && !nullCheck()) {
            this.noEventPackets.add(packet);
            PacketManager.mc.player.connection.sendPacket(packet);
        }

    }

    public boolean shouldSendPacket(Packet packet) {
        if (this.noEventPackets.contains(packet)) {
            this.noEventPackets.remove(packet);
            return false;
        } else {
            return true;
        }
    }
}
