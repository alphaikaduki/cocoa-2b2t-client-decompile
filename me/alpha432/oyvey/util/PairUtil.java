package me.alpha432.oyvey.util;

public class PairUtil {

    private Object first;
    private Object second;

    public PairUtil(Object f, Object s) {
        this.first = f;
        this.second = s;
    }

    public Object getFirst() {
        return this.first;
    }

    public void setFirst(Object f) {
        this.first = f;
    }

    public Object getSecond() {
        return this.second;
    }

    public void setSecond(Object s) {
        this.second = s;
    }
}
