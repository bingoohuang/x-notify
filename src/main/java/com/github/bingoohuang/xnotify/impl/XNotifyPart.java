package com.github.bingoohuang.xnotify.impl;

import java.util.Map;

public interface XNotifyPart {
    String eval(Object arg, Map<String, String> templateVars);

    boolean isVar();
}
