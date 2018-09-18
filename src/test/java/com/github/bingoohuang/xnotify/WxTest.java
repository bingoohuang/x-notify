package com.github.bingoohuang.xnotify;

import com.github.bingoohuang.xnotify.impl.XNotifyFactory;
import lombok.val;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class WxTest {
    @Test
    public void test() {
        Wx wx = XNotifyFactory.create(Wx.class);
        val fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        val start = DateTime.parse("2018-09-18 16:56:21", fmt);
        val msg = wx.buySuccess("12345", "黑巧克力", 99.9, start, "http://z.cn");
        assertThat(msg).isEqualTo("{" +
                "   'touser':'12345'," +
                "   'template_id':'template_id_var'," +
                "   'data':{'first':{'value':'恭喜你购买成功！', 'color':'#173177' }," +
                "     'keyword1':{'value':'黑巧克力', 'color':'#173177' }," +
                "     'keyword2':{'value':'99.9元', 'color':'#173177' }," +
                "     'keyword3':{'value':'2018年9月18日', 'color':'#173177' }," +
                "     'remark':{'value':'欢迎再次购买！', 'color':'#173177' }" +
                "   }," +
                "   'url':'http://z.cn'" +
                " }");
    }

    public interface Wx {
        @XNotify(value = "{" +
                "   'touser':'<OPENID>'," +
                "   'template_id':'template_id_var'," +
                "   'data':{'first':{'value':'恭喜你购买成功！', 'color':'#173177' }," +
                "     'keyword1':{'value':'<巧克力>', 'color':'#173177' }," +
                "     'keyword2':{'value':'<39.8>元', 'color':'#173177' }," +
                "     'keyword3':{'value':'<2014年9月22日>', 'color':'#173177' }," +
                "     'remark':{'value':'欢迎再次购买！', 'color':'#173177' }" +
                "   }," +
                "   'url':'<http://weixin.qq.com/download>'" +
                " }", quotes = "<,>")
        String buySuccess(String openId, String goodsName, double price, DateTime buyTime, String url);
    }
}
