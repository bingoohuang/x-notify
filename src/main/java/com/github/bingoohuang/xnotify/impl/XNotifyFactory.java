package com.github.bingoohuang.xnotify.impl;

import com.github.bingoohuang.xnotify.XNotify;
import com.github.bingoohuang.xnotify.XNotifyProvider;
import com.github.bingoohuang.xnotify.XNotifyTarget;
import com.github.bingoohuang.xnotify.XProvider;
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

            val msgType = xNotifyProvider.type();
            val target = template.getTarget(args, msgType);

            notify(xProvider, target, method, args, template, eval, xNotify, msgType);
        }

        return eval.getText();
    }

    private static void notify(XProvider xProvider, XNotifyTarget target, Method method, Object[] args,
                               XNotifyTemplate template, TemplateEval eval, XNotify xNotify, String msgType) {
        var templateCode = eval.getTemplateCodeMap().get(xProvider.getProviderName());
        if (StringUtils.isEmpty(templateCode)) templateCode = xNotify.templateCode();
        if (StringUtils.isEmpty(templateCode))
            templateCode = method.getDeclaringClass().getSimpleName() + "." + method.getName();

        val sigName = template.getSigName(args);
        val sender = xProvider.getSender();
        if (sender != null) {
            val smsLog = sender.send(target, msgType, sigName, templateCode, eval.getTemplateVars(), eval.getText());
            log.info("send log {}", smsLog);
        }
    }
}
