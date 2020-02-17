package com.hkafeidou.my_tomcat_infrastructure.infrstructure.util;

import java.text.FieldPosition;
import java.util.Date;

import javax.servlet.http.Cookie;

public class CookieTools {
    public static String getCookieHeaderName(Cookie cookie) {
        int version = cookie.getVersion();

        if (version == 1) {
            return "Set-Cookie2";
        } else {
            return "Set-Cookie";
        }
    }
    
    public static String getCookieHeaderValue(Cookie cookie) {
        StringBuffer buf = new StringBuffer();
        getCookieHeaderValue( cookie, buf );
        return buf.toString();
    }
    
    public static void getCookieHeaderValue(Cookie cookie,StringBuffer buf) {
        int version = cookie.getVersion();
        String name = cookie.getName();
        if(null == name) {
            name="";
        }
        String value = cookie.getValue();
        if(null == value) {
            value="";
        }
        buf.append(name).append("=");
        maybeQuote(version, buf, value);
        
        if(version==1) {
            buf.append (";Version=1");
            if (cookie.getComment() != null) {
                buf.append (";Comment=");
                maybeQuote (version, buf, cookie.getComment());
            }
        }
        
        if (cookie.getDomain() != null) {
            buf.append(";Domain=");
            maybeQuote (version, buf, cookie.getDomain());
        }
        
        if (cookie.getMaxAge() >= 0) {
            if (version == 0) {
                buf.append (";Expires=");
                if (cookie.getMaxAge() == 0)
                    DateTool.oldCookieFormat.format(new Date(10000), buf,
                                                    new FieldPosition(0));
                else
                    DateTool.oldCookieFormat.format
                        (new Date( System.currentTimeMillis() +
                                   cookie.getMaxAge() *1000L), buf,
                         new FieldPosition(0));
            } else {
                buf.append (";Max-Age=");
                buf.append (cookie.getMaxAge());
            }
        }else if (version == 1) {
            buf.append (";Discard");
        }
        if (cookie.getPath() != null) {
            buf.append (";Path=");
            maybeQuote (version, buf, cookie.getPath());
        }

        // Secure
        if (cookie.getSecure()) {
          buf.append (";Secure");
        }
    }
    
    static void maybeQuote(int version,StringBuffer buf,String value) {
        if(version==0||isToken(value)) {
            buf.append(value);
        }else {
            buf.append('"').append(value).append('"');
        }
    }
    private static final String tspecials = "()<>@,;:\\\"/[]?={} \t";
    private static boolean isToken(String value) {
        int len =value.length();
        for(int i=0;i<len;i++) {
            char c = value.charAt(i);
            if(c<0x20 ||c>0x7f || tspecials.indexOf(c)!=-1) {
                return false;
            }
        }
        return true;
    }
    
    
}
