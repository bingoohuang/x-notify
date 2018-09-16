package com.github.bingoohuang.xnotify;

public interface SmsProvider {
    String getProviderName();

    SmsSender getSmsSender();
}
