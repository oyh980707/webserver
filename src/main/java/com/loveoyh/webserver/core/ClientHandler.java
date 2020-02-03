package com.loveoyh.webserver.core;

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
            InputStream in = socket.getInputStream();
            String line = readLine(in);
            System.out.println("Line:"+line);
            System.out.println("处理客户端请求完毕!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过输入流读取一行字符串，以CRLF结尾为一行字符串。
     * 返回的字符串中不含有最后的CRLF. CR(13) LF(10)
     * @param in
     * @return 读取一行的内容
     */
    private String readLine(InputStream in){
        StringBuilder builder = new StringBuilder();
        try {
            /*
             * c1表示上次读取到的字符
             * c2表示本次读取到的字符
             */
            char c1='0',c2='0';
            int d = -1;
            while((d=in.read()) != -1){
                c2 = (char)d;
                if(c1==13 && c2==10){
                    break;
                }
                builder.append(c2);
                //下次读取前将当前赋值
                c1 = c2;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString().trim();
    }
}
