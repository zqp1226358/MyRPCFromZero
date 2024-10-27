# MyRPCFromZero
pratice to implement rpc by Java
参考资料：
[原始文档链接](https://gitee.com/cjwwarren/MyRPCFromZero)
[全注释代码补充](https://blog.csdn.net/fisherish/article/details/122009738)

## MyRPCVersion0
在这个版本，实现一个简单的调用，client通过给定的一个id，使用RPC框架，从server拿到id对应的User对象信息。

1. 只能调用服务端Service唯一确定的方法，如果有两个方法需要调用呢？(Request需要抽象)
2. 返回值只支持User对象，如果需要传一个字符串或者Dog类，String对象呢(Response需要抽象)
3. 客户端不够通用，host，port，与调用的方法都特定(需要抽象)

## MyRPCVersion1
common新增blog实体
service新增BlogService和BlogServiceImpl以及ServiceProvider
server整体抽象重构
client没变，只是RPCclient多了一行调用代码
