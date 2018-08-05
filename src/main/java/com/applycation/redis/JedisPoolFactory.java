package com.applycation.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import static com.applycation.util.RedisConfig.*;

    public class JedisPoolFactory {
        private static JedisPool pool = null;

        public static JedisPool getInstance() {
            if(null == pool) {
                synchronized (JedisPoolFactory.class) {
                    if(null == pool ) {
                        JedisPoolConfig config = new JedisPoolConfig();
                        config.setMaxTotal(maxActive);
                        config.setMaxIdle(maxIdle);
                        config.setTestOnBorrow(false);
                        config.setTestWhileIdle(false);
                        if (null ==auth || auth.length() == 0){
                            pool = new JedisPool(config, server,port,timeout);
                        }else {
                            pool = new JedisPool(config, server,port,timeout, auth);
                        }
                    }
                }
            }
            return pool;
        }
    }
