package me.alpha432.oyvey.event.events;

import java.awt.Event;

public class EventUpdate extends Event {

    public EventUpdate(Object target, long when, int id, int x, int y, int key, int modifiers, Object arg) {
        super(target, when, id, x, y, key, modifiers, arg);
    }
}
