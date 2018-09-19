package com.github.bingoohuang.xnotify.impl;

import com.github.bingoohuang.xnotify.*;
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

            val target = findTarget(template, args, method, xNotifyProvider.type());
            if (target != null) notify(xProvider, target, method, args, template, eval, xNotify);
        }

        return eval.getText();
    }

    private static XNotifyTarget findTarget(XNotifyTemplate template, Object[] args, Method method, XNotifyMsgType type) {
        val target = template.getTarget(args, type);
        if (target == null) log.warn("message not send because of no target for " + method);
        return target;
    }

    private static void notify(XProvider xProvider, XNotifyTarget target, Method method, Object[] args,
                               XNotifyTemplate template, TemplateEval eval, XNotify xNotify) {
        var templateCode = eval.getTemplateCodeMap().get(xProvider.getProviderName());
        if (StringUtils.isEmpty(templateCode)) templateCode = xNotify.templateCode();
        if (StringUtils.isEmpty(templateCode)) templateCode = method.getDeclaringClass().getSimpleName() + "." + method.getName();

        val sigName = template.getSigName(args);
        val sender = xProvider.getSender();
        val smsLog = sender.send(target, sigName, templateCode, eval.getTemplateVars(), eval.getText());

        log.info("send log {}", smsLog);
    }
}
