package com.hkafeidou.my_tomcat_infrastructure.infrstructure.my_const;

import java.io.File;

/**
 * http server default const
 * @author Hao
 *
 */
public class HttpServerConst {
    
    public static final String SCHEME="http";
    
    /**
     * web default workspace is application work directory
     */
    public static final String DEFAULT_WEB_ROOT=System.getProperty("user.dir")+ File.separator  + "webroot";
    
    /**
     * shutdwon command 
     */
    public static final String DEFAULT_SHUTDOWN_COMMAND="/SHUTDWON";
    
    /**
     * default server port
     */
    public static final int DEFAULT_PORT=8080;
    
    /**
     * default server address
     */
    public static final String DEFAULT_SERVER_ADDR="127.0.0.1";
    
    /***
     * default buffer size
     */
    public static final int DEFAULT_BUFF_SIZE=2048;
    
    public static final String DEFAULT_ENCODING="UTF-8";
    
    public static final String MY_SERVER_KEY="/servlet/";
    
}
