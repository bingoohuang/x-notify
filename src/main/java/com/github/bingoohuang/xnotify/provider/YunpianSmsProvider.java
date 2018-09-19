package com.github.bingoohuang.xnotify.provider;

import com.github.bingoohuang.xnotify.XNotifySender;
import com.github.bingoohuang.xnotify.XProvider;
import com.github.bingoohuang.xnotify.sender.YunpianSmsSender;
import com.github.bingoohuang.xnotify.util.XNotifyConfig;

public class YunpianSmsProvider implements XProvider {
    @Override public String getProviderName() {
        return "yunpian";
    }

    @Override public XNotifySender getSender() {
        return new YunpianSmsSender(XNotifyConfig.get("yunpian.apikey"));
    }
}
