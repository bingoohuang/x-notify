package com.github.bingoohuang.xnotify.sender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.github.bingoohuang.westid.WestId;
import com.github.bingoohuang.xnotify.SmsSender;
import com.github.bingoohuang.xnotify.impl.SmsLog;
import com.github.bingoohuang.xnotify.util.OkHttp;
import com.google.common.collect.Lists;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import org.joda.time.DateTime;
import org.n3r.eql.util.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor @Slf4j
public class TencentYunSmsSender implements SmsSender {
    private final String appKey;
    private final String sdkAppId;

    @Override
    public SmsLog send(String mobile, String signName, String templateCode, Map<String, String> params, String text) {
        val smsLog = send(mobile, signName, Integer.parseInt(templateCode), createParams(params));
        smsLog.setEval(text);
        smsLog.setTemplateCode(templateCode);
        smsLog.setTemplateVars(JSON.toJSONString(params));
        smsLog.setEval(text);
        return smsLog;
    }

    public SmsLog send(String mobile, String signName, int templateCode, List<String> params) {
        val smsLog = SmsLog.builder();
        val random = String.valueOf(WestId.next());
        smsLog.logId(random).mobile(mobile).signName(signName).createTime(DateTime.now());

        val url = HttpUrl.parse("https://yun.tim.qq.com/v5/tlssmssvr/sendsms").newBuilder()
                .addQueryParameter("sdkappid", sdkAppId) // sdkappid 请填写您在腾讯云上申请到的
                .addQueryParameter("random", random)     // random 请填成随机数。
                .toString();

        val time = System.currentTimeMillis() / 1000;
        val req = Req.builder()
                .ext(random)
                .templateId(templateCode)
                .tel(new Req.Tel(mobile))
                .params(params)
                .sign(signName)
                .time(time)
                .sig(computeSig(mobile, random, time))
                .build();
        val reqJson = JSON.toJSONString(req);
        smsLog.req(reqJson).reqTime(DateTime.now());

        val rspJSON = OkHttp.postJSON(url, reqJson);
        smsLog.rsp(rspJSON).rspTime(DateTime.now());

        val rsp = JSON.parseObject(rspJSON, Rsp.class);
        smsLog.rspId(rsp.getSid()).state(rsp.result == 0 ? 2 /* SUCC */ : 3 /* FAIL */);

        return smsLog.build();
    }

    private List<String> createParams(Map<String, String> params) {
        List<String> l = Lists.newArrayList();
        for (val e : params.entrySet()) {
            l.add(e.getValue());
        }

        return l;
    }

    /**
     * 给用户发短信验证码、短信通知，营销短信（内容长度不超过 450 字）。
     * ref: https://cloud.tencent.com/document/product/382/5976.
     */
    @Data @Builder
    public static class Req {
        private String ext;          // 用户的 session 内容，腾讯 server 回包中会原样返回，可选字段，不需要就填空
        private List<String> params; // 模板参数，若模板没有参数，请提供为空数组
        private String sig;          // App 凭证，计算公式：sha256（appkey=$appkey&random=$random&time=$time&mobile=$mobile）
        private String sign;         // 短信签名，如果使用默认签名，该字段可缺省
        private Tel tel;
        private long time;           // 请求发起时间，UNIX 时间戳（单位：秒），如果和系统时间相差超过 10 分钟则会返回失败
        @JSONField(name = "tpl_id")
        private int templateId;      // 模板 ID，在控制台审核通过的模板 ID

        @Data @RequiredArgsConstructor
        public static class Tel {
            private final String mobile;
            @JSONField(name = "nationcode")
            private String nationCode = "86";
        }
    }

    @Data
    public static class Rsp {
        private int result;    // 错误码，0 表示成功(计费依据)，非 0 表示失败，参考 错误码
        private String errmsg; // 错误消息，result 非 0 时的具体错误信息
        private String ext;    // 用户的 session 内容，腾讯 server 回包中会原样返回
        private int fee;       // 短信计费的条数，"fee" 字段计费说明
        private String sid;    // 本次发送标识 id，标识一次短信下发记录
    }

    @SneakyThrows
    public String computeSig(String mobile, String random, long time) {
        val content = "appkey=" + appKey + "&random=" + random + "&time=" + time + "&mobile=" + mobile;
        val sha256 = MessageDigest.getInstance("SHA-256");
        val hash = sha256.digest(content.getBytes(StandardCharsets.UTF_8));
        return Hex.encode(hash);
    }

}
