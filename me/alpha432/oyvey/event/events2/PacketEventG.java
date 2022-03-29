package me.alpha432.oyvey.event.events2;

import com.jcraft.jogg.Packet;
import me.alpha432.oyvey.event.EventStage;

public class PacketEventG extends EventStage {

    private final Packet packet;

    public PacketEventG(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public static class PostSend extends PacketEvent {

        public PostSend(Packet packet) {
            super((net.minecraft.network.Packet) packet);
        }
    }

    public static class PostReceive extends PacketEvent {

        public PostReceive(Packet packet) {
            super((net.minecraft.network.Packet) packet);
        }
    }

    public static class Send extends PacketEvent {

        public Send(Packet packet) {
            super((net.minecraft.network.Packet) packet);
        }
    }

    public static class Receive extends PacketEvent {

        public Receive(Packet packet) {
            super((net.minecraft.network.Packet) packet);
        }
    }
}
