package com.github.bingoohuang.xnotify.sender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.github.bingoohuang.westid.WestId;
import com.github.bingoohuang.xnotify.XNotifyLogSender;
import com.github.bingoohuang.xnotify.XNotifySender;
import com.github.bingoohuang.xnotify.XNotifyTarget;
import com.github.bingoohuang.xnotify.impl.XNotifyLog;
import com.github.bingoohuang.xnotify.util.OkHttp;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.joda.time.DateTime;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TreeMap;

@RequiredArgsConstructor @Slf4j
public class AliyunSmsSender implements XNotifySender, XNotifyLogSender {
    private final String accessKeyId;
    private final String accessSecret;

    // https://help.aliyun.com/document_detail/56189.html
    @Override
    public XNotifyLog send(XNotifyTarget target, String msgType, String signName, String templateCode, Map<String, String> params, String text) {
        val smsLog = send(target.getMobile(), signName, templateCode, params);
        smsLog.setEval(text);
        return smsLog;
    }

    @SuppressWarnings("unchecked")
    @Override public void send(XNotifyLog log) {
        Map<String, String> params = JSON.parseObject(log.getTemplateVars(), Map.class);
        send(log, log.getMobile(), log.getSignName(), log.getTemplateCode(), params, log.getLogId());
    }

    public XNotifyLog send(String mobile, String signName, String templateCode, Map<String, String> params) {
        val smsLog = XNotifyLog.builder().build();
        val outId = "" + WestId.next();
        smsLog.setLogId(outId).setMobile(mobile).setSignName(signName).setCreateTime(DateTime.now())
                .setTemplateCode(templateCode).setTemplateVars(JSON.toJSONString(params));

        return send(smsLog, mobile, signName, templateCode, params, outId);
    }

    private XNotifyLog send(XNotifyLog smsLog, String mobile, String signName, String templateCode, Map<String, String> params, String outId) {
        val paras = new TreeMap<String, String>() {{
            // 1. 系统参数
            put("SignatureMethod", "HMAC-SHA1");
            put("SignatureNonce", java.util.UUID.randomUUID().toString());
            put("AccessKeyId", accessKeyId);
            put("SignatureVersion", "1.0");

            val df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            df.setTimeZone(new SimpleTimeZone(0, "GMT")); // 这里一定要设置GMT时区
            put("Timestamp", df.format(new Date()));
            put("Format", "JSON");
            // 2. 业务API参数
            put("Action", "SendSms");
            put("Version", "2017-05-25");
            put("RegionId", "cn-hangzhou");
            put("PhoneNumbers", mobile);
            put("SignName", signName);
            put("TemplateParam", JSON.toJSONString(params));
            put("TemplateCode", templateCode);
            put("OutId", outId);
        }};

        // 3. 构造待签名的字符串
        val sb = new StringBuilder();
        for (val e : ((Map<String, String>) paras).entrySet()) {
            sb.append("&" + enc(e.getKey()) + "=" + enc(e.getValue()));
        }
        val toSign = "GET&" + enc("/") + "&" + enc(sb.substring(1));
        val sign = hmacSHA1Base64(toSign, accessSecret + "&");
        // 4. 签名最后也要做特殊URL编码
        paras.put("Signature", enc(sign));

        smsLog.setReq(JSON.toJSONString(paras)).setReqTime(DateTime.now());

        // 最终打印出合法GET请求的URL
        val rspJSON = OkHttp.encodedGet("http://dysmsapi.aliyuncs.com/", paras);

        smsLog.setRsp(rspJSON).setRspTime(DateTime.now());

        val rsp = JSON.parseObject(rspJSON, Rsp.class);
        smsLog.setState("OK".equals(rsp.code) ? 2 /* SUCC */ : 3 /* FAIL */)
                .setRspId(rsp.getRequestId());

        return smsLog;
    }

    @SneakyThrows
    private String enc(String value) {
        return URLEncoder.encode(value, "UTF-8")
                .replace("+", "%20")
                .replace("*", "%2A")
                .replace("%7E", "~");
    }

    @SneakyThrows
    private String hmacSHA1Base64(String source, String secretKey) {
        val mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA1"));
        val signData = mac.doFinal(source.getBytes(StandardCharsets.UTF_8));
        return DatatypeConverter.printBase64Binary(signData);
    }

    @Data
    public static class Rsp {
        @JSONField(name = "RequestId")
        private String requestId;
        @JSONField(name = "Message")
        private String message;
        @JSONField(name = "Code")
        private String code;
    }
}
