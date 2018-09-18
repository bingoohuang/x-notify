package com.github.bingoohuang.xnotify;

public interface SmsProvider extends XProvider{
    String getProviderName();

    SmsSender getSmsSender();
}
