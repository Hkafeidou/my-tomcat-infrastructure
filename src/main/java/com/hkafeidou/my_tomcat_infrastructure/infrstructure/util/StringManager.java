package com.hkafeidou.my_tomcat_infrastructure.infrstructure.util;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class StringManager {
    private ResourceBundle bundle;
    
    public StringManager(String packageName) {
        String bundleName = packageName+".LocalStrings";
        bundle = ResourceBundle.getBundle(bundleName);
    }
    
    public String getString(String key) {
        if(null == key) {
            String msg = "key is null";
            throw new NullPointerException(msg);
        }
        String str = null;
        try {
            str = bundle.getString(key);
        } catch (MissingResourceException e) {
            // TODO Auto-generated catch block
            str="Cannot find message associated with key '"+key+"'";
        }
        
        return str;
    }
    
    /**
     * 带参数格式化输出
     * @param key
     * @param args
     * @return
     */
    public String getString(String key,Object[] args) {
        String iString = null;
        String value = getString(key);
        try {
            Object nonNullArgs[] =  args;
            for(int i =0;i<args.length;i++) {
                if(null == args[i]) {
                    if(nonNullArgs==args) {
                        nonNullArgs=(Object[])args.clone();
                    }
                    nonNullArgs[i]="null";
                }
            }
            iString = MessageFormat.format(value, nonNullArgs);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            StringBuffer buf = new StringBuffer();
            buf.append(value);
            for(int i=0;i<args.length;i++) {
                buf.append(" args["+i+"]="+args[i]);
            }
            iString = buf.toString();
        }
        return iString;
    }
    
    public String getString(String key,Object arg) {
        Object[] args = new Object[] {arg};
        return getString(key,args);
    }
    
    public String getString(String key, Object arg1, Object arg2) {
        Object[] args = new Object[] {arg1, arg2};
        return getString(key, args);
    }
    public String getString(String key, Object arg1, Object arg2,
            Object arg3) {
        Object[] args = new Object[] {arg1, arg2, arg3};
        return getString(key, args);
    }
    public String getString(String key, Object arg1, Object arg2,
            Object arg3, Object arg4) {
        Object[] args = new Object[] {arg1, arg2, arg3, arg4};
        return getString(key, args);
    }
    
    private static Hashtable manager = new Hashtable();
    
    public synchronized static StringManager getManager(String packageName) {
        StringManager mgr = (StringManager)manager.get(packageName);
        if(null == mgr) {
            mgr = new StringManager(packageName);
            manager.put(packageName, mgr);
        }
        return mgr;
    }
    
}
