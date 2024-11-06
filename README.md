# MyRPCFromZero
pratice to implement rpc by Java

**参考资料：**
[原始文档链接](https://gitee.com/cjwwarren/MyRPCFromZero)
[全注释代码补充](https://blog.csdn.net/fisherish/article/details/122009738)

## MyRPCVersion0
在这个版本，实现一个简单的调用，client通过给定的一个id，使用RPC框架，从server拿到id对应的User对象信息。

1. 只能调用服务端Service唯一确定的方法，如果有两个方法需要调用呢？(Request需要抽象)
2. 返回值只支持User对象，如果需要传一个字符串或者Dog类，String对象呢(Response需要抽象)
3. 客户端不够通用，host，port，与调用的方法都特定(需要抽象)

## MyRPCVersion2
common新增blog实体
service新增BlogService和BlogServiceImpl以及ServiceProvider
server整体抽象重构
client没变，只是RPCclient多了一行调用代码

> 知识点：动态代理

## MyRPCVersion3
引入Netty优化，主要对client和server这两部分进行改造，common和service没有变化。

> 知识点：netty一些组件

## MyRPCVersion4
这里报了个错 invoke返回null  原因是在common的RPCResponse没有加@AllArgsConstructor这个注解
在这个版本 自定义了消息格式在codec里面
实现了ObjectSerializer与JsonSerializer两种序列化器
使用消息头加长度的方式解决粘包问题

> 知识点：修改序列化方式

## MyRPCVersion5
添加一个注册中心zookeeper
[【zookeeper】windows版zookeeper安装与启动 可能遇到的各种问题](https://blog.csdn.net/fisherish/article/details/118974827)

加入了注册中心，一个完整的RPC框架三个角色都有了：服务提供者，服务消费者，注册中心
缺点：根据服务名查询地址时，返回的总是第一个IP

> 知识点：注册中心zookeeper

## MyRPCVersion6
添加负载均衡
实现负载均衡的两种策略：随机与轮询
痛点: 客户端每次发起请求都要先与zookeeper进行通信得到地址，效率低下
