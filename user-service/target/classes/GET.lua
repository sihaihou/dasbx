local values = table.getn(ARGV)
for i=1, values
do
    local value =  redis.call('GETBIT', KEYS[1], ARGV[i]) 
    if value == 0
    then return 0
    end
end
return 1
