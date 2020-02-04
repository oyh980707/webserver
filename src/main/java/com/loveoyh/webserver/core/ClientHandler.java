package com.loveoyh.webserver.core;

import com.loveoyh.webserver.http.HttpRequest;
import com.loveoyh.webserver.http.ex.EmptyRequestException;

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
            System.out.println("处理客户端请求完毕!");

            //2
            String url = request.getUrl();
            //对应从web目录中找到该资源
            File file = new File("web"+url);
            if(file.exists()){
                OutputStream out = socket.getOutputStream();

                String line = "HTTP/1.1 200 OK";
                out.write(line.getBytes("ISO8859-1"));
                out.write(13);
                out.write(10);
                System.out.println("发送状态行:"+line);

                line = "Content-Type: text/html";
                out.write(line.getBytes("ISO8859-1"));
                out.write(13);
                out.write(10);
                System.out.println("发送响应头:"+line);

                line = "Content-Length: "+file.length();
                out.write(line.getBytes("ISO8859-1"));
                out.write(13);
                out.write(10);
                System.out.println("发送响应头:"+line);

                out.write(13);
                out.write(10);
                System.out.println("响应头发送完毕!");

                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[10*1024];
                int len = -1;
                while((len = fis.read(data)) != -1){
                    out.write(data,0,len);
                }
                System.out.println("响应正文完毕!");
            }else{
                file = new File("web/root/404.html");

                OutputStream out = socket.getOutputStream();

                String line = "HTTP/1.1 200 OK";
                out.write(line.getBytes("ISO8859-1"));
                out.write(13);
                out.write(10);
                System.out.println("发送状态行:"+line);

                line = "Content-Type: text/html";
                out.write(line.getBytes("ISO8859-1"));
                out.write(13);
                out.write(10);
                System.out.println("发送响应头:"+line);

                line = "Content-Length: "+file.length();
                out.write(line.getBytes("ISO8859-1"));
                out.write(13);
                out.write(10);
                System.out.println("发送响应头:"+line);

                out.write(13);
                out.write(10);
                System.out.println("响应头发送完毕!");

                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[10*1024];
                int len = -1;
                while((len = fis.read(data)) != -1){
                    out.write(data,0,len);
                }
                System.out.println("发送响应正文完毕!");
            }
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
