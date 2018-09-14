package com.github.bingoohuang.xnotify;


import com.google.common.collect.Lists;
import lombok.val;

import java.util.List;

public class XNotifyTemplate {
    private final List<XNotifyPart> parts = Lists.newArrayList();
    private final XNotify xNotify;


    public XNotifyTemplate(XNotify xNotify) {
        this.xNotify = xNotify;
        parse();
    }

    public void parse() {
        val quote = xNotify.quote();
        val quoteLength = quote.length();
        val value = xNotify.value();
        val valueLength = value.length();

        for (int pos = 0; pos < valueLength; ) {
            val quoteStart = value.indexOf(quote, pos);
            if (quoteStart < 0) {
                parts.add(new XNotifyPart(value.substring(pos), false));
                break;
            }

            val quoteEnd = value.indexOf(quote, quoteStart + quoteLength);
            if (quoteEnd < 0) throw new RuntimeException("unmatched quote found in " + xNotify);

            if (pos < quoteStart) parts.add(new XNotifyPart(value.substring(pos, quoteStart), false));

            parts.add(new XNotifyPart(value.substring(quoteStart + quoteLength, quoteEnd), true));

            pos = quoteEnd + quoteLength;
        }
    }

    public String eval(Object[] args) {
        val msg = new StringBuilder();
        int i = 0;

        for (val part : parts) {
            msg.append(part.eval(part.isVar() ? args[i++] : null));
        }
        return msg.toString();
    }
}
