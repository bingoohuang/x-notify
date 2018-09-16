package com.github.bingoohuang.xnotify;

import com.github.bingoohuang.xnotify.impl.SmsLog;

import java.util.Map;

public interface SmsSender {
    /**
     * 发送短信。
     *
     * @param mobile       手机号码
     * @param signName     签名
     * @param templateCode 模板编码
     * @param params       模板参数
     * @param text         消息内容
     */
    SmsLog send(String mobile, String signName, String templateCode, Map<String, String> params, String text);
}
