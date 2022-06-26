local n = tonumber(ARGV[1])
local surplus = tonumber(redis.call('HGET',KEYS[1],KEYS[2]))
local max = tonumber(redis.call('HGET',KEYS[1],KEYS[2] .. '@total'))

-- 要修改的步长为空，返回 -1
if n == nil then
   return -1
end

-- 11：总额度 或 剩余额度为空
if max == nil or surplus == nil then
   return 11
end
if ( surplus <= n ) then
   return 12
end

-- 要修改的步长为0 或 用户为无限额度(-1)，直接返回0 (成功)
if n == 0 or max == -1 then
   return 0
end

redis.call('HINCRBY',KEYS[1],KEYS[2],-n)
redis.call('EXPIRE',KEYS[1],43200)
redis.call('SADD',KEYS[3],'\"' .. KEYS[1] .. '\"')

if (surplus == 1001) then
   return 2000
end

if (surplus == 501) then
   return 1500
end

if (surplus == 101) then
   return 1100
end

return 0