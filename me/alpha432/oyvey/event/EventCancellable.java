package me.alpha432.oyvey.event;

public abstract class EventCancellable extends Event implements IEventCancellable {

    private boolean isCancelled = false;

    public void setCancelled() {
        this.isCancelled = true;
    }

    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public abstract void setCanceled(boolean flag);
}
