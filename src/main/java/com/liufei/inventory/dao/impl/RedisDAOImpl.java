package com.liufei.inventory.dao.impl;

import javax.annotation.Resource;
import com.liufei.inventory.dao.RedisDAO;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisCluster;

@Repository
public class RedisDAOImpl implements RedisDAO {

	@Resource
	private JedisCluster jedisCluster;
	
	@Override
	public void set(String key, String value) {
		jedisCluster.set(key, value);
	}

	@Override
	public String get(String key) {
		return jedisCluster.get(key);
	}

	@Override
	public void delete(String productId) {
		jedisCluster.del(String.valueOf(productId));
	}

}
