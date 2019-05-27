package com.pan.society.Common;

import com.yonyou.iuap.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springside.modules.nosql.redis.JedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class CacheUtil {
    private final Logger logger = LoggerFactory.getLogger(CacheUtil.class);

    private static CacheUtil cacheUtil = null;

    public static CacheUtil getInstance() {
        if (cacheUtil == null) {
            cacheUtil = new CacheUtil();
        }
        return cacheUtil;
    }

    private static ApplicationContext context;

    private static CacheManager cacheManager;

    private static JedisTemplate jedisTemplate;

    public CacheUtil() {
        if (context == null) {
            context = ContextLoader.getCurrentWebApplicationContext();
        }
        if (cacheManager == null) {
            cacheManager = context.getBean("cacheManager", CacheManager.class);
        }
        if (jedisTemplate == null) {
            jedisTemplate = context.getBean("jedisTemplate", JedisTemplate.class);
        }
    }

    /**
     * 包含指定值的所有key
     *
     * @param key
     * @return Set
     * @throws Exception
     */
    public Set<String> getKeys(String key) throws Exception {
        return cacheManager.keys(key);
    }

    /**
     * 缓存字符串对象到redis数据库
     *
     * @param key
     * @param value
     * @param time 秒
     * @return void
     * @throws Exception
     */
    public void setStr(String key, String value, int time) throws Exception {
        cacheManager.set(key, value);
        setExpire(key, time);
    }

    /**
     * 获取字符串对象
     *
     * @param key
     * @return String
     * @throws Exception
     */
    public String getStr(String key) throws Exception {
        if (cacheManager.get(key) != null) {
            return cacheManager.get(key);
        }
        return null;
    }

    /**
     * 缓存map对象到redis数据库
     *
     * @param key
     * @param map
     * @param time
     * @return void
     * @throws Exception
     */
    public void setMap(String key, Map<String, String> map, int time) throws Exception {
        for (String mapkey : map.keySet()) {
            cacheManager.hset(key, mapkey, map.get(mapkey));
        }
        setExpire(key, time);
    }

    /**
     * 缓存map中的一项到redis数据库
     *
     * @param key
     * @param mapkey
     * @param value
     * @param time
     * @return
     * @throws Exception
     */
    public void setMapStr(String key, String mapkey, String value, int time) throws Exception {
        cacheManager.hset(key, mapkey, value);
        setExpire(key, time);
    }

    /**
     * 获取map对象
     *
     * @param key
     * @return Map
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public Map getMap(String key) throws Exception {
        if (cacheManager.hgetAll(key) != null) {
            return cacheManager.hgetAll(key);
        }
        ;
        return null;
    }

    /**
     * 获取map对象中的某个值
     *
     * @param key
     * @param filedName
     * @return String
     * @throws Exception
     */
    public static String getMapStr(String key, String filedName) throws Exception {
        if (cacheManager.hget(key, filedName) != null) {
            return cacheManager.hget(key, filedName);
        }
        return null;
    }

    /**
     * 缓存pojo对象到redis数据库
     *
     * @param key
     * @param t
     * @param time
     * @return void
     * @throws Exception
     */
    public <T extends Serializable> void setPojo(String key, T t, int time) throws Exception {
        cacheManager.set(key, t);
        setExpire(key, time);
    }

    /**
     * 获取序列化pojo对象
     *
     * @param key
     * @return T
     * @throws Exception
     */
    public <T extends Serializable> T getPojo(String key) throws Exception {
        if (cacheManager.get(key) != null) {
            return cacheManager.get(key);
        }
        return null;
    }

    /**
     * 删除key的缓存
     *
     * @param key
     * @return void
     * @throws Exception
     */
    public void removeCache(String key) throws Exception {
        if (cacheManager.get(key) != null) {
            cacheManager.removeCache(key);
        }
    }

    /**
     * 删除map的key的缓存
     *
     * @param key
     * @return void
     * @throws Exception
     */
    public void removeMapCache(String key, String field) throws Exception {
        if (key != null && field != null) {
            cacheManager.hdel(key, field);
        }
    }

    /**
     * 设置key值过期时间
     *
     * @param key
     * @param time
     * @return void
     * @throws Exception
     */
    public void setExpire(String key, int time) throws Exception {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = jedisTemplate.getJedisPool().getResource();
            jedis.expire(key, time);
        } catch (JedisException var8) {
            broken = this.handleJedisException(var8);
            throw var8;
        } finally {
            this.closeResource(jedis, broken);
        }
    }

    private boolean handleJedisException(JedisException jedisException) {
        if (jedisException instanceof JedisConnectionException) {
            logger.error("Redis connection " + jedisTemplate.getJedisPool().getAddress() + " lost.", jedisException);
        } else if (jedisException instanceof JedisDataException) {
            if (jedisException.getMessage() == null || !jedisException.getMessage().contains("READONLY")) {
                return false;
            }

            logger.error("Redis connection " + jedisTemplate.getJedisPool().getAddress() + " are read-only slave.",
                    jedisException);
        } else {
            logger.error("Jedis exception happen.", jedisException);
        }

        return true;
    }

    private void closeResource(Jedis jedis, boolean conectionBroken) throws Exception {
        try {
            if (conectionBroken) {
                jedisTemplate.getJedisPool().returnBrokenResource(jedis);
            } else {
                jedisTemplate.getJedisPool().returnResource(jedis);
            }
        } catch (Exception var4) {
            logger.error("return back jedis failed, will fore close the jedis.", var4);
            if (jedis != null && jedis.isConnected()) {
                try {
                    try {
                        jedis.quit();
                    } catch (Exception var2) {
                        logger.error("closeResource jedis.quit error:", var4);
                        throw new Exception("closeResource jedis.quit error :" + var2.getMessage());
                    }

                    jedis.disconnect();
                } catch (Exception var3) {
                    logger.error("closeResource jedis.disconnect() error:", var3);
                    throw new Exception("closeResource jedis.disconnect() error:" + var3.getMessage());
                }
            }
        }
    }
}
