package com.github.bingoohuang.xnotify;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.xnotify.util.Wx;
import lombok.val;
import org.junit.Test;

public class WxTest {
    @Test
    public void test() {
        val accessToken = "your accessToken";
        val rsp = Wx.sendTemplateMessage(accessToken, JSON.toJSONString(Wx.Req.builder()
                .templateId("EtbKuo-W7vWJut_JD86gH1Akqf15MpCC8HYodbiFjr4")
                .openId("o_R-jxB8VCIO_v_Sy0pLy5nZNljo")
                .put("first", Wx.Item.of("预约人：黄进兵(5951770693)"))
                .put("keyword1", Wx.Item.of("普拉提 (美美Amy，南京店)"))
                .put("keyword2", Wx.Item.of("X人"))
                .put("keyword3", Wx.Item.of("9月17日 15:00-17:00"))
                .put("remark", Wx.Item.of(""))
                .build()));

        System.out.println(rsp);
    }
}
