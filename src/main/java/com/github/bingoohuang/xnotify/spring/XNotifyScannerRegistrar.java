package com.github.bingoohuang.xnotify.spring;

import com.github.bingoohuang.utils.spring.XyzScannerRegistrar;
import com.github.bingoohuang.xnotify.XNotify;

public class XNotifyScannerRegistrar extends XyzScannerRegistrar {
    public XNotifyScannerRegistrar() {
        super(XNotifyScan.class, XNotifyFactoryBean.class, XNotify.class);
    }
}
