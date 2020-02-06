package com.loveoyh.webserver.http;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
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
     * 1：在项目中导入dom4j的jar包
     * 2：这里通过dom解析项目目录中config/web.xml
     * 3：将web.xml中根目录标签中所有名为：mime-mapping的子标签获取回来
     *  注意：跟标签下不是只有mime-mapping名字的字标签
     * 4：遍历所有的mime-mapping标签，将该标签中的子标签<extension>中间的文本作为key
     * 	子标签：<mime-type>中间的文本作为value保存到MIME_MAPPING这个map中完成初始化
     *
     */
    private static void initMimeMapping() {
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(new File("config/web.xml"));
            Element root = doc.getRootElement();
            List<Element> mimeList = root.elements("mime-mapping");
            for(Element mime : mimeList){
                String key = mime.elementText("extension");
                String value = mime.elementText("mime-type");
                MIME_MAPING.put(key,value);
            }
            System.out.println(MIME_MAPING.size());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
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

    public static void main(String[] args) {
        System.out.println(HttpContext.getMimeType("txt"));
    }
}
