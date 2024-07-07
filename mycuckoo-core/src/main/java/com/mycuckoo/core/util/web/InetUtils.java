package com.mycuckoo.core.util.web;

import com.mycuckoo.core.util.StrUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author rutine
 * @time May 1, 2024 8:55:26 AM
 */
public abstract class InetUtils {
    private static final String IP_UNKNOWN = "unknown";
    private static final String IP_LOCAL = "127.0.0.1";
    private static final int IP_LEN = 15;

    private InetUtils() {}


    public static String getLocalIp() {
        InetAddress addr;
        String ip = null;
        try {
            addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress().toString(); // 获取本机ip
        } catch (UnknownHostException e) {
            //ignore
        }

        return ip;
    }

    /**
     * 获取客户端真实ip
     * @param request request
     * @return 返回ip
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (StrUtils.isEmpty(ip) || IP_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
            if (!StrUtils.isEmpty(ip)) {
                // 多次反向代理后会有多个ip值，第一个ip才是真实ip
                int index = ip.indexOf(",");
                if (index != -1) {
                    return ip.substring(0, index);
                } else {
                    return ip;
                }
            }
        }

        if (StrUtils.isEmpty(ip) || IP_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

//    /**
//     * 获取客户端真实ip
//     * @param request request
//     * @return 返回ip
//     */
//    public static String getIp(ServerHttpRequest request) {
//        HttpHeaders headers = request.getHeaders();
//        String ipAddress = headers.getFirst("x-forwarded-for");
//        if (ipAddress == null || ipAddress.length() == 0 || IP_UNKNOWN.equalsIgnoreCase(ipAddress)) {
//            ipAddress = headers.getFirst("Proxy-Client-IP");
//        }
//        if (ipAddress == null || ipAddress.length() == 0 || IP_UNKNOWN.equalsIgnoreCase(ipAddress)) {
//            ipAddress = headers.getFirst("WL-Proxy-Client-IP");
//        }
//        if (ipAddress == null || ipAddress.length() == 0 || IP_UNKNOWN.equalsIgnoreCase(ipAddress)) {
//            ipAddress = Optional.ofNullable(request.getRemoteAddress())
//                    .map(address -> address.getAddress().getHostAddress())
//                    .orElse("");
//            if (IP_LOCAL.equals(ipAddress)) {
//                // 根据网卡取本机配置的IP
//                try {
//                    InetAddress inet = InetAddress.getLocalHost();
//                    ipAddress = inet.getHostAddress();
//                } catch (UnknownHostException e) {
//                    // ignore
//                }
//            }
//        }
//
//        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
//        if (ipAddress != null && ipAddress.length() > IP_LEN) {
//            int index = ipAddress.indexOf(",");
//            if (index > 0) {
//                ipAddress = ipAddress.substring(0, index);
//            }
//        }
//
//        return ipAddress;
//    }

    public static long[] ipToLong(String ip) {
        if (ip.contains(":")) {
            return ipv6ToLong(ip);
        } else {
            return new long[] {0, ipv4ToLong(ip)};
        }
    }

    private static long[] ipv6ToLong(String ipv6) {
        byte[] result = ipv6ToBytes(ipv6);

        long high = 0;
        long low = 0;
        for (int i = 0; i < result.length; i++) {
            if (i < 8) {
                high = high | (((long) result[i] & 0xFF) << ((7 - i) * 8));
            } else {
                low = low | (((long) result[i] & 0xFF) << ((15 - i) * 8));
            }
        }

        return new long[] {high, low};
    }

    private static byte[] ipv6ToBytes(String ipv6) {
        if (ipv6.startsWith(":")) {
            ipv6 = ipv6.substring(1);
        }

        byte[] result = new byte[16];
        int pos = result.length;
        boolean comFlag = false; //ipv4混合模式标记
        String[] arr = ipv6.split(":");
        for (int i = arr.length - 1; i > -1; i--) {
            if (arr[i].contains(".")) {
                //ipv4混合模式
                String[] ipv4s = arr[i].split("\\.");
                result[--pos] = Short.valueOf(ipv4s[3]).byteValue();
                result[--pos] = Short.valueOf(ipv4s[2]).byteValue();
                result[--pos] = Short.valueOf(ipv4s[1]).byteValue();
                result[--pos] = Short.valueOf(ipv4s[0]).byteValue();
                comFlag = true;
            } else if ("".equals(arr[i])) {
                //出现零长度压缩, 计算缺少数组
                int less = 9 - (arr.length + (comFlag ? 1 : 0));
                while (less-- > 0) { //这些数组置0
                    result[--pos] = 0;
                    result[--pos] = 0;
                }
            } else {
                int tmp = Integer.parseInt(arr[i], 16);
                result[--pos] = (byte) tmp;
                result[--pos] = (byte) (tmp >> 8);
            }
        }

        return result;
    }

    private static long ipv4ToLong(String ipv4) {
        if (ipv4 == null || ipv4.trim().equals("")) {
            return 0;
        }

        String[] arr = ipv4.split("\\.");
        return Long.valueOf(arr[0]) << 24 | Long.valueOf(arr[1]) << 16 | Long.valueOf(arr[2]) << 8 | Long.valueOf(arr[3]);
    }
}
