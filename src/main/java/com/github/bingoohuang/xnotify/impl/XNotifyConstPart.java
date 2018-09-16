package com.github.bingoohuang.xnotify.impl;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class XNotifyConstPart implements XNotifyPart {
    private final String part;

    @Override public String eval(Object arg, Map<String, String> templateVars) {
        return part;
    }

    @Override public boolean isVar() {
        return false;
    }
}
