package com.github.bingoohuang.xnotify;

public interface XProvider {
    String getProviderName();

    XNotifySender getSender();
}
