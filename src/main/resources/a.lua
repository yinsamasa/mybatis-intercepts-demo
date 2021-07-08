local key = KEYS[1]
local number = tonumber(ARGV[1])


if number==0 then return 1;
else redis.call("set",key,number); return 0;
end

