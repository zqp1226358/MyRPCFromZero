package com.rpc.client;

import com.rpc.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * RPC客户端：调用服务器端的方法
 * 客户端建立socket连接，标定主机ip地址，指定程序使用的端口号
 *
 * Socket.getInputStream()：方法得到一个输入流，客户端的Socket对象上的getInputStream()方法
 *                          得到的输入流其实就是从服务器端发回的数据流。
 * Socket.GetOutputStream()：方法得到一个输出流，客户端Socket对象上的getOutputStream()方法
 *                           返回的输出流,就是将要发送到服务器端的数据流（其实是一个缓冲区，暂时存储将要发送过去的数据）。
 * java.io.ObjectOutputStream.flush()：此方法刷新流。这将写入所有缓冲的输出字节并刷新到基础流。
 * java.io.ObjectInputStream.readObject()：方法从ObjectInputStream中读取对象。读取该对象的类，类签名，类及其所有超类型的
 *                                         非瞬态和非静态字段的值。默认的反序列化的类可以使用writeObject和readObject方法被重写。
 *                                         由此对象引用的对象被传递地读，这样对象的完全等价的图形是由readObject重建。
 */
public class MyRPCClient {
    public static void main(String[] args) {
        try {
            // 建立Socket连接
            Socket socket = new Socket("127.0.0.1",8899);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // 传给服务器 要查询的id
            objectOutputStream.writeInt(new Random().nextInt());
            objectOutputStream.flush();
            // 服务器查询数据，返回对应的对象
            User user = (User) objectInputStream.readObject();
            System.out.println("服务端返回的User:"+user);
        }  catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("客户端启动失败");
        }
    }
}
