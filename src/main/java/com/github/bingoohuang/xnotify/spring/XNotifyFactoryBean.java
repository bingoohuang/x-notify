package com.github.bingoohuang.xnotify.spring;

import com.github.bingoohuang.utils.spring.XyzFactoryBean;
import com.github.bingoohuang.xnotify.impl.XNotifyFactory;

public class XNotifyFactoryBean extends XyzFactoryBean {
    public XNotifyFactoryBean() {
        super(XNotifyFactory::create);
    }
}
