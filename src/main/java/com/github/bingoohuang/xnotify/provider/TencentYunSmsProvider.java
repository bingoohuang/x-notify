package com.github.bingoohuang.xnotify.provider;

import com.github.bingoohuang.utils.lang.Classpath;
import com.github.bingoohuang.xnotify.XNotifySender;
import com.github.bingoohuang.xnotify.XProvider;
import com.github.bingoohuang.xnotify.sender.TencentYunSmsSender;
import lombok.val;

public class TencentYunSmsProvider implements XProvider {
    @Override public String getProviderName() {
        return "tencent";
    }

    @Override public XNotifySender getSender() {
        val config = Classpath.loadEnvProperties("tencent-sms.properties");
        return new TencentYunSmsSender(config.getProperty("appKey"), config.getProperty("sdkAppId"));
    }
}
