for i=1, #ARGV
do
    redis.call('SETBIT',KEYS[1], ARGV[i], 1)
end