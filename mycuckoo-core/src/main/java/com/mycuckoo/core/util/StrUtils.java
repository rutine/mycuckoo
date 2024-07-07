package com.mycuckoo.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能说明: 常用工具类
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 22, 2014 9:36:10 PM
 */
public class StrUtils {
    private static Logger logger = LoggerFactory.getLogger(StrUtils.class);

    /**
     * 加密操作
     *
     * @param data
     * @return
     * @author rutine
     * @time Oct 4, 2012 1:08:18 PM
     */
    public static String encrypt(String data) {
        return PwdCrypt.getInstance().encrypt(data);
    }

    /**
     * 解密操作
     *
     * @param data
     * @return
     * @author rutine
     * @time Oct 4, 2012 1:08:36 PM
     */
    public static String decrypt(String data) {
        return PwdCrypt.getInstance().decrypt(data);
    }

    /**
     * 获得一句话的首字母
     *
     * @param words
     * @return
     * @author rutine
     * @time Oct 6, 2012 2:38:11 PM
     */
    public static String getFirstLetters(String words) {
        return FirstLetter.getFirstLetters(words);
    }

    /**
     * 检查是否为null
     *
     * @param obj 被检查对象
     * @return
     * @author rutine
     * @time Oct 4, 2012 1:08:52 PM
     */
    public static boolean isNull(Object obj) {
        if (obj == null) return true;
        return false;
    }

    /**
     * 检查字符串是否为空
     *
     * @param str
     * @return
     * @author rutine
     * @time Oct 4, 2012 1:09:16 PM
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim())) return true;
        return false;
    }

    public static boolean isNotBlank(String str) {
        return !isEmpty(str);
    }
}
