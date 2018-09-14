package com.github.bingoohuang.xnotify;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.joda.time.DateTime;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class XNotifyPart {
    private final String part;
    private final boolean isVar;

    private String datetimePattern;

    static Pattern datePattern = Pattern.compile("(\\d+)(年|月|日)");
    static Pattern timePattern = Pattern.compile("(\\d\\d):(\\d\\d)(:\\d\\d)?");

    public String eval(Object arg) {
        if (!isVar) return part;

        if (arg instanceof DateTime) {
            if (datetimePattern == null) createDateTimePattern();
            return ((DateTime) arg).toString(datetimePattern);
        }

        return String.valueOf(arg);
    }

    public boolean isVar() {
        return isVar;
    }

    private synchronized void createDateTimePattern() {
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
