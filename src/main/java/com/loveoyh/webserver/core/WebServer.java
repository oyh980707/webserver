package com.loveoyh.webserver.core;

import sun.awt.image.InputStreamImageSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * WebServer主类
 */
public class WebServer {
    private ServerSocket server;

    public WebServer() {
        try {
            System.out.println("服务端启动中...");
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

            InputStream in = socket.getInputStream();

            int d = -1;
            while((d=in.read()) != -1){
                System.out.println((char)d);
            }

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
