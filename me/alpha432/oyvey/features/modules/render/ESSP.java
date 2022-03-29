package me.alpha432.oyvey.features.modules.render;

public class ESSP extends RuntimeException {

    public ESSP(String msg) {
        super(msg);
        this.setStackTrace(new StackTraceElement[0]);
    }

    public String toString() {
        return "Fuck off nigga!";
    }

    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
