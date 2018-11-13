package com.github.bingoohuang.xnotify.sender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.github.bingoohuang.westid.WestId;
import com.github.bingoohuang.xnotify.XNotifyLogSender;
import com.github.bingoohuang.xnotify.XNotifySender;
import com.github.bingoohuang.xnotify.XNotifyTarget;
import com.github.bingoohuang.xnotify.impl.XNotifyLog;
import com.github.bingoohuang.xnotify.util.JsonEscape;
import com.github.bingoohuang.xnotify.util.OkHttp;
import lombok.Data;
import lombok.val;
import okhttp3.HttpUrl;
import org.joda.time.DateTime;

import java.net.Proxy;
import java.util.Map;

public abstract class WxTemplateMsgSender implements XNotifySender, XNotifyLogSender {
    @Override
    public XNotifyLog send(XNotifyTarget target, String msgType, String signName, String templateCode, Map<String, String> params, String text) {
        val json = JsonEscape.escapeJson(text);

        val log = XNotifyLog.builder().build();
        val outId = "" + WestId.next();
        log.setLogId(outId).setMobile(target.getMobile()).setSignName(signName).setCreateTime(DateTime.now())
                .setTemplateCode(templateCode).setTemplateVars(JSON.toJSONString(params));

        val templateId = getTemplateId(log);
        val content = json.replace("template_id_var", templateId);
        sendTemplateMessage(getAccessToken(log), content, getProxy());

        return null;
    }

    protected Proxy getProxy() {
        return null;
    }

    abstract protected String getTemplateId(XNotifyLog log);

    abstract protected String getAccessToken(XNotifyLog log);

    /**
     * 发送微信模板消息。
     *
     * @param accessToken 接口调用凭证
     * @param json        模板消息请求数据
     * @param proxy       代理，没有代理时传null
     * @return 调用结果
     */
    public static Rsp sendTemplateMessage(String accessToken, String json, Proxy proxy) {
        val url = HttpUrl.parse("https://api.weixin.qq.com/cgi-bin/message/template/send").newBuilder()
                .addQueryParameter("access_token", accessToken).toString();

        val s = OkHttp.postJSON(url, json, proxy);
        return JSON.parseObject(s, Rsp.class);
    }

    @Override public void send(XNotifyLog log) {
        val content = log.getEval().replace("template_id_var", getTemplateId(log));
        log.setEval(content);
        sendTemplateMessage(getAccessToken(log), content, getProxy());
    }

    /**
     * JavaBean for WxTemplateMessageSender Template Message POST data, like :
     *
     * <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277">API REFERENCE</a>
     * 发送模板消息
     * 接口调用请求说明
     * http请求方式: POST
     * https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN
     * <pre>
     * {
     *      "touser":"OPENID",
     *      "template_id":"ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY",
     *      "url":"http://weixin.qq.com/download",
     *      "miniprogram":{
     *          "appid":"xiaochengxuappid12345",
     *          "pagepath":"index?foo=bar"
     *      },
     *      "data":{ "first": { "value":"恭喜你购买成功！", "color":"#173177" },
     *          "keyword1":{ "value":"巧克力", "color":"#173177" },
     *          "keyword2": { "value":"39.8元", "color":"#173177" },
     *          "keyword3": { "value":"2014年9月22日", "color":"#173177" },
     *          "remark":{ "value":"欢迎再次购买！", "color":"#173177" }
     *      }
     * }
     * </pre>
     * <p>
     * 参数	是否必填	说明
     * touser	是	接收者openid
     * template_id	是	模板ID
     * url	否	模板跳转链接
     * miniprogram	否	跳小程序所需数据，不需跳小程序可不用传该数据
     * appid	是	所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系，暂不支持小游戏）
     * pagepath	否	所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar），暂不支持小游戏
     * data	是	模板数据
     * color	否	模板内容字体颜色，不填默认为黑色
     * <p>
     * 注：url和miniprogram都是非必填字段，若都不传则模板无跳转；若都传，会优先跳转至小程序。
     * 开发者可根据实际需要选择其中一种跳转方式即可。当用户的微信客户端版本不支持跳小程序时，将会跳转至url。
     * <p>
     * 返回码说明
     * <p>
     * 在调用模板消息接口后，会返回JSON数据包。正常时的返回JSON数据包示例：
     * <p>
     * {
     * "errcode":0,
     * "errmsg":"ok",
     * "msgid":200228332
     * }
     */

    @Data
    public static class Rsp {
        @JSONField(name = "errcode")
        private int errCode;
        @JSONField(name = "errmsg")
        private String errMsg;
        @JSONField(name = "msgid")
        private String msgId;
    }
}
