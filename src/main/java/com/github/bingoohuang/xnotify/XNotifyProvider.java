package com.github.bingoohuang.xnotify;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface XNotifyProvider {
    Class<? extends XProvider> value();
}
