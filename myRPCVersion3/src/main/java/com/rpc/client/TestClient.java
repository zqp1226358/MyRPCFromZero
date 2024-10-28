package com.rpc.client;

import com.rpc.common.Blog;
import com.rpc.common.User;
import com.rpc.service.BlogService;
import com.rpc.service.UserService;

public class TestClient {
    public static void main(String[] args) {
        RPCClient rpcClient = new NettyRPCClient("127.0.0.1", 8899);
        RPCClientProxy rpcClientProxy = new RPCClientProxy(rpcClient);
        UserService proxy = rpcClientProxy.getProxy(UserService.class);
        // 服务的方法1
        User userByUserId = proxy.getUserByUserId(10);
        System.out.println("从服务端得到的user为：" + userByUserId);
        // 服务的方法2
        User user = User.builder().userName("张三").id(100).sex(true).build();
        Integer integer = proxy.insertUserId(user);
        System.out.println("向服务端插入数据："+integer);
        // my2新增 服务的方法3
        BlogService blogService = rpcClientProxy.getProxy(BlogService.class);
        Blog blogById = blogService.getBlogById(10000);
        System.out.println("从服务端得到的blog为：" + blogById);
    }
}
