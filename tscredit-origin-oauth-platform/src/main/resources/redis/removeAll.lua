local data = redis.call('SMEMBERS',KEYS[1])
redis.call('DEL',KEYS[1])
return data