package com.loveoyh.webserver.servlet;

import com.loveoyh.webserver.http.HttpRequest;
import com.loveoyh.webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * 注册业务
 */
public class RegServlet extends HttpServlet{
    public void service(HttpRequest request, HttpResponse response){
        /*
         * 注册流程
         * 1：通过request获取用户注册页面中表单提交的注册信息
         * 2：将这些信息写到文件user.dat中保存
         * 3：若保存成功则响应给客户注册成功的页面
         * 4：若在注册过程中出现了异常，响应错误页面
         */
        String username = request.getParameters("username");
        String password = request.getParameters("password");
        String nickname = request.getParameters("nickname");
        Integer age = Integer.parseInt(request.getParameters("age"));

        /*
         * 使用RandomAccessFile对user.dat文件进行写操作
         * 每个用户信息包含四项：
         * 用户名，密码，昵称，年龄
         * 除了年龄为int值之外，剩下的全部为String
         * 规划每条记录在user.dat文件中占用100字节
         * 其中用户名，密码，昵称，年龄个占用32，年龄为int值固定为4个字节
         */
        try {
            RandomAccessFile raf= new RandomAccessFile("user.dat","rw");
            raf.seek(raf.length());

            //写用户名
            byte[] data = username.getBytes("UTF-8");
            data = Arrays.copyOf(data,32);
            raf.write(data);

            //写密码
            data = password.getBytes("UTF-8");
            data = Arrays.copyOf(data, 32);
            raf.write(data);

            //写昵称
            data = nickname.getBytes("UTF-8");
            data = Arrays.copyOf(data, 32);
            raf.write(data);

            //写年龄
            raf.writeInt(age);

            forward("/myweb/reg_success.html",request,response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
