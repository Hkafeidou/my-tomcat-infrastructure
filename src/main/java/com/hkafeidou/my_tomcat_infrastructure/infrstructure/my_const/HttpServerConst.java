package com.hkafeidou.my_tomcat_infrastructure.infrstructure.my_const;

/**
 * http server default const
 * @author Hao
 *
 */
public class HttpServerConst {
    /**
     * web default workspace is application work directory
     */
    public static final String DEFAULT_WEB_ROOT=System.getProperty("user.dir");
    
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
    
}
