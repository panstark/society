package com.pan.society.service.model.redis;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/6/24
 */
public class RedisClient {

    private static ShardedJedisPool pool;
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(5);
        config.setMaxIdle(1);
        config.setMaxWaitMillis(3000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        // 集群
        JedisShardInfo jedisShardInfo1 = new JedisShardInfo("10.21.33.74", 6379);
        jedisShardInfo1.setPassword("Occ2019");
        List<JedisShardInfo> list = new LinkedList<JedisShardInfo>();
        list.add(jedisShardInfo1);
        pool = new ShardedJedisPool(config, list);
    }

    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();
        String keys = "myname";
        String vaule = jedis.set(keys, "lxr");
        System.out.println(vaule);

        Map<String, String> userName = jedis.hgetAll("UserName");

        System.out.println(userName.size());

    }



}
