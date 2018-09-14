package com.github.bingoohuang.xnotify;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface XNotify {
    /**
     * 模板内容。里面使用quote分割包含的部分，需要使用实际参数替换掉。
     *
     * @return template content.
     */
    String value() default "";

    /**
     * 模板中引用需要使用实际变量替换的部分。
     *
     * @return 模板变量引用符号。
     */
    String quote() default "`";
}
