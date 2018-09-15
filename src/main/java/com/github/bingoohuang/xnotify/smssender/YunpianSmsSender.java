package com.github.bingoohuang.xnotify.smssender;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.xnotify.OkHttp;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.Map;

@RequiredArgsConstructor @Slf4j
public class YunpianSmsSender {
    private final String apikey;

    public void send(String mobile, String text) {
        Map<String, String> req = Maps.newHashMap();
        req.put("apikey", apikey);
        req.put("mobile", mobile);
        req.put("text", text);

        val reqJson = JSON.toJSONString(req);
        log.info("send req {}", reqJson);

        val url = "https://sms.yunpian.com/v2/sms/single_send.json";

        val rspJSON = OkHttp.   postForm(url, req);
        log.info("send rsp {}", rspJSON);
        val rsp = JSON.parseObject(rspJSON, Rsp.class);
    }

    // https://www.yunpian.com/doc/zh_CN/domestic/single_send.html

    @Data
    public static class Rsp {
        private int code;   // 0代表发送成功，其他code代表出错，详细见"返回值说明"页面
        private String msg; // 例如""发送成功""，或者相应错误信息
        private int count;  // 发送成功短信的计费条数(计费条数：70个字一条，超出70个字时按每67字一条计费)
        private double fee; // 扣费金额，单位：元，类型：双精度浮点型/double
        private String unit; // 计费单位；例如：“RMB”
        private long sid;   // 短信id，64位整型， 对应Java和C#的long，不可用int解析
    }
}
