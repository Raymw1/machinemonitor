package com.api.machinemonitor.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableCaching
public class RedisConfig {
	
	@Autowired
	CacheManager cacheManager;
	
	@Value("${spring.data.redis.host}")
	private String redisHost;
	
	@Value("${spring.data.redis.port}")
	private int redisPort;

//	@Bean
//	public RedisTemplate<String, Serializable> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
//		RedisTemplate<String, Serializable> template = new RedisTemplate<>();
//		template.setKeySerializer(new StringRedisSerializer());
//		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//		template.setConnectionFactory(redisConnectionFactory);
//		return template;
//	}
//	
//	@Bean
//	public CacheManager cacheManager(RedisConnectionFactory factory) {
//		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
//		RedisCacheConfiguration redisCacheConfiguration = config
//			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
//		RedisCacheManager redisCacheManager = RedisCacheManager.builder(factory).cacheDefaults(redisCacheConfiguration).build();
//		return redisCacheManager;
//	}
//	
//	@PostConstruct
//	public void clearCache() {
//		Jedis jedis = new Jedis(redisHost, redisPort, 5);
//		jedis.flushAll();
//		jedis.close();
//	}
	
}
