package com.loveoyh.webserver.http;

import com.loveoyh.webserver.http.ex.EmptyRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 该类的每一个实例用于表示一个客户端发送过来的HTTP请求内容
 * 一个请求有三部分：请求行，消息头，消息正文
 * @author oyh
 *
 */
public class HttpRequest {
    /** 请求行相关的信息 */
    //请求方式
    private String method;
    //请求的资源路径
    private String url;
    //请求使用的协议版本
    private String protocol;

    /** 消息头的相关信息 */
    private Map<String, String> headers = new HashMap<String, String>();

    /** 消息正文的相关内容 */


    private Socket socket;
    private InputStream in;

    public HttpRequest(Socket socket) throws EmptyRequestException{
        try {
            this.socket = socket;
            this.in = socket.getInputStream();
            /*
             * 1.解析请求行
             * 2.解析消息头
             * 3.解析消息正文
             */
            //1
            parseRequestLine();
            //2
            parseHeaders();
            //3
            parseContent();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 解析请求行
     * 1.通过输入流读取一行字符串（一个请求中的第一行内容就是请求行内容）
     *  GET /index.html HTTP/1.1
     * 2.按照空格将请求行拆分为三部分
     * 3.将拆分后的内容分别设置到对应的属性：method url protocol
     *  完成了解析请求操作
     */
    private void parseRequestLine(){
        System.out.println("开始解析请求行...");

        String line = readLine();
        /*
         * 这里有可能会出现下标越界的情况:
         * HTTP协议允许客户端发送空请求（连接后实际没有发送一个标准的HTTP请求）
         * 这时若连接解析请求行读取到的的是一个空字符串，那么拆分后得不到三项内容。
         * 针对空指针请求我们就不做任何处理了。
         */
        String[] data = line.split("\\s");
        if(data.length < 3){
            throw new EmptyRequestException("空请求!!!");
        }
        this.method = data[0];
        this.url = data[1];
        this.protocol = data[2];
        //输出测试
        System.out.println(this.method+" "+this.url+" "+this.protocol);

        System.out.println("解析请求行结束!");
    }

    /**
     * 解析消息头
     * 1.循环调用readLine方法读取若干行，由于parseRequest方法已经从流中读取了请求中的第一行内容（请求行），
     *  那么在这里使用readLine方法读取的就应当消息头部分了
     * 2.将每个消息头读取后，按照": "拆分成两部分，第一部分应当是消息头的名字，第二部分
     *  应当是消息头对应的值，将他们put到headers这个map中即可完成解析消息头工作
     * 3.当调用readLine方法返回的是一个空字符串时，表示单独读取到了CRLF，那么直接break循环，
     *  停止解析消息头部分即可
     */
    private void parseHeaders(){
        System.out.println("开始解析消息头...");

        String line = null;
        while(true){
            line = readLine();
            if("".equals(line)){
                break;
            }
            String[] data = line.split(":\\s");
            headers.put(data[0],data[1]);
        }
        //输出测试
        Set<Map.Entry<String, String>> setEntry = headers.entrySet();
        for (Map.Entry<String, String> item : setEntry) {
            System.out.println(item.getKey()+": "+item.getValue());
        }

        System.out.println("解析消息头结束!");
    }

    /**
     * 解析消息正文
     */
    private void parseContent(){
        System.out.println("开始解析消息正文...");

        System.out.println("解析消息正文结束!");
    }

    /**
     * 通过输入流读取一行字符串，以CRLF结尾为一行字符串。
     * 返回的字符串中不含有最后的CRLF. CR(13) LF(10)
     * @return 读取一行的内容
     */
    private String readLine(){
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
