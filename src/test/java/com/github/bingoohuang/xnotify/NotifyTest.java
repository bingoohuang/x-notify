package com.github.bingoohuang.xnotify;

import com.github.bingoohuang.xnotify.impl.XNotifyFactory;
import com.github.bingoohuang.xnotify.impl.XNotifyLog;
import lombok.val;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.BeforeClass;
import org.junit.Test;
import org.n3r.eql.Eql;
import org.n3r.eql.eqler.EqlerFactory;
import org.n3r.eql.util.C;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class NotifyTest {
    static Sms sms = XNotifyFactory.create(Sms.class);
    static MyXNotifyLogDao dao = EqlerFactory.getEqler(MyXNotifyLogDao.class);

    @BeforeClass
    public static void beforeClass() {
        String sql = C.classResourceToString("h2-createTable.sql");
        new Eql().execute(sql);
    }

    @Test
    public void firstBlood() {
        val fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        val start = DateTime.parse("2018-09-14 22:13:31", fmt);
        val end = DateTime.parse("2018-09-14 23:13:31", fmt);
        val x = sms.classNotMeetRemind("菜鸟瑜伽", start, end, "空中瑜伽", "私教课");
        assertThat(x).isEqualTo("[菜鸟瑜伽]有节课人数未达标，系统已自动取消已订课会员的预约：2018年09月14日22:13-23:13空中瑜伽（私教课）");
    }

    @Test
    public void sendConfirmCode() {
        dao.clearLogs();

        val code = RandomStringUtils.randomNumeric(6);
        val text = sms.sendConfirmCode("18551855099", "奕起嗨", code, "精武堂");
        assertThat(text).isEqualTo("验证码为：" + code + "（15分钟内有效），验证码打死也不要告诉别人哦！精武堂");

        List<XNotifyLog> XNotifyLogs = dao.queryLogs();
        assertThat(XNotifyLogs.size()).isEqualTo(1);


        val target = new XNotifyTarget() {
            @Override public String getMobile() {
                return "18551855099";
            }

            @Override public String getUsername() {
                return "bingoohuang";
            }
        };
        val text2 = sms.sendConfirmCode(target, "奕起嗨", code, "精武堂");
        assertThat(text2).isEqualTo("验证码为：" + code + "（15分钟内有效），验证码打死也不要告诉别人哦！精武堂");

        XNotifyLogs = dao.queryLogs();
        assertThat(XNotifyLogs.size()).isEqualTo(2);
        assertThat(XNotifyLogs.get(1).getUsername()).isEqualTo("bingoohuang");

        val text3 = sms.sendConfirmCode("18551855099", code);
        assertThat(text3).isEqualTo("验证码为：" + code + "（15分钟内有效），验证码打死也不要告诉别人哦！默认场馆名称");
    }

    @XNotifyProvider(value = MySaveDbProvider.class, type = "sms")
    public interface Sms {
        // 某排期课的开班人数未达标时，发送提醒
        @XNotify("[#静瑜伽#]有节课人数未达标，系统已自动取消已订课会员的预约：#2018年09月26日15:00#-#16:00##阴瑜伽#（#小班课#）")
        String classNotMeetRemind(String merchantName, DateTime start, DateTime end, String scheduleName, String courseTypeName);

        @XNotify(value = "验证码为：#code#（15分钟内有效），验证码打死也不要告诉别人哦！#name#", templateCode = "tencent:68689,aliyun:SMS_12841674")
        String sendConfirmCode(@XNotifyParam("target") String target, @XNotifyParam("signName") String signName, String code, String merchantName);

        @XNotify(value = "验证码为：#code#（15分钟内有效），验证码打死也不要告诉别人哦！#name#", templateCode = "tencent:68689,aliyun:SMS_12841674")
        String sendConfirmCode(XNotifyTarget target, @XNotifyParam("signName") String signName, String code, String merchantName);

        @XNotify(value = "验证码为：#code#（15分钟内有效），验证码打死也不要告诉别人哦！#name:!默认场馆名称#", templateCode = "tencent:68689,aliyun:SMS_12841674")
        String sendConfirmCode(@XNotifyParam("target") String target, String code);
    }


    static Wx wx = XNotifyFactory.create(Wx.class);

    @Test
    public void test() {
        val fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        val start = DateTime.parse("2018-09-18 16:56:21", fmt);
        val msg = wx.buySuccess("12345", "黑巧克力", 99.9, start, "http://z.cn");
        assertThat(msg).isEqualTo("{" +
                " `touser`:`12345`," +
                " `template_id`:`template_id_var`," +
                " `data`:{`first`:{`value`:`恭喜你购买成功！`, `color`:`#173177` }," +
                "  `keyword1`:{`value`:`黑巧克力`, `color`:`#173177` }," +
                "  `keyword2`:{`value`:`99.9元`, `color`:`#173177` }," +
                "  `keyword3`:{`value`:`2018年9月18日`, `color`:`#173177` }," +
                "  `remark`:{`value`:`欢迎再次购买！`, `color`:`#173177` }" +
                " }," +
                " `url`:`http://z.cn`" +
                "}");
    }

    @XNotifyProvider(value = MySaveDbProvider.class, type = "wx")
    public interface Wx {
        @XNotify(value = "{" +
                " `touser`:`<OPENID>`," +
                " `template_id`:`template_id_var`," +
                " `data`:{`first`:{`value`:`恭喜你购买成功！`, `color`:`#173177` }," +
                "  `keyword1`:{`value`:`<巧克力>`, `color`:`#173177` }," +
                "  `keyword2`:{`value`:`<39.8>元`, `color`:`#173177` }," +
                "  `keyword3`:{`value`:`<2014年9月22日>`, `color`:`#173177` }," +
                "  `remark`:{`value`:`欢迎再次购买！`, `color`:`#173177` }" +
                " }," +
                " `url`:`<http://weixin.qq.com/download>`" +
                "}", quotes = "<,>")
        String buySuccess(String openId, String goodsName, double price, DateTime buyTime, String url);
    }
}
