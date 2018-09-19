package com.github.bingoohuang.xnotify.provider;

import com.github.bingoohuang.xnotify.XNotifySender;
import com.github.bingoohuang.xnotify.XProvider;
import com.github.bingoohuang.xnotify.sender.AliyunSmsSender;
import com.github.bingoohuang.xnotify.util.XNotifyConfig;

public class AliyunSmsProvider implements XProvider {
    @Override public String getProviderName() {
        return "aliyun";
    }

    @Override public XNotifySender getSender() {
        return new AliyunSmsSender(XNotifyConfig.get("aliyun.accessKeyId"), XNotifyConfig.get("aliyun.accessSecret"));
    }

}
