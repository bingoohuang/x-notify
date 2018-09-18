package com.github.bingoohuang.xnotify.provider;

import com.github.bingoohuang.xnotify.util.JsonEscape;
import com.github.bingoohuang.xnotify.util.WxTemplateMessageSender;
import lombok.val;

import java.lang.reflect.Method;

public abstract class WxProvider {

    public void send(String text, Method method) {
        val json = JsonEscape.escapeJson(text);
        val templateId = getTemplateId(method.getClass().getSimpleName() + "." + method.getName());
        val content = json.replace("template_id_var", templateId);
        WxTemplateMessageSender.sendTemplateMessage(getAccessToken(), content);
    }

    protected abstract String getTemplateId(String classDotMethodName);

    protected abstract String getAccessToken();
}
