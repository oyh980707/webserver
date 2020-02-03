package com.loveoyh;

import java.io.*;
import java.net.Socket;
/**
 * 客户端，与服务端建立联系
 * @author Admin
 *
 */
public class Client {
    private Socket socket;

    public Client() {
        try {
            System.out.println("连接服务端...");
            socket = new Socket("localhost", 8080);
            System.out.println("连接服务器成功...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            String message = "Hello Server!";
            pw.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}
