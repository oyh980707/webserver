package com.loveoyh.webserver.http;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP协议相关内容
 * @author oyh
 *
 */
public class HttpContext {
    /** 状态码与对应描述的映射关系 */
    private static Map<Integer, String> STATUS_CODE_REASON_MAPPING = new HashMap<Integer,String>();
    /**  */
    private static Map<String, String> MIME_MAPING = new HashMap<String, String>();

    static{
        //初始化状态代码与描述的映射
        initStatusMapping();
        //初始化介质类型映射
        initMimeMapping();
        //初始化其他内容
    }

    /**
     * 初始化介质类型映射
     */
    private static void initMimeMapping() {
        MIME_MAPING.put("html", "text/html");
        MIME_MAPING.put("png", "image/png");
        MIME_MAPING.put("gif", "image/gif");
        MIME_MAPING.put("jpg", "image/jpeg");
        MIME_MAPING.put("css", "text/css");
        MIME_MAPING.put("js", "application/javascript");
    }

    /**
     * 初始化状态代码与描述的映射
     */
    private static void initStatusMapping(){
        STATUS_CODE_REASON_MAPPING.put(200, "OK");
        STATUS_CODE_REASON_MAPPING.put(201, "Created");
        STATUS_CODE_REASON_MAPPING.put(202, "Accepted");
        STATUS_CODE_REASON_MAPPING.put(204, "No Content");
        STATUS_CODE_REASON_MAPPING.put(301, "Moved Permanently");
        STATUS_CODE_REASON_MAPPING.put(302, "Moved Temporarily");
        STATUS_CODE_REASON_MAPPING.put(304, "Not Modified");
        STATUS_CODE_REASON_MAPPING.put(400, "Bad Request");
        STATUS_CODE_REASON_MAPPING.put(401, "Unauthorized");
        STATUS_CODE_REASON_MAPPING.put(403, "Forbidden");
        STATUS_CODE_REASON_MAPPING.put(404, "Not Found");
        STATUS_CODE_REASON_MAPPING.put(500, "Internal Server Error");
        STATUS_CODE_REASON_MAPPING.put(501, "Not Implemented");
        STATUS_CODE_REASON_MAPPING.put(502, "Bad Gateway");
        STATUS_CODE_REASON_MAPPING.put(503, "Service Unavailable");
    }

    /**
     * 根据给定的状态代码返回对应的状态描述
     * @param statusCode
     * @return 状态码对应的描述
     */
    public static String getStatusReason(int statusCode){
        return STATUS_CODE_REASON_MAPPING.get(statusCode);
    }

    /**
     * 根据给定的后缀名获取相应的介质类型
     * @param ext
     * @return
     */
    public static String getMimeType(String ext){
        return MIME_MAPING.get(ext);
    }
}
