package com.rpc.client;

import com.rpc.common.RPCResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

/**
 AttributeMap这是是绑定在Channel或者ChannelHandlerContext上的一个附件，ChannelHandlerContext中的AttributeMap是独有的，
 * Channel上的AttributeMap就是大家共享的，每一个ChannelHandler都能获取到。AttributeMap的结构，其实和Map的格式很像，
 * key是AttributeKey，value是Attribute，我们可以根据AttributeKey找到对应的Attribute

 */
public class NettyClientHandler extends SimpleChannelInboundHandler<RPCResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RPCResponse rpcResponse) throws Exception {
        // 接收到response, 给channel设计别名，让sendRequest里读取response
        // 默认情况下，第一次存放key值时，一般使用 AttributeKey.valueOf("rpcResponse")
        AttributeKey<RPCResponse> key = AttributeKey.valueOf("RPCResponse");
        channelHandlerContext.attr(key).set(rpcResponse);
        channelHandlerContext.channel().close();
    }

//     跟NettyRPCServerHandler一样
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
