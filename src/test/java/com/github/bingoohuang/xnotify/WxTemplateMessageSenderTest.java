package com.github.bingoohuang.xnotify;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.xnotify.util.WxTemplateMessageSender;
import lombok.val;
import org.junit.Test;

public class WxTemplateMessageSenderTest {
    @Test
    public void test() {
        val accessToken = "your accessToken";
        val rsp = WxTemplateMessageSender.sendTemplateMessage(accessToken, JSON.toJSONString(WxTemplateMessageSender.Req.builder()
                .templateId("EtbKuo-W7vWJut_JD86gH1Akqf15MpCC8HYodbiFjr4")
                .openId("o_R-jxB8VCIO_v_Sy0pLy5nZNljo")
                .put("first", WxTemplateMessageSender.Item.of("预约人：黄进兵(5951770693)"))
                .put("keyword1", WxTemplateMessageSender.Item.of("普拉提 (美美Amy，南京店)"))
                .put("keyword2", WxTemplateMessageSender.Item.of("X人"))
                .put("keyword3", WxTemplateMessageSender.Item.of("9月17日 15:00-17:00"))
                .put("remark", WxTemplateMessageSender.Item.of(""))
                .build()));

        System.out.println(rsp);
    }
}
