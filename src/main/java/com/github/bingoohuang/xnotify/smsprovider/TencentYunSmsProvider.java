package com.github.bingoohuang.xnotify.smsprovider;

import com.github.bingoohuang.xnotify.SmsProvider;
import com.github.bingoohuang.xnotify.SmsSender;
import com.github.bingoohuang.xnotify.smssender.TencentYunSmsSender;
import com.github.bingoohuang.xnotify.util.XNotifyConfig;

public class TencentYunSmsProvider implements SmsProvider {
    @Override public String getProviderName() {
        return "tencent";
    }

    @Override public SmsSender getSmsSender() {
        return new TencentYunSmsSender(XNotifyConfig.get("tencent.appKey"), XNotifyConfig.get("tencent.sdkAppId"));
    }
}
