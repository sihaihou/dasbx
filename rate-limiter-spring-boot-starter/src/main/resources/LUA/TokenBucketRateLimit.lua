-- 令牌桶限流
-- KEYS[1]: 限流key
-- ARGV[1]: 最大容量
-- ARGV[2]: 每周期生成的令牌数
-- ARGV[3]: 周期时长（毫秒）

local key = KEYS[1]
local capacity = tonumber(ARGV[1])
local rate = tonumber(ARGV[2])
local periodMillis = tonumber(ARGV[3])

-- 兼容 Redis 3.2～6.x；Redis 7 默认使用 effect replication
if redis.replicate_commands then
    redis.replicate_commands()
end

-- 使用 Redis 服务器时间，避免应用节点时钟漂移
local redisTime = redis.call('TIME')
local nowMillis = tonumber(redisTime[1]) * 1000 + math.floor(tonumber(redisTime[2]) / 1000)

-- 计算每毫秒填充速率
local fill_rate = rate / periodMillis

-- 缓存过期时间，最少 60 秒
local ttl_seconds = math.max(60, math.ceil(periodMillis / 1000) * 2)

-- 获取当前桶状态
local tokens = redis.call('HGET', key, 'tokens')
local lastTime = redis.call('HGET', key, 'last_time')

-- 桶不存在，初始化满桶，消耗一个令牌
if tokens == false or lastTime == false then
    local initial_tokens = capacity - 1
    if initial_tokens >= 0 then
        redis.call('HSET', key, 'tokens', initial_tokens)
        redis.call('HSET', key, 'last_time', nowMillis)
        redis.call('EXPIRE', key, ttl_seconds)
        return 1
    else
        return 0
    end
end

tokens = tonumber(tokens)
lastTime = tonumber(lastTime)

-- 计算新增令牌数
local elapsed_millis = math.max(0, nowMillis - lastTime)
local tokens_to_add = elapsed_millis * fill_rate
local new_tokens = math.min(capacity, tokens + tokens_to_add)

-- 尝试获取令牌
if new_tokens >= 1 then
    local remaining_tokens = new_tokens - 1
    redis.call('HSET', key, 'tokens', remaining_tokens)
    redis.call('HSET', key, 'last_time', nowMillis)
    redis.call('EXPIRE', key, ttl_seconds)
    return 1
else
    -- 令牌不足
    redis.call('HSET', key, 'tokens', new_tokens)
    redis.call('HSET', key, 'last_time', nowMillis)
    redis.call('EXPIRE', key, ttl_seconds)
    return 0
end
