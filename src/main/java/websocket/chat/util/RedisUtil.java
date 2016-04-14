package websocket.chat.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool.impl.GenericObjectPool.Config;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

public class RedisUtil {

    private static ShardedJedisPool pool = null;

    public static ShardedJedisPool getRedisPool() {
        if (pool == null) {
            List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
            JedisShardInfo si = new JedisShardInfo("120.25.204.152", 6379);
            si.setPassword("123456");
            shards.add(si);
            pool = new ShardedJedisPool(new Config(), shards);
        }
        return pool;
    }

}
