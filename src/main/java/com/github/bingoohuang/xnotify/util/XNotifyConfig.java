package com.github.bingoohuang.xnotify.util;

import com.github.bingoohuang.utils.lang.Classpath;

import java.util.Properties;

public interface XNotifyConfig {
    default String getConfig(String key) {
        return get(key);
    }

    Properties config = Classpath.loadProperties("xnotify.properties");

    static String get(String name) {
        return config.getProperty(name);
    }
}
