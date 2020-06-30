package com.pan.society.service.model.redis;

import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/6/24
 */
public class RedisDB {

    private static Jedis jedis = null;

    static {
        String host  = "10.21.33.74";
        int port = 6379;
        try {
            jedis = new Jedis(host,port);
            jedis.auth("Occ2019");
            jedis.select(1);
            jedis.set("name","kun");
            String name = jedis.get("name");
            System.out.println("name = " + name);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (null != jedis){
                try {
                    jedis.close();
                }catch (Exception e){
                    System.out.println("redis连接关闭失败");
                    e.printStackTrace();
                }
            }
        }
    }



    public static void main(String[] args) {
        Set<String> userName = jedis.keys("UserName*");
        userName.forEach(x-> System.out.println(jedis.get(x)));

    }

}
