package com.github.bingoohuang.xnotify.smsprovider;

import com.github.bingoohuang.xnotify.SmsProvider;
import com.github.bingoohuang.xnotify.SmsSender;
import com.github.bingoohuang.xnotify.smssender.AliyunSmsSender;
import com.github.bingoohuang.xnotify.util.XNotifyConfig;

public class AliyunSmsProvider implements SmsProvider {
    @Override public String getProviderName() {
        return "aliyun";
    }

    @Override public SmsSender getSmsSender() {
        return new AliyunSmsSender(XNotifyConfig.get("aliyun.accessKeyId"), XNotifyConfig.get("aliyun.accessSecret"));
    }

}
