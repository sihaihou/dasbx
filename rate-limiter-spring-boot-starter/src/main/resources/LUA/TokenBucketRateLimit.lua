-- 令牌桶限流
-- KEYS[1]: 限流key
-- ARGV[1]: 最大容量
-- ARGV[2]: 每周期生成的令牌数
-- ARGV[3]: 周期时长（秒）
-- ARGV[4]: 当前时间戳（秒）

local key = KEYS[1]
local capacity = tonumber(ARGV[1])
local rate = tonumber(ARGV[2])
local periodSeconds = tonumber(ARGV[3])
local now = tonumber(ARGV[4])

-- 计算每秒填充速率
local fill_rate = rate / periodSeconds

-- 获取当前桶状态
local tokens = redis.call('hget', key, 'tokens')
local last_time = redis.call('hget', key, 'last_time')

if tokens == false then
    -- 桶不存在，初始化满桶，消耗一个令牌
    local initial_tokens = capacity - 1
    if initial_tokens >= 0 then
        redis.call('hset', key, 'tokens', initial_tokens)
        redis.call('hset', key, 'last_time', now)
        redis.call('expire', key, math.max(60, math.ceil(periodSeconds * 2)))
        return 1
    else
        return 0
    end
end

tokens = tonumber(tokens)
last_time = tonumber(last_time)

-- 计算新增令牌数
local time_passed = math.max(0, now - last_time)
local tokens_to_add = time_passed * fill_rate
local new_tokens = math.min(capacity, tokens + tokens_to_add)

-- 尝试获取令牌
if new_tokens >= 1 then
    local remaining_tokens = new_tokens - 1
    redis.call('hset', key, 'tokens', remaining_tokens)
    redis.call('hset', key, 'last_time', now)
    redis.call('expire', key, math.max(60, math.ceil(periodSeconds * 2)))
    return 1
else
    -- 令牌不足
    redis.call('hset', key, 'tokens', new_tokens)
    redis.call('hset', key, 'last_time', now)
    redis.call('expire', key, math.max(60, math.ceil(periodSeconds * 2)))
    return 0
end