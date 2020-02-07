package com.loveoyh.webserver.servlet;

import com.loveoyh.webserver.http.HttpRequest;
import com.loveoyh.webserver.http.HttpResponse;

import java.io.File;

/**
 * Servlet的超类，定义所有公用功能以及约定派生类行为
 * @author oyh
 */
public abstract class HttpServlet {
    public abstract void service(HttpRequest request, HttpResponse response);

    /**
     * 跳转到指定路径
     * 该方法在tomcat中应用时是使用request获取转发器中
     * 定义的方法，可以实现在不同Servlet或jsp之间内部跳转
     * @param url
     * @param request 在这里用不上
     * @param response
     */
    public void forward(String url,HttpRequest request,HttpResponse response){
        File file = new File("web"+url);
        response.setEntity(file);
    }
}
