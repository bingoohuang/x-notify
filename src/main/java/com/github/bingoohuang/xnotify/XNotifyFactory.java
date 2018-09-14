package com.github.bingoohuang.xnotify;

import lombok.val;
import org.springframework.cglib.proxy.Proxy;

public class XNotifyFactory {
    public static <T> T create(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(XNotifyFactory.class.getClassLoader(), new Class[]{interfaceClass}, (o, method, args) -> {
            val xNotify = method.getAnnotation(XNotify.class);
            if (xNotify == null) throw new RuntimeException("@XNotify required for method " + method);

            XNotifyTemplate template = new XNotifyTemplate(xNotify);
            return template.eval(args);
        });
    }
}
