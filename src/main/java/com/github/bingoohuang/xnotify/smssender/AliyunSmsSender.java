package com.github.bingoohuang.xnotify.smssender;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.westid.WestId;
import com.github.bingoohuang.xnotify.SmsSender;
import com.github.bingoohuang.xnotify.util.OkHttp;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

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
public class AliyunSmsSender implements SmsSender {
    private final String accessKeyId;
    private final String accessSecret;

    // https://help.aliyun.com/document_detail/56189.html
    @Override
    public void send(String mobile, String signName, String templateCode, Map<String, String> params, String text) {
        send(mobile, signName, templateCode, params);
    }

    public void send(String mobile, String signName, String templateCode, Map<String, String> params) {
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
            put("OutId", "" + WestId.next());
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

        log.info("send req {}", JSON.toJSONString(paras));

        // 最终打印出合法GET请求的URL
        val rsp = OkHttp.encodedGet("http://dysmsapi.aliyuncs.com/", paras);

        log.info("send rsp {}", rsp);
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
}
