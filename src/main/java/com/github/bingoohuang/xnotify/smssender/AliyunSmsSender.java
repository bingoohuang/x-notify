package com.github.bingoohuang.xnotify.smssender;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.westid.WestId;
import com.github.bingoohuang.xnotify.OkHttp;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@RequiredArgsConstructor @Slf4j
public class AliyunSmsSender {
    private final String accessKeyId;
    private final String accessSecret;

    // https://help.aliyun.com/document_detail/56189.html

    public void send(String mobile, String signName, String templateCode, Map<String, String> params) {
        val paras = new TreeMap<String, String>() {{
            // 1. 系统参数
            put("SignatureMethod", "HMAC-SHA1");
            put("SignatureNonce", java.util.UUID.randomUUID().toString());
            put("AccessKeyId", accessKeyId);
            put("SignatureVersion", "1.0");
            put("Timestamp", createTimestamp());
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
        val qs = createSignOriginalString(paras);
        val stringToSign = "GET&" + specialUrlEncode("/") + "&" + specialUrlEncode(qs);
        val sign = sign(accessSecret + "&", stringToSign);
        // 4. 签名最后也要做特殊URL编码
        val signature = specialUrlEncode(sign);

        paras.put("Signature", signature);

        log.info("send req {}", JSON.toJSONString(paras));

        // 最终打印出合法GET请求的URL
        val rsp = OkHttp.get("http://dysmsapi.aliyuncs.com/", paras);

        log.info("send rsp {}", rsp);
    }

    private String createSignOriginalString(Map<String, String> paras) {
        val sb = new StringBuilder();
        for (val e : paras.entrySet()) {
            sb.append("&").append(specialUrlEncode(e.getKey()))
                    .append("=").append(specialUrlEncode(e.getValue()));
        }

        return sb.substring(1);
    }

    private static String createTimestamp() {
        val df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));// 这里一定要设置GMT时区
        return df.format(new Date());
    }

    @SneakyThrows
    public static String specialUrlEncode(String value) {
        return URLEncoder.encode(value, "UTF-8")
                .replace("+", "%20")
                .replace("*", "%2A")
                .replace("%7E", "~");
    }

    @SneakyThrows
    public static String sign(String accessSecret, String stringToSign) {
        val mac = javax.crypto.Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(accessSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA1"));
        val signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        return DatatypeConverter.printBase64Binary(signData);
    }
}
