package com.github.bingoohuang.xnotify;

/**
 * 发送通知给谁。
 */
public interface XNotifyTarget {
    /**
     * 手机号码。发短信时，需要。
     *
     * @return 手机号码。
     */
    default String getMobile() {
        return null;
    }

    /**
     * 用户姓名。方便查问题，记录一下。
     *
     * @return 用户姓名。
     */
    default String getUsername() {
        return null;
    }

    /**
     * 用户微信openId。发模板消息时使用。
     *
     * @return 微信openId。
     */
    default String getOpenid() {
        return null;
    }

    /**
     * 用户所属组织。比如瑜伽用户所在商户。记录方便后续查问题。
     *
     * @return 用户所属组织。
     */
    default String getUsergroup() {
        return null;
    }
}
