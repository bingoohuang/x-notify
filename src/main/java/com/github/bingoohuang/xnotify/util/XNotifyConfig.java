package com.github.bingoohuang.xnotify.util;

import com.github.bingoohuang.utils.lang.ClzPath;
import lombok.SneakyThrows;
import lombok.val;

import java.util.Properties;

public class XNotifyConfig {
    public static final Properties config = loadEnvProperties();

    @SneakyThrows
    private static Properties loadEnvProperties() {
        val is = ClzPath.toInputStream("xnotify.properties");
        val p = new Properties();
        p.load(is);
        return p;
    }

    public static String get(String name) {
        return config.getProperty(name);
    }
}
