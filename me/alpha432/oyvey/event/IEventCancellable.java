package me.alpha432.oyvey.event;

public interface IEventCancellable extends IEvent {

    void setCancelled();

    void setCancelled(boolean flag);

    boolean isCancelled();
}
