package com.github.bingoohuang.xnotify.impl;


import com.github.bingoohuang.xnotify.XNotify;
import com.github.bingoohuang.xnotify.XNotifyMsgType;
import com.github.bingoohuang.xnotify.XNotifyParam;
import com.github.bingoohuang.xnotify.XNotifyTarget;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XNotifyTemplate {
    private final List<XNotifyPart> parts = Lists.newArrayList();
    private final XNotify xNotify;
    private final int targetArgIndex;
    private final int sigNameArgIndex;
    private final List<Integer> varIndices;

    public XNotifyTemplate(Method method, XNotify xNotify) {
        this.xNotify = xNotify;
        this.targetArgIndex = findTargetArgIndex(method);
        this.sigNameArgIndex = findArg(method, "signName");
        this.varIndices = createVarIndex(method);
        parse();
    }

    private int findTargetArgIndex(Method method) {
        int index = findArg(method, "target");
        if (index >= 0) return index;

        for (int i = 0; i < method.getParameterCount(); ++i) {
            Class<?> pType = method.getParameterTypes()[i];
            if (XNotifyTarget.class.isAssignableFrom(pType)) return i;
        }

        return -1;
    }

    private List<Integer> createVarIndex(Method method) {
        List<Integer> indices = Lists.newArrayList();

        for (int i = 0; i < method.getParameterCount(); ++i) {
            val xp = method.getParameters()[i].getAnnotation(XNotifyParam.class);
            if (xp != null) continue;

            if (XNotifyTarget.class.isAssignableFrom(method.getParameterTypes()[i])) continue;

            indices.add(i);
        }

        return indices;
    }

    public void parse() {
        String[] quotes = xNotify.quotes().split(",", 2);
        val quoteOpen = quotes[0];
        val quoteOpenLength = quoteOpen.length();
        val quoteClose = quotes[1];
        val quoteCloseLength = quoteOpen.length();

        val value = xNotify.value();
        val valueLength = value.length();

        val templateVars = xNotify.templateVars().split(",");
        int index = 0;

        for (int pos = 0; pos < valueLength; ) {
            val quoteStart = value.indexOf(quoteOpen, pos);
            if (quoteStart < 0) {
                parts.add(new XNotifyConstPart(value.substring(pos)));
                break;
            }

            val quoteEnd = value.indexOf(quoteClose, quoteStart + quoteOpenLength);
            if (quoteEnd < 0) throw new RuntimeException("unmatched quotes found in " + xNotify);

            if (pos < quoteStart) parts.add(new XNotifyConstPart(value.substring(pos, quoteStart)));

            val varDemo = value.substring(quoteStart + quoteOpenLength, quoteEnd);
            parts.add(new XNotifyVarPart(varDemo, getTemplateVar(templateVars, index++)));

            pos = quoteEnd + quoteCloseLength;
        }
    }

    private String getTemplateVar(String[] templateVars, int index) {
        if (index < templateVars.length) return templateVars[index];
        return templateVars.length == 1 ? templateVars[0] : null;
    }

    public TemplateEval eval(Object[] args) {
        val msg = new StringBuilder();
        int i = 0;

        Map<String, String> templateVars = Maps.newHashMap();
        for (val part : parts) {
            msg.append(part.eval(part.isVar() ? args[varIndices.get(i++)] : null, templateVars));
        }

        String templateCode = xNotify.templateCode();
        Map<String, String> templateCodeMap = StringUtils.isNotEmpty(templateCode)
                ? Splitter.on(',').withKeyValueSeparator(':').split(templateCode)
                : new HashMap<>();

        return new TemplateEval(msg.toString(), templateVars, templateCodeMap);
    }

    private static int findArg(Method method, String paramName) {
        for (int i = 0; i < method.getParameterCount(); ++i) {
            val xp = method.getParameters()[i].getAnnotation(XNotifyParam.class);
            if (xp != null && xp.value().equals(paramName)) return i;
        }

        return -1;
    }

    @SuppressWarnings("unchecked")
    public XNotifyTarget getTarget(Object[] args, XNotifyMsgType type) {
        if (targetArgIndex < 0) return null;

        if (args[targetArgIndex] instanceof XNotifyTarget) {
            return (XNotifyTarget) args[targetArgIndex];
        }

        if (args[targetArgIndex] instanceof String) {
            val target = (String) args[targetArgIndex];
            if (target == null) return null;

            if (type == XNotifyMsgType.SMS) {
                return new XNotifyTarget() {
                    @SuppressWarnings("unchecked")
                    @Override public String getMobile() {
                        return target;
                    }
                };
            } else if (type == XNotifyMsgType.WX_TEMPLATE_MSG)
                return new XNotifyTarget() {
                    @SuppressWarnings("unchecked")
                    @Override public String getOpenid() {
                        return target;
                    }
                };
        }

        throw new RuntimeException("unsupported type of target arg " + args[targetArgIndex]);
    }

    public String getSigName(Object[] args) {
        return sigNameArgIndex >= 0 ? (String) args[sigNameArgIndex] : null;
    }
}
