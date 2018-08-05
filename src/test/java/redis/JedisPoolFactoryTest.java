package redis;

import com.applycation.redis.JedisPoolFactory;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisPoolFactoryTest {
    @Test
    public void get(){
        Jedis jedis = new Jedis("140.143.38.60",6379);
        jedis.set("xcl","chenglin");
    }
}
