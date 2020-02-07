package com.loveoyh.webserver.servlet;

import com.loveoyh.webserver.http.HttpRequest;
import com.loveoyh.webserver.http.HttpResponse;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * 登录功能
 */
public class LoginServlet extends HttpServlet{
    public void service(HttpRequest request, HttpResponse response){
        //
        String username = request.getParameters("username");
        String password = request.getParameters("password");

        try(RandomAccessFile raf = new RandomAccessFile("user.dat","r")) {
            //是否匹配到用户
            boolean have = false;

            for(int i=0;i<raf.length()/100;i++){
                //移动指针到当前记录的开始位置
                raf.seek(i*100);
                //读取用户信息
                byte[] data = new byte[32];
                raf.read(data);//一次读32个字节
                String user = new String(data,"UTF-8").trim();

                //比对是否哦为该用户
                if(user.equals(username)){
                    //找到该用户
                    raf.read(data);
                    String pwd = new String(data,"UTF-8").trim();
                    if(pwd.equals(password)){
                        //密码一致登陆成功
                        forward("/myweb/login_success.html",request,response);
                    }else{
                        //密码不一致
                        forward("/myweb/login_fail.html",request,response);
                    }
                    have = true;
                    //停止循环，无需再往下面读取数据
                    break;
                }
            }
            if(!have){
                //用户名输入无效
                forward("/myweb/login_fail.html",request,response);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
