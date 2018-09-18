package com.github.bingoohuang.xnotify.impl;

import com.github.bingoohuang.xnotify.SmsProvider;
import com.github.bingoohuang.xnotify.XNotify;
import com.github.bingoohuang.xnotify.XNotifyProvider;
import com.github.bingoohuang.xnotify.XProvider;
import com.github.bingoohuang.xnotify.provider.WxProvider;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.n3r.eql.joor.Reflect;
import org.springframework.cglib.proxy.Proxy;

import java.lang.reflect.Method;

@Slf4j
public class XNotifyFactory {
    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(XNotifyFactory.class.getClassLoader(), new Class[]{interfaceClass},
                (o, method, args) -> processNotify(interfaceClass, method, args));
    }

    private static <T> Object processNotify(Class<T> interfaceClass, Method method, Object[] args) {
        val xNotify = method.getAnnotation(XNotify.class);
        if (xNotify == null) throw new RuntimeException("@XNotify required for method " + method);

        val template = new XNotifyTemplate(method, xNotify);
        val eval = template.eval(args);

        val xNotifyProvider = interfaceClass.getAnnotation(XNotifyProvider.class);
        if (xNotifyProvider != null) {
            XProvider xProvider = Reflect.on(xNotifyProvider.value()).create().get();

            if (xProvider instanceof SmsProvider) {
                sendSms((SmsProvider) xProvider, method, args, template, eval, xNotify);
            } else if (xProvider instanceof WxProvider) {
                sendWx((WxProvider)xProvider, method, args, template, eval, xNotify);
            }
        }

        return eval.getText();
    }

    private static void sendWx(WxProvider xProvider, Method method, Object[] args, XNotifyTemplate template, TemplateEval eval, XNotify xNotify) {
        xProvider.send(eval.getText(), method);
    }

    private static void sendSms(SmsProvider xProvider, Method method, Object[] args, XNotifyTemplate template, TemplateEval eval, XNotify xNotify) {
        val mobile = template.getMobile(args);
        if (StringUtils.isEmpty(mobile)) {
            log.warn("sms not send because of no mobile for " + method);
            return;
        }

        var templateCode = eval.getTemplateCodeMap().get(xProvider.getProviderName());
        if (templateCode == null) templateCode = xNotify.templateCode();

        val sigName = template.getSigName(args);

        val sender = xProvider.getSmsSender();
        val smsLog = sender.send(mobile, sigName, templateCode, eval.getTemplateVars(), eval.getText());

        log.info("send log {}", smsLog);
    }


}
