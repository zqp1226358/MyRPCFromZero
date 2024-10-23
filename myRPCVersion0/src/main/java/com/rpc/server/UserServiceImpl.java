package com.rpc.server;


import com.rpc.common.User;

import java.util.Random;
import java.util.UUID;

/*
lombok.Builder构造器构造方式详情可见：https://blog.csdn.net/weixin_41540822/article/details/86606562
构造格式：目标类.builder()...build()
比如 User.builder().id(id).build();
 * 则实际上是给User构造了：
 *         public User.UserBuilder id(int id) {
 *             this.id = id;
 *             return this;
 *         }
 */
public class UserServiceImpl implements UserService{
    @Override
    public User getUserByUserId(Integer id) {
        System.out.println("客户端查询了"+id+"的用户");
        // 模拟从数据库中取用户的行为
        Random random = new Random();
        User user = User.builder().userName(UUID.randomUUID().toString())
                .id(id)
                .sex(random.nextBoolean()).build();
        return user;
    }
}
