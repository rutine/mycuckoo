package com.mycuckoo.utils;

import java.util.Random;
import java.util.UUID;

/**
 * @author rutine
 * @time May 1, 2024 8:55:26 AM
 */
public abstract class IdGenerator {
    final static char[] digits = {
            '0' , '1' , '2' , '3' , '4' , '5' ,
            '6' , '7' , '8' , '9' , 'a' , 'b' ,
            'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
            'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
            'o' , 'p' , 'q' , 'r' , 's' , 't' ,
            'u' , 'v' , 'w' , 'x' , 'y' , 'z',
            'A' , 'B' , 'C' , 'D' , 'E' , 'F' ,
            'G' , 'H' , 'I' , 'J' , 'K' , 'L' ,
            'M' , 'N' , 'O' , 'P' , 'Q' , 'R' ,
            'S' , 'T' , 'U' , 'V' , 'W' , 'X' ,
            'Y' , 'Z'
    };


    private IdGenerator() {}


    public static String uuid() {
        String key = UUID.randomUUID().toString();
        StringBuilder builder = new StringBuilder();
        for (int i = 0, len = key.length(); i < len; i++) {
            char ch = key.charAt(i);
            if (ch != '-') {
                builder.append(ch);
            }
        }

        return builder.toString();
    }

    public static String randomNum(int len) {
        return random(len, 10);
    }

    public static String randomId(int len) {
        return random(len, digits.length - 1);
    }

    public static String random(int len, int radix) {
        if (radix >= digits.length) {
            radix =  digits.length - 1;
        }
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            sb.append(digits[random.nextInt(radix)]);
        }

        return sb.toString();
    }
}
