package me.alpha432.oyvey.event;

public class EventStage extends net.minecraftforge.fml.common.eventhandler.Event {

    private int stage;

    public EventStage() {}

    public EventStage(int stage) {
        this.stage = stage;
    }

    public int getStage() {
        return this.stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
}
