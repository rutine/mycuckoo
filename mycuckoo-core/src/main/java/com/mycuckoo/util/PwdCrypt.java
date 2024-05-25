package com.mycuckoo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;


/**
 * 功能说明: 加密工具
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 22, 2014 9:27:57 PM
 */
public class PwdCrypt {
    private Logger logger = LoggerFactory.getLogger(PwdCrypt.class);

    private final String love = "liangs2yixiu!@#$%^&";

    public static PwdCrypt getInstance() {
        return new PwdCrypt();
    }

    private PwdCrypt() {

    }

    /**
     * 加密操作
     *
     * @param data
     * @return
     * @author rutine
     * @time Oct 3, 2012 4:23:03 PM
     */
    public String encrypt(String data) {
        BASE64Encoder encoder = new BASE64Encoder();

        return encoder.encode(simpleEncrypt(data).getBytes());
    }

    /**
     * 解密操作
     *
     * @param data
     * @return
     * @author rutine
     * @time Oct 3, 2012 4:23:14 PM
     */
    public String decrypt(String data) {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] result = null;
        try {
            result = decoder.decodeBuffer(data);
        } catch (IOException e) {
            logger.error("解密失败, data={}", data, e);
        }

        return simpleEncrypt(new String(result));
    }

    /**
     * 进行常量异或
     *
     * @param data
     * @return
     * @author rutine
     * @time Oct 3, 2012 4:23:35 PM
     */
    public String simpleEncrypt(String data) {
        char[] a = data.toCharArray();
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < love.length(); j++) {
                char c = love.charAt(j);
                a[i] = (char) (a[i] ^ c);
            }
        }
        String str = new String(a);

        return str;
    }

    public static void main(String[] args) {
        PwdCrypt pwdCrypt = new PwdCrypt();
        String estr = pwdCrypt.encrypt("admin");
        System.out.println("estr is : " + estr);
        String dstr = pwdCrypt.decrypt(estr);
        System.out.println("dstr is : " + dstr);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] result = null;

        try {
            result = decoder.decodeBuffer("UhIUBhEXF1Y=");
            System.out.println("dstr is : " + pwdCrypt.simpleEncrypt(new String(result)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
