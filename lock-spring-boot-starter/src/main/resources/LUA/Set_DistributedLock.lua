if redis.call('setnx', KEYS[1],ARGV[1]) == 1 then 
    return redis.call('EXPIRE',KEYS[1],ARGV[2]) 
else 
    return 0 
end