-- 改文件未被启动，如果启用 请注意 `LIANGXI:`  需要替换成变量


local tt = 0
for i,key in ipairs(KEYS) do
    for j,value in ipairs(ARGV) do
        local max = tonumber(redis.call('HGET','LIANGXI:quota:' .. key,value .. '@total'))
        redis.call('HSET','LIANGXI:quota:' .. key,value,50)
        tt = tt + 1
    end
end

return '\"' .. tt .. '\"'