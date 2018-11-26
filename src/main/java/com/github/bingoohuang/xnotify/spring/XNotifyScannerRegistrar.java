package com.github.bingoohuang.xnotify.spring;

import com.github.bingoohuang.utils.spring.XyzScannerRegistrar;
import com.github.bingoohuang.xnotify.XNotifyProvider;

public class XNotifyScannerRegistrar extends XyzScannerRegistrar {
    @SuppressWarnings("unchecked")
    public XNotifyScannerRegistrar() {
        super(XNotifyScan.class, XNotifyFactoryBean.class, XNotifyProvider.class);
    }
}
