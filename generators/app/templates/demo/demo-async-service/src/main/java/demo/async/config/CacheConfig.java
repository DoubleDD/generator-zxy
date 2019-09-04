package <%= package %>.<%= project %>.async.config;

import com.zxy.common.cache.CacheService;
import com.zxy.common.cache.redis.ClusterRedis;
import com.zxy.common.cache.redis.Redis;
import com.zxy.common.cache.redis.RedisCacheService;
import com.zxy.common.cache.redis.SingleRedis;
import com.zxy.common.serialize.Serializer;
import com.zxy.common.serialize.hessian.HessianSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author <%= user %>
 *
 */
@Configuration
public class CacheConfig {

    @Bean
    public Serializer serializer() {
        return new HessianSerializer();
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig(Environment env){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(env.getProperty("spring.jedis.max-total", Integer.class));
        jedisPoolConfig.setMaxIdle(env.getProperty("spring.jedis.max-idle", Integer.class));
        jedisPoolConfig.setBlockWhenExhausted(env.getProperty("spring.jedis.block-when-exhausted", Boolean.class));
        jedisPoolConfig.setMaxWaitMillis(env.getProperty("spring.jedis.max-wait-millis", Long.class));
        return jedisPoolConfig;
    }

    @Bean
    public Redis redis(Environment env, JedisPoolConfig jedisPoolConfig) {
        boolean isCluster = env.getProperty("spring.redis.cluster", Boolean.class, false);
        if(isCluster) {
            ClusterRedis clusterRedis = new ClusterRedis();
            clusterRedis.setJedisCluster(new JedisCluster(
                    Arrays.stream(env.getProperty("spring.redis.cluster.nodes").split(",")).map(node -> {
                        return new HostAndPort(node.split(":")[0], Integer.parseInt(node.split(":")[1]));
                    }).collect(Collectors.toSet()),
                    env.getProperty("spring.redis.connection-timeout", Integer.class),
                    env.getProperty("spring.redis.timeout", Integer.class),
                    env.getProperty("spring.redis.max-attempts", Integer.class),
                    env.getProperty("spring.redis.password", String.class),
                    jedisPoolConfig)
            );
            return clusterRedis;
        } else {
            SingleRedis singleRedis = new SingleRedis();
            String hostAndPort = env.getProperty("spring.redis.cluster.nodes").split(",")[0];
            singleRedis.setJedisPool(new JedisPool(
                    jedisPoolConfig,
                    hostAndPort.split(":")[0],
                    Integer.parseInt(hostAndPort.split(":")[1]),
                    env.getProperty("spring.redis.timeout", Integer.class),
                    env.getProperty("spring.redis.password", String.class)));
            return singleRedis;
        }
    }

    @Bean
    public CacheService cacheService(Environment env, Serializer serializer, Redis redis){
        RedisCacheService redisCacheService =  new RedisCacheService(env.getProperty("spring.application.name"));
        redisCacheService.setSerializer(serializer);
        redisCacheService.setRedis(redis);
        return redisCacheService;
    }
}
