package com.example.springbootdatamybatis.mozart.utils;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:46
 **/
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.Iterator;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/** @deprecated */
@Deprecated
public final class RequestUtil {
    private static final String HEADER_X_REAL_IP = "X-Real-IP";
    private static final Splitter pattern = Splitter.on('&').trimResults().omitEmptyStrings();

    public RequestUtil() {
    }

    public static String getRealIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        }

        int pos = ip.lastIndexOf(44);
        if (pos >= 0) {
            ip = ip.substring(pos);
        }

        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }

    public static String getPathInfo(HttpServletRequest request) {
        return request == null ? null : request.getRequestURI().substring(request.getContextPath().length());
    }

    public static boolean isIntranet(HttpServletRequest request) {
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && xRealIp.length() != 0) {
            try {
                InetAddress address = InetAddress.getByName(xRealIp);
                return address.isSiteLocalAddress() || address.isLoopbackAddress();
            } catch (UnknownHostException var3) {
                return false;
            }
        } else {
            return true;
        }
    }

    public static boolean match(String host, String url) {
        if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("//")) {
            url = url.substring(url.indexOf(47) + 2);
        }

        int idx = url.indexOf(47);
        return idx > 0 ? url.substring(0, idx).endsWith(host) : url.endsWith(host);
    }

    public static boolean match(String[] hosts, String url) {
        if (Strings.isNullOrEmpty(url)) {
            return false;
        } else if (hosts != null && hosts.length != 0) {
            String[] var2 = hosts;
            int var3 = hosts.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String host = var2[var4];
                if (match(host, url)) {
                    return true;
                }
            }

            return false;
        } else {
            return true;
        }
    }

    public static boolean validRedirect(String url) {
        return validRedirect(url, "", "r", "cbt");
    }

    public static boolean validRedirect(String url, String secret, String dataKey, String tokenKey) {
        if (Strings.isNullOrEmpty(url)) {
            return false;
        } else {
            int start = url.indexOf("?");
            if (start < 0) {
                return false;
            } else {
                String d = dataKey + "=";
                String t = tokenKey + "=";
                String data = null;
                String token = null;
                Iterator var9 = pattern.split(url.substring(start + 1)).iterator();

                while(var9.hasNext()) {
                    String str = (String)var9.next();
                    if (str.startsWith(d)) {
                        data = str.substring(d.length());
                    } else if (str.startsWith(t)) {
                        token = str.substring(t.length());
                    }
                }

                try {
                    data = URLDecoder.decode(data, Charsets.UTF_8.name());
                } catch (UnsupportedEncodingException var11) {
                }

                return validRedirect(data, secret, token);
            }
        }
    }

    public static boolean validRedirect(String data, String secret, String token) {
        return rtoken(data, secret).equals(token);
    }

    public static String rtoken(String data, String secret) {
        byte[] bytes = (data + secret).getBytes(Charsets.UTF_8);
        return Hashing.md5().hashBytes(Hashing.md5().hashBytes(bytes).asBytes()).toString();
    }

    public static Cookie cookieOf(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        } else {
            Cookie[] var3 = cookies;
            int var4 = cookies.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Cookie cookie = var3[var5];
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }

            return null;
        }
    }
}
