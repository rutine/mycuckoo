package com.mycuckoo.common.utils;

import java.io.UnsupportedEncodingException;


/**
 * 功能说明: 字符操作工具类
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 22, 2014 9:10:43 PM
 */
public class FirstLetter {

    // 国标码和区位码转换常量
    static final int GB_SP_DIFF = 160;

    // 存放国标一级汉字不同读音的起始区位码
    static final int[] secPosValueList = {
            1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787,

            3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086,

            4390, 4558, 4684, 4925, 5249, 5600
    };

    // 存放国标一级汉字不同读音的起始区位码对应读音
    static final char[] firstLetter = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j',

            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',

            't', 'w', 'x', 'y', 'z'
    };

    /**
     * 获取一个字符串的拼音码
     *
     * @param oriStr 字符串
     * @return 返回一个字符串的拼音码
     */
    public static String getFirstLetters(String oriStr) {
        String str = oriStr.toLowerCase();
        StringBuffer buffer = new StringBuffer();
        char ch;
        char[] temp;
        for (int i = 0; i < str.length(); i++) { // 依次处理str中每个字符
            ch = str.charAt(i);
            temp = new char[]{ch};
            try {
                byte[] uniCode = new String(temp).getBytes("GBK");
                if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字
                    buffer.append(temp);
                } else {
                    buffer.append(convert(uniCode));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return buffer.toString();
    }

    /**
     * 获取一个汉字的拼音首字母。
     * <p>
     * GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
     * <p>
     * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
     * <p>
     * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
     *
     * @param bytes 字节数组
     * @return 返回一个汉字的拼音首字母
     */
    static char convert(byte[] bytes) {
        char result = '-';
        int secPosValue = 0;
        int i;
        for (i = 0; i < bytes.length; i++) {
            bytes[i] -= GB_SP_DIFF;
        }
        secPosValue = bytes[0] * 100 + bytes[1];
        for (i = 0; i < 23; i++) {
            if (secPosValue >= secPosValueList[i]
                    && secPosValue < secPosValueList[i + 1]) {
                result = firstLetter[i];
                break;
            }
        }

        return result;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(FirstLetter.getFirstLetters("I love u"));

        System.out.println(FirstLetter.getFirstLetters("您好"));

        System.out.println(FirstLetter.getFirstLetters("I love 北京天安门"));
    }

}
