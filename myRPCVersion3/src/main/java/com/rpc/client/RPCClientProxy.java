package com.rpc.client;

import com.rpc.common.RPCRequest;
import com.rpc.common.RPCResponse;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@AllArgsConstructor
public class RPCClientProxy implements InvocationHandler {
    private RPCClient client;
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // request的构建，使用了lombok中的builder，代码简洁
        RPCRequest request = RPCRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args).paramsTypes(method.getParameterTypes())
                .build();
        // 数据传输
        RPCResponse response = client.sendRequest(request);
        //System.out.println(response);
        return response.getData();
    }
    <T>T getProxy(Class<T> clazz){
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T)o;
    }
}
