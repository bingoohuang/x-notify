package com.github.bingoohuang.xnotify.impl;

import lombok.Value;

import java.util.Map;

@Value
public class TemplateEval {
    private final String text;
    private final Map<String, String> templateVars;
    private final Map<String, String> templateCodeMap;
}
