package com.reyco.dasbx.user.core.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Preconditions;

@Component
public class RedisBloomFilter {
	
	public final static String PUT = "for i=1,#ARGV do redis.call('SETBIT',KEYS[1], ARGV[i], 1) end";
	
	public final static String GET = "local values = table.getn(ARGV) for i=1, values do local value =  redis.call('GETBIT', KEYS[1], ARGV[i]) if value == 0 then return 0 end end return 1";
	
	@Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    public <T> void put(CustomBloomFilterHelper<T> bloomFilter, String key, T value) {
        Preconditions.checkArgument(bloomFilter != null, "bloomFilter不能为空");
        List<Long> offset = bloomFilter.murmurHashOffset(value);
        if (CollectionUtils.isEmpty(offset)) {
            return;
        }
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("PUT.lua")));
        redisScript.setResultType(Boolean.class);
        List<String> keys = new ArrayList<>();
        keys.add(key);
        redisTemplate.execute(redisScript, keys, offset.toArray());
    }

    public <T> void batchPut(CustomBloomFilterHelper<T> bloomFilter, String key, List<T> values) {
        Preconditions.checkArgument(bloomFilter != null, "bloomFilter不能为空");
        // 数据整合批量提交
        List<Long> offset = new ArrayList<>();
        for (T value : values) {
            offset.addAll(bloomFilter.murmurHashOffset(value));
        }
        if (CollectionUtils.isEmpty(offset)) {
            return;
        }
        Set<Long> set = new HashSet<>(offset);
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("PUT.lua")));
        redisScript.setResultType(Boolean.class);
        List<String> keys = new ArrayList<>();
        keys.add(key);
        redisTemplate.execute(redisScript, keys, set.toArray());
    }
    
    public <T> boolean mightContain(CustomBloomFilterHelper<T> bloomFilter, String key, T value) {
        Preconditions.checkArgument(bloomFilter != null, "bloomFilter不能为空");
        List<Long> offset = bloomFilter.murmurHashOffset(value);
        if (CollectionUtils.isEmpty(offset)) {
            return false;
        }
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("GET.lua")));
        redisScript.setResultType(Long.class);
        List<String> keys = new ArrayList<>();
        keys.add(key);
        Long result = redisTemplate.execute(redisScript, keys, offset.toArray());
        if(result == 1){
            return true;
        }
        return false;
    }
}
