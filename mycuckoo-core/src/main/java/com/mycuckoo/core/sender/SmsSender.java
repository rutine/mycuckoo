package com.mycuckoo.core.sender;

import java.util.Arrays;
import java.util.List;

/**
 * @author rutine
 * @date 2024/7/4 15:46
 */
public interface SmsSender {

    default void send(String phone, String tpl, Object params) {
        this.send(Arrays.asList(phone), tpl, params, null);
    }

    default void send(String phone, String tpl, Object params, String signName) {
        this.send(Arrays.asList(phone), tpl, params, signName);
    }

    default void send(List<String> phones, String tpl, Object params) {
        this.send(phones, tpl, params, null);
    }

    void send(List<String> phones, String tpl, Object params, String signName);
}
