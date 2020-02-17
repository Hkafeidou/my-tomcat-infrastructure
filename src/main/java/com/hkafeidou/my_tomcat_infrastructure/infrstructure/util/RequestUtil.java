package com.hkafeidou.my_tomcat_infrastructure.infrstructure.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.Cookie;

public final class RequestUtil {
    private static SimpleDateFormat format=new SimpleDateFormat("EEE,dd-MMM-yy kk:mm:ss zz");
    
    static {
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    
    public static String encodeCookie(Cookie cookie) {
        StringBuffer buf = new StringBuffer(cookie.getName());
        buf.append("=");
        buf.append(cookie.getValue());
        if(null!=cookie.getComment()) {
            buf.append("; Comment=\"");
            buf.append(cookie.getComment());
            buf.append("\"");
        }
        
        if(null != cookie.getDomain()) {
            buf.append("; Domain=\"");
            buf.append(cookie.getDomain());
            buf.append("\"");
        }
        
        long age = cookie.getMaxAge();
        if(age>=0) {
            buf.append("; Max-Age=\"");
            buf.append(cookie.getMaxAge());
            buf.append("\"");
        }
        
        if(null!=cookie.getPath()) {
            buf.append("; Path=\"");
            buf.append(cookie.getPath());
            buf.append("\"");
        }
        
        if(cookie.getSecure()) {
            buf.append("; Source");
        }
        
        if(cookie.getVersion()>0) {
            buf.append("; Version=\"");
            buf.append(cookie.getVersion());
            buf.append("\"");
        }
        return buf.toString();
    }
    
    public static String filter(String message) {
        if(null == message) {
            return null;
        }
        char content[] = new char[message.length()];
        message.getChars(0, message.length(), content, 0);
        StringBuilder result = new StringBuilder(content.length+50);
        for(int i = 0;i<content.length;i++) {
            switch (content[i]) {
                case '<' :
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                case '"':
                    result.append("&quot;");
                    break;
                default :
                    result.append(content[i]);
                    break;
            }
        }
        
        return result.toString();
    }
    
    public static String normalize(String path) {
        if(null == path) {
            return null;
        }
        
        String normalized = path;
        if(normalized.equals("/.")) {
            return "/";
        }
        
        if(!normalized.startsWith("/")) {
            normalized = "/" + normalized;
        }
        
        while(true) {
            int index = normalized.indexOf("//");
            if(index<0) {
                break;
            }
            normalized = normalized.substring(0,index)+normalized.substring(index+1);
        }
        
        while(true) {
            int index = normalized.indexOf("/../");
            if (index < 0)
                break;
            if (index == 0)
                return (null);  // Trying to go outside our context
            int index2 = normalized.lastIndexOf('/', index - 1);
            normalized = normalized.substring(0, index2) +
                normalized.substring(index + 3);
        }
        
        return normalized;
    }
    
    public static String parseCharacterEncoding(String contentType) {
        if(null == contentType) {
            return null;
        }
        int start = contentType.indexOf("charset=");
        if(start<0) {
            return null;
        }
        String encoding = contentType.substring(start+8);//推测 编码 最长小于8个字节
        int end = encoding.indexOf(";");
        if(end>0) {
            encoding = encoding.substring(0,end);
        }
        encoding=encoding.trim();
        if(encoding.length()>2 && encoding.startsWith("\"") && encoding.endsWith("\"")) {
            encoding=encoding.substring(1,encoding.length()-1);
        }
        return encoding.trim();
    }
    
    public static Cookie[] parseCookieHeader(String header) {
        if(null == header || header.length()<1) {
            return new Cookie[0];
        }
        
        ArrayList cookies = new ArrayList<>();
        while(header.length()>0) {
            int semicolon = header.indexOf(";");
            if(semicolon<0) {
                semicolon=header.length();
            }
            if(semicolon==0) {
                break;
            }
            String token  = header.substring(0,semicolon);
            if(semicolon<header.length()) {
                header = header.substring(semicolon+1);
            }else {
                header="";
            }
            
            try {
                int equals = token.indexOf("=");
                if(equals>0) {
                    String name = token.substring(0,equals).trim();
                    String value = token.substring(equals+1).trim();
                    cookies.add(new Cookie(name,value));
                }
            } catch (Throwable e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        return ((Cookie[]) cookies.toArray(new Cookie[cookies.size()]));
        
    }
    
    public static void parseParameters(Map map,String data,String encoding)  throws UnsupportedEncodingException  {
        if(null != data && data.length()>0) {
            int len = data.length();
            byte[] bytes = new byte[len];
            data.getBytes(0,len,bytes,0);
            parseParameters(map, bytes, encoding);
        }
    }
    
    public static void parseParameters(Map map,byte[] data,String encoding) throws UnsupportedEncodingException {
        if(null != data &&data.length>0) {
            int pos = 0;
            int ix=0;
            int ox =0;
            String key = null;
            String value = null;
            while(ix<data.length) {
                byte c = data[ix++];
                switch((char)c) {
                    case '&':
                        value = new String(data,0,ox,encoding);
                        if(null != key) {
                            putMapEntry(map, key, value);
                            key = null;
                        }
                        ox=0;
                        break;
                    case '=':
                        key = new String(data, 0, ox, encoding);
                        ox = 0;
                        break;
                    case '+':
                        data[ox++] = (byte)' ';
                        break;
                    case '%':
                        data[ox++] = (byte)((convertHexDigit(data[ix++]) << 4)
                                        + convertHexDigit(data[ix++]));
                        break;
                    default:
                        data[ox++] = c;
                }
            }
            if(key!=null) {
                value = new String(data, 0, ox, encoding);
                putMapEntry(map, key, value);
            }
        }
    }
    
    
    public static String URLDecode(String str) {
        return URLDecode(str, null);
    }
    
    public static String URLDecode(String str,String encoding) {
        if(null != str) {
            return null;
        }
        int len = str.length();
        byte[] bytes = new byte[len];
        str.getBytes(0,len,bytes,0);
        return URLDecode(bytes,encoding);
    }
    
    public static String URLDecode(byte[] bytes) {
        return URLDecode(bytes, null);
    }
    
    public static String URLDecode(byte[] bytes,String enc) {
        if(null == bytes) {
            return null;
        }
        
        int len = bytes.length;
        int ix = 0;
        int ox = 0;
        while(ix<len) {
            byte b  = bytes[ix++];
            if(b == '+') {
                b=(byte)' ';
            }else if(b == '%') {
                b = (byte)((convertHexDigit(bytes[ix++])<<4)+convertHexDigit(bytes[ix++]));
            }
            bytes[ox++]=b;
        }
        
        if(null != enc) {
            try {
                return new String(bytes,0,ox,enc);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return new String(bytes,0,ox);
    }
    
    private static byte convertHexDigit(byte b) {
        if((b>='0')&& (b<='9')) {
            return (byte)(b-'0');
        }
        
        if(b>='a' && b <='f') {
            return (byte)(b-'a'+10);
        }
        if(b>='A' && b <='F') {
            return (byte)(b-'A'+10);
        }
        return 0;
    }
    
    private static void putMapEntry(Map map,String name,String value) {
        String[] newValues = null;
        String[] oldValues = (String[])map.get(name);
        if(null == oldValues) {
            newValues= new String[1];
            newValues[0]=value;
        }else {
            newValues = new String[oldValues.length+1];
            System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);
        }
        map.put(name,newValues);
    }
    
}
