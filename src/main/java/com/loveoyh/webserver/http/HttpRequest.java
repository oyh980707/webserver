package com.loveoyh.webserver.http;

import com.loveoyh.webserver.http.ex.EmptyRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
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
    //url中的请求部分
    private String requestURI;
    //url中的参数部分
    private String queryString;
    //保存具体的每个参数
    private Map<String,String> parameters = new HashMap<String,String>();

    /** 消息头的相关信息 */
    private Map<String, String> headers = new HashMap<String, String>();

    /** 消息正文的相关内容 */


    private Socket socket;
    private InputStream in;

    public HttpRequest(Socket socket) throws EmptyRequestException{
        try {
            this.socket = socket;
            in = socket.getInputStream();
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
        method = data[0];
        url = data[1];
        //进一步解析url
        parseUrl();
        protocol = data[2];
        //输出测试
        System.out.println(method+" "+url+" "+protocol);

        System.out.println("解析请求行结束!");
    }

    /**
     * url可能存在两种情况
     * 1：不带参数
     * 2：带参数
     *
     * 如果不带参数，则直接将url赋值给requestURI即可，而queryString和parameter则无需操作。
     * 如果带参数，则需要进一步解析url，该url是否含有参数部分，可以根据url中是否含有"?"判别
     *
     * 如果含有参数，则现根据“？”将url拆分成两个部分
     * 第一部分是请求部分，赋值给requestURI
     * 第二部分是参数部分，赋值给queryString
     *
     * 并且还要对参数进行进一步解析
     * 将参数部分按照“&”进行拆分，可以得到每一个参数，再将每一个参数按照
     * “=”拆分成两部分，并且分别作为key，value保存到parameters这个map保存
     */
    private void parseUrl(){
        if(url.indexOf("?") != -1){
            String[] data = url.split("\\?");
            requestURI = data[0];
            System.out.println("URI:"+requestURI);
            if(data.length>1){
                this.queryString = data[1];
                //进一步按照"&"拆分参数
                parseParameters(queryString);
            }
        }else{
            requestURI = url;
        }
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
        /*
         * 判别当前请求是否含有消息正文可以分析消息头
         * 中是否含有Content-Length,Content-Type
         */
        if(headers.containsKey("Content-Length")){
            //存在消息正文,根据头获取正文长度
            int len = Integer.parseInt(headers.get("Content-Length"));
            try {
                byte[] data = new byte[len];
                in.read(data);//读取指定的字节量
                /*
                 * 具体读到的消息正文字节表示的是什么
                 * 要通过分析消息头Content-Type决定
                 */
                String type = headers.get("Content-Type");
                //判断是否为form表单提交的用户数据
                if("application/x-www-form-urlencoded".equals(type)){
                    String line = new String(data,"ISO8859-1");
                    System.out.println("内容："+line);
                    parseParameters(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("解析消息正文结束!");
    }

    /**
     * 解析参数
     * 参数的格式应当为：name=value&name=value....
     * @param line
     */
    private void parseParameters(String line){
        try {
            System.out.println("解析前:"+line);
            queryString = URLDecoder.decode(line, "UTF-8");
            System.out.println("解析后:"+queryString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] paras = queryString.split("&");
        for(String para : paras){
            //进一步按照"="拆分参数
            String[] arr = para.split("=");
            if(arr.length>1){
                parameters.put(arr[0],arr[1]);
            }else{
                parameters.put(arr[0],null);
            }
        }
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

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHeaders(String key) {
        return headers.get(key);
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameters(String key) {
        return parameters.get(key);
    }
}
