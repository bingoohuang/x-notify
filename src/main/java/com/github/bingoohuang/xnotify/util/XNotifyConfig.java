package com.github.bingoohuang.xnotify.util;

import com.github.bingoohuang.utils.lang.ClzPath;
import lombok.SneakyThrows;
import lombok.val;

import java.util.Properties;

public interface XNotifyConfig {
    default String getConfig(String key) {
        return get(key);
    }

    Properties config = loadEnvProperties();

    @SneakyThrows
    static Properties loadEnvProperties() {
        val is = ClzPath.toInputStream("xnotify.properties");
        val p = new Properties();
        p.load(is);
        return p;
    }

    static String get(String name) {
        return config.getProperty(name);
    }
}
