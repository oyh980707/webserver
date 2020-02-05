package com.loveoyh.webserver.http;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 响应对象
 * 其实每一个实例用于给客户端回应的一个标准的HTTP响应
 * @author oyh
 *
 */
public class HttpResponse {
    /** 状态行相关信息定义 */
    private int statusCode = 200;

    /** 响应头相关信息定义 */
    Map<String, String> headers = new HashMap<String, String>();

    /** 响应正文相关信息定义 */
    private File entity;

    private Socket socket;
    private OutputStream out;

    public HttpResponse(Socket socket){
        try {
            this.socket = socket;
            this.out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 将当前响应对象内容响应给客户端 */
    public void flush(){
        //1发送状态行
        sendStatusLine();
        //2发送响应头
        sendHeaders();
        //3发送响应正文
        sendContent();
    }

    /**
     * 发送状态行
     */
    private void sendStatusLine(){
        String line = "HTTP/1.1 "+this.statusCode+" "+HttpContext.getStatusReason(this.statusCode);
        println(line);
        System.out.println("发送状态行:"+line);
    }

    /**
     * 发送响应头
     */
    private void sendHeaders(){
        Set<Map.Entry<String,String>> set =  headers.entrySet();
        for (Map.Entry<String, String> item: set){
            String line = item.getKey()+": "+item.getValue();
            println(line);
            System.out.println("发送响应头:"+line);
        }

        println("");
        System.out.println("响应头发送完毕!");
    }

    /**
     * 发送响应正文
     */
    private void sendContent(){
        if (this.entity != null){
            try {
                FileInputStream fis = new FileInputStream(entity);
                byte[] data = new byte[10*1024];
                int len = -1;
                while((len = fis.read(data)) != -1){
                    out.write(data,0,len);
                }
                System.out.println("响应正文完毕!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void println(String line){
        try {
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);
            out.write(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public File getEntity() {
        return entity;
    }

    public void setEntity(File entity) {
        if(entity != null){
            /*
             * 对应添加两个针对该形影正文的响应头
             * Content-Type:说明该文件类型
             * Content-Length:说明文件长度
             */
            headers.put("Content-Length",entity.length()+"");
            //设置Content-Type要根据文件后缀决定值是什么
            /*
             * 获取文件名的后缀
             * 思路：
             * 从文件名最后一个"."之后的第一个字符开始截取到末尾
             */
            String name = entity.getName();
            int index = name.lastIndexOf(".")+1;
            String ext = name.substring(index);
            String type = HttpContext.getMimeType(ext);
            headers.put("Content-Type", type);
        }
        this.entity = entity;
    }
}
