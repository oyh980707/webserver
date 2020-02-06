package com.loveoyh.webserver.core;

import com.loveoyh.webserver.http.HttpRequest;
import com.loveoyh.webserver.http.HttpResponse;
import com.loveoyh.webserver.http.ex.EmptyRequestException;
import com.loveoyh.webserver.servlet.RegServlet;

import java.io.*;
import java.net.Socket;

/**
 * 客户端处理器，用来处理指定客户端的请求并与响应
 * @author oyh
 */
public class ClientHandler implements Runnable{
    private Socket socket;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("开始处理客户端请求...");
            /*
             * 处理客户端请求的大致流程
             * 1：解析请求
             * 2：处理请求
             * 3：响应客户端
             */
            //1
            HttpRequest request = new HttpRequest(socket);
            //2
            HttpResponse response = new HttpResponse(socket);

            String url = request.getRequestURI();

            if("/myweb/reg".equals(url)){
                RegServlet reg = new RegServlet();
                reg.service(request,response);
            }else{
                //对应从web目录中找到该资源
                File file = new File("web"+url);
                if(file.exists()){
                    response.setEntity(file);
                }else{
                    file = new File("web/root/404.html");

                    response.setStatusCode(404);
                    response.setEntity(file);
                }
            }

            response.flush();

        }catch (EmptyRequestException e) {
            System.err.println(e.getClass().getName());
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //响应完后断开连接
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
