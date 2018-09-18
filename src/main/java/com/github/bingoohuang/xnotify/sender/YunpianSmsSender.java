package com.github.bingoohuang.xnotify.sender;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.westid.WestId;
import com.github.bingoohuang.xnotify.SmsSender;
import com.github.bingoohuang.xnotify.impl.SmsLog;
import com.github.bingoohuang.xnotify.util.OkHttp;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.joda.time.DateTime;

import java.util.Map;


@RequiredArgsConstructor @Slf4j
public class YunpianSmsSender implements SmsSender {
    private final String apikey;

    /**
     * 发送短信。
     *
     * @param mobile       目标手机号码
     * @param signName     云片的签名，是在模板申请中定死的，不能参数传递。如果需要使用不同签名，需要申请新的模板。
     * @param templateCode 模板编号（暂不用）
     * @param params       模板参数（暂不用）
     * @param text         发送消息（已经完成模板替换后的）
     */
    @Override
    public SmsLog send(String mobile, String signName, String templateCode, Map<String, String> params, String text) {
        SmsLog smsLog = send(mobile, signName, text);
        smsLog.setTemplateVars(JSON.toJSONString(params));
        return smsLog;
    }

    public SmsLog send(String mobile, String signName, String text) {
        val smsLog = SmsLog.builder();
        smsLog.logId("" + WestId.next()).mobile(mobile).signName(signName).eval(text).createTime(DateTime.now());

        Map<String, String> req = Maps.newHashMap();
        req.put("apikey", apikey);
        req.put("mobile", mobile);
        req.put("text", "【" + signName + "】" + text);

        val reqJson = JSON.toJSONString(req);
        smsLog.req(reqJson).reqTime(DateTime.now());

        val rspJSON = OkHttp.postForm("https://sms.yunpian.com/v2/sms/single_send.json", req);
        smsLog.rsp(rspJSON).rspTime(DateTime.now());

        val rsp = JSON.parseObject(rspJSON, Rsp.class);
        smsLog.rspId("" + rsp.getSid()).state(rsp.code == 0 ? 2 /* SUCC */ : 3 /* FAIL */);

        return smsLog.build();
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
