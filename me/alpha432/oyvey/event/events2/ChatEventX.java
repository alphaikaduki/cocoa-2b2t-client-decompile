package me.alpha432.oyvey.event.events2;

import me.alpha432.oyvey.event.EventStage;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
class ChatEventX extends EventStage {

    private final String msg;

    public ChatEventX(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }
}
