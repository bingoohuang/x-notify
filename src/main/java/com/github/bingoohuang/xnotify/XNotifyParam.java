package com.github.bingoohuang.xnotify;

import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface XNotifyParam {
    // 参数名称
    String value();
}
