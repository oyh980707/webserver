package com.loveoyh.webserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * WebServer主类
 * @author oyh
 */
public class WebServer {
    private ServerSocket server;

    public WebServer() {
        try {
            System.out.println("服务端启动中...");
            //申请服务器端口
            server = new ServerSocket(8080);
            System.out.println("服务端启动成功!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务端启动
     */
    public void start() {
        try {
            System.out.println("等待客户端连接...");
            Socket socket = server.accept();
            System.out.println("客户端连接成功!");

            Thread t = new Thread(new ClientHandler(socket));
            t.start();

            System.out.println("完毕!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WebServer server = new WebServer();
        server.start();
    }
}
