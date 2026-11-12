-- 滑动窗口限流
-- KEYS[1]: 限流key
-- ARGV[1]: 时间窗口大小（毫秒）
-- ARGV[2]: 窗口内最大请求次数
-- ARGV[3]: 当前请求标识

local key = KEYS[1]
local windowMillis = tonumber(ARGV[1])
local maxRequests = tonumber(ARGV[2])
local currentRequest = ARGV[3]

-- 兼容 Redis 3.2～6.x；Redis 7 默认使用 effect replication
if redis.replicate_commands then
    redis.replicate_commands()
end

-- 使用 Redis 服务器时间，避免应用节点时钟漂移
local redisTime = redis.call('TIME')
local nowMillis = tonumber(redisTime[1]) * 1000 + math.floor(tonumber(redisTime[2]) / 1000)

-- 窗口起始时间（毫秒）
local windowStart = nowMillis - windowMillis

-- 移除窗口外的无效记录
redis.call('ZREMRANGEBYSCORE', key, 0, windowStart)

-- 获取当前窗口内的请求数量
local currentCount = redis.call('ZCARD', key)

if currentCount < maxRequests then
    -- 允许请求，记录当前请求
    redis.call('ZADD', key, nowMillis, currentRequest)
    -- 设置过期时间（窗口毫秒数 + 1秒缓冲，转换为秒）
    local expireSeconds = math.ceil(windowMillis / 1000) + 1
    redis.call('EXPIRE', key, expireSeconds)
    return 1
else
    -- 拒绝请求
    return 0
end
