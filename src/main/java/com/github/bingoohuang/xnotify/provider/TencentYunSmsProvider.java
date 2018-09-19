package com.github.bingoohuang.xnotify.provider;

import com.github.bingoohuang.xnotify.XNotifySender;
import com.github.bingoohuang.xnotify.XProvider;
import com.github.bingoohuang.xnotify.sender.TencentYunSmsSender;
import com.github.bingoohuang.xnotify.util.XNotifyConfig;

public class TencentYunSmsProvider implements XProvider {
    @Override public String getProviderName() {
        return "tencent";
    }

    @Override public XNotifySender getSender() {
        return new TencentYunSmsSender(XNotifyConfig.get("tencent.appKey"), XNotifyConfig.get("tencent.sdkAppId"));
    }
}
