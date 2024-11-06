package com.rpc.server;

import com.rpc.service.ServiceProvider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 加强线程池版
 */
public class ThreadPoolRPCServer implements RPCServer{
    private final ThreadPoolExecutor threadPool;
    private ServiceProvider serviceProvider;

    //默认构造函数
    public ThreadPoolRPCServer(ServiceProvider serviceProvider) {
        threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                1000,60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100));
        this.serviceProvider = serviceProvider;
    }

    //自定义构造函数：函数里自己初始化ThreadPoolExecutor线程池，初始化serviceProvider
    public ThreadPoolRPCServer(ServiceProvider serviceProvider, int corePoolSize, int maximumPoolSize,
                               long keepAliveTime, TimeUnit unit, BlockingDeque<Runnable> workqueue){
        threadPool = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workqueue);
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void start(int port) {
        System.out.println("线程池服务器端启动了");
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            while (true){
                Socket socket = serverSocket.accept();
                threadPool.execute(new WorkThread(socket,serviceProvider));
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {

    }
}
