package com.github.bingoohuang.xnotify.provider;

import com.github.bingoohuang.utils.lang.Classpath;
import com.github.bingoohuang.xnotify.XNotifySender;
import com.github.bingoohuang.xnotify.XProvider;
import com.github.bingoohuang.xnotify.sender.AliyunSmsSender;
import lombok.val;

public class AliyunSmsProvider implements XProvider {
    @Override public String getProviderName() {
        return "aliyun";
    }

    @Override public XNotifySender getSender() {
        val config = Classpath.loadEnvProperties("aliyun-sms.properties");
        return new AliyunSmsSender(config.getProperty("accessKeyId"), config.getProperty("accessSecret"));
    }
}
