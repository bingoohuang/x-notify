package com.github.bingoohuang.xnotify.impl;

import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.Map;
import java.util.regex.Pattern;


public class XNotifyVarPart implements XNotifyPart {
    private final String part;
    private final String defaultValue;
    private final String templateVar;

    public XNotifyVarPart(String part, String templateVar) {
        int defaultValueIndex = part.indexOf(":!");

        this.part = defaultValueIndex < 0 ? part : part.substring(0, defaultValueIndex);
        this.defaultValue = defaultValueIndex < 0 ? "" : part.substring(defaultValueIndex + 2);
        this.templateVar = templateVar;
    }

    private String datetimePattern;
    static Pattern datePattern = Pattern.compile("(\\d+)(年|月|日)");
    static Pattern timePattern = Pattern.compile("(\\d\\d):(\\d\\d)(:\\d\\d)?");

    public String eval(Object arg, Map<String, String> templateVars) {
        String value = evalText(arg);

        val templateVarName = StringUtils.defaultIfEmpty(templateVar, part);
        val templateVarValue = StringUtils.defaultIfEmpty(value, defaultValue);

        templateVars.put(templateVarName, templateVarValue);
        return templateVarValue;
    }

    private String evalText(Object arg) {
        if (arg == null) return "";

        if (arg instanceof DateTime) {
            if (datetimePattern == null) createDateTimePattern();
            return ((DateTime) arg).toString(datetimePattern);
        }

        return arg.toString();
    }

    public boolean isVar() {
        return true;
    }

    private synchronized void createDateTimePattern() {
        if (datetimePattern != null) return;

        val sb = new StringBuffer();
        val dateMatcher = datePattern.matcher(part);
        while (dateMatcher.find()) {
            val number = dateMatcher.group(1);
            val unit = dateMatcher.group(2);
            val len = number.length();
            if (len == 4 && unit.equals("年")) {
                dateMatcher.appendReplacement(sb, "yyyy年");
            } else if (len <= 2 && !unit.equals("年")) {
                if (unit.equals("月")) dateMatcher.appendReplacement(sb, len > 1 ? "MM月" : "M月");
                else dateMatcher.appendReplacement(sb, len > 1 ? "dd日" : "d日");
            } else {
                throw new RuntimeException("unsupported datetime format " + part);
            }
        }
        dateMatcher.appendTail(sb);

        datetimePattern = sb.toString();
        val timeMatcher = timePattern.matcher(datetimePattern);
        if (timeMatcher.find()) {
            datetimePattern = timeMatcher.replaceFirst(timeMatcher.group(3) == null ? "HH:mm" : "HH:mm:ss");
        }

    }
}
