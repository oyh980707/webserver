package com.loveoyh.webserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WebServer主类
 * @author oyh
 */
public class WebServer {
    private ServerSocket server;

    private ExecutorService threadPool;

    public WebServer() {
        try {
            System.out.println("服务端启动中...");
            //申请服务器端口
            server = new ServerSocket(8080);
            threadPool = Executors.newFixedThreadPool(50);
            System.out.println("服务端启动成功!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务端启动
     */
    public void start() {
        while (true) {
            try {
                System.out.println("等待客户端连接...");
                Socket socket = server.accept();
                System.out.println("客户端连接成功!");

                ClientHandler handler = new ClientHandler((socket));
                //将ClientHandler交给线程池处理
                threadPool.execute(handler);

                System.out.println("完毕!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        WebServer server = new WebServer();
        server.start();
    }
}
