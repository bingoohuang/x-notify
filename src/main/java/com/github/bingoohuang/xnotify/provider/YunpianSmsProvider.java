package com.github.bingoohuang.xnotify.provider;

import com.github.bingoohuang.utils.lang.Classpath;
import com.github.bingoohuang.xnotify.XNotifySender;
import com.github.bingoohuang.xnotify.XProvider;
import com.github.bingoohuang.xnotify.sender.YunpianSmsSender;
import lombok.val;

public class YunpianSmsProvider implements XProvider {
    @Override public String getProviderName() {
        return "yunpian";
    }

    @Override public XNotifySender getSender() {
        val config = Classpath.loadEnvProperties("yunpian-sms.properties");
        return new YunpianSmsSender(config.getProperty("apikey"));
    }
}
