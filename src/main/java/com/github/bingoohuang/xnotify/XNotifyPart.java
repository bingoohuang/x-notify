package com.github.bingoohuang.xnotify;

public interface XNotifyPart {
    String eval(Object arg);

    default boolean isVar() {
        return false;
    }
}
