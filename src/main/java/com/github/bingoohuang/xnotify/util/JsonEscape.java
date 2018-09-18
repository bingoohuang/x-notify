package com.github.bingoohuang.xnotify.util;

import lombok.val;

import java.util.regex.Pattern;

public class JsonEscape {
    static final Pattern pattern = Pattern.compile("(['`^]).*?(\\1)\\s*:");

    public static String escapeJson(String json) {
        val matcher = pattern.matcher(json);
        return matcher.find() ? json.replace(matcher.group(1), "\"") : json;
    }
}
