package com.rpc.client;

import com.rpc.common.RPCRequest;
import com.rpc.common.RPCResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

/**
 https://blog.csdn.net/W664160450/article/details/123403397
 netty业务主要分两部分：启动类模版代码 + 自定义业务Handler
 bootstrap客户端启动类的主要流程：
 1.制定线程模型：.group(group)
 2.指定IO模型: .channel(NioServerSocketChannel.class)来指定NIO模型，也可以指定BIO模型
 3.制定处理逻辑器: childHandler(),创建一个 ChannelInitializer，这里主要就是管理自定义 Handler
 4.连接服务端: 调用 connect () 连接服务端，需要传递两个参数，分别是服务端的 IP 地址和端口号。

 在 Netty 中所有的 IO 操作都是异步的，不能立刻得到 IO 操作的执行结果，
 但是可以通过注册一个监听器来监听其执行结果。在 Java 的并发编程当中可以通过 Future 来进行异步结果的监听，
 但是在 Netty 当中是通过 ChannelFuture 来实现异步结果的监听。
 通过注册一个监听的方式进行监听，当操作执行成功或者失败时监听会自动触发注册的监听事件。

 在 Netty 当中 Bind 、Write 、Connect 等操作会简单的返回一个 ChannelFuture

 sync () 和 await () 都是等待异步操作执行完成，那么它们有什么区别呢？
 1. sync () 会抛出异常，建议使用 sync ()；
 2. await () 不会抛出异常，主线程无法捕捉子线程执行抛出的异常。
 */
public class NettyRPCClient implements RPCClient{
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup evenLoopGroup;
    private String host;
    private int port;
    // 构造函数
    public NettyRPCClient(String host,int port){
        this.host = host;
        this.port = port;
    }
    // netty客户端初始化，重复使用
    static {
        evenLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(evenLoopGroup).channel(NioSocketChannel.class).handler(new NettyClientInitializer());
    }
    // 这里需要操作一下，因为netty的传输都是异步的，你发送request，会立刻返回一个值， 而不是想要的相应的response
    @Override
    public RPCResponse sendRequest(RPCRequest request) {
        try{
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            // 发送数据
            channel.writeAndFlush(request);
            channel.closeFuture().sync();
            // 阻塞的获得结果，通过给channel设计别名，获取特定名字下的channel中的内容（这个在hanlder中设置）
            // AttributeKey是，线程隔离的，不会有线程安全问题。
            // 实际上不应通过阻塞，可通过回调函数
            // 通过这种方式获得全局可见的返回结果
            AttributeKey<RPCResponse> key = AttributeKey.valueOf("RPCResponse");
            RPCResponse response = channel.attr(key).get();

            System.out.println(response);
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
