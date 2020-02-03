package com.loveoyh.webserver.core;

import com.loveoyh.webserver.http.HttpRequest;
import com.loveoyh.webserver.http.ex.EmptyRequestException;

import java.io.IOException;
import java.io.InputStream;
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
            HttpRequest request = new HttpRequest(socket);
            System.out.println("处理客户端请求完毕!");
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
