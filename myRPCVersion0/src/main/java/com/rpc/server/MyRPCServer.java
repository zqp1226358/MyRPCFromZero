package com.rpc.server;

import com.rpc.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * RPC服务器端:
 * 服务器端：创建ServerSocket对象监听客户端的连接（BIO），监听到连接之后，开启一个线程来处理，socket对象的获取输入
 *
 * ServerSocket：用于服务器端，监听客户端连接
 * ServerSocket.accept()：是一个阻塞方法，方法从连接请求队列中取出一个客户的连接请求，然后创建与客户连接的Socket对象，
 *                       并将它返回。如果队列中没有连接请求，accept()方法就会一直等待，直到接收到了连接请求才返回。
 * java.io.ObjectInputStream.readInt()：方法读取一个32位的int
 * java.io.ObjectOutputStream.writeObject(Object obj): 此方法将指定的对象写入ObjectOutputStream。该对象的类，类的签名，
 *                                                     以及类及其所有超类型的非瞬态和非静态字段的值被写入。
 */
public class MyRPCServer {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        try{
            ServerSocket serverSocket = new ServerSocket(8899);
            System.out.println("服务端启动");
            // BIO的方式监听Socket
            while (true){
                Socket socket = serverSocket.accept();
                // 开启一个线程去处理
                new Thread(()->{
                    try{
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        // 读取客户端传过来的id
                        Integer id = objectInputStream.readInt();
                        User userByUserId = userService.getUserByUserId(id);
                        // 写入User对象给客户端
                        objectOutputStream.writeObject(userByUserId);
                        objectOutputStream.flush();
                    }catch (IOException e){
                        e.printStackTrace();
                        System.out.println("从IO中读取数据错误");
                    }
                }).start();
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }
}
