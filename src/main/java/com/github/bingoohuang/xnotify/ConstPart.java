package com.github.bingoohuang.xnotify;

public class ConstPart implements XNotifyPart {
    private final String constValue;

    public ConstPart(String constValue) {
        this.constValue = constValue;
    }

    @Override public String eval(Object arg) {
        return constValue;
    }
}
