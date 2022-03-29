package me.alpha432.oyvey.event.events2;

import com.jcraft.jogg.Packet;
import me.alpha432.oyvey.event.EventStage;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class PacketEventM extends EventStage {

    private final Packet packet;

    public PacketEventM(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    @Cancelable
    public static class Send extends PacketEvent {

        public Send(Packet packet) {
            super((net.minecraft.network.Packet) packet);
        }
    }

    @Cancelable
    public static class Receive extends PacketEvent {

        public Receive(Packet packet) {
            super((net.minecraft.network.Packet) packet);
        }
    }
}
