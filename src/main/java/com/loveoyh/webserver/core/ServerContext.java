package com.loveoyh.webserver.core;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务端环境类，用于保存服务端相关信息
 * @author oyh
 *
 */
public class ServerContext {
    /**
     * Servlet映射信息
     * Key:请求路径
     * value:处理该请求的Servlet的名字
     */
    private static Map<String, String> SERVLET_MAPPING = new HashMap<String, String>();

    static {
        initServerMapping();
    }

    /**
     * 初始化Servlet映射关系
     */
    private static void initServerMapping(){
        /*
         * 解析config/servlets.xml
         * 将根标签servlets下所有的servlet标签解析出来
         * 将每个servlet标签中的属性url的值作为key，将className属性的值作为value存入到
         * SERVLET_MAPPING中
         */
        try(FileInputStream fis = new FileInputStream("config/servlets.xml")) {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(fis);
            Element root = doc.getRootElement();
            List<Element> servletList = root.elements();
            for(Element servlet : servletList){
                String key = servlet.attributeValue("url");
                String value = servlet.attributeValue("className");
                SERVLET_MAPPING.put(key, value);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 根据请求获取对应的Servlet的名字
     * @param url
     * @return
     */
    public static String getServletName(String url){
        return SERVLET_MAPPING.get(url);
    }
}
