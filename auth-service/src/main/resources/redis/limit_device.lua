-- KEYS[1] = user_devices:{userId}
-- ARGV[1] = deviceId
-- ARGV[2] = now (timestamp)
-- ARGV[3] = limit (5)
-- ARGV[4] = userId

local zsetKey = KEYS[1]
local deviceId = ARGV[1]
local now = tonumber(ARGV[2])
local limit = tonumber(ARGV[3])
local userId = ARGV[4]
local keyRefreshToken = ARGV[5]


-- Nếu device đã tồn tại → update time
if redis.call("ZSCORE", zsetKey, deviceId) then
    redis.call("ZADD", zsetKey, now, deviceId)
    return "UPDATED"
end

-- số lượng thiết bị đang sử dụng
local size = redis.call("ZCARD", zsetKey)


-- kiểm tra giới hạn thiết bị
if size >= limit then
    local oldest = redis.call("ZRANGE", zsetKey, 0, 0)[1]
    if oldest then
        redis.call("ZREM", zsetKey, oldest)
        redis.call("DEL", keyRefreshToken .. userId .. ":" .. oldest)
    end
end

redis.call("ZADD", zsetKey, now, deviceId)
return "ADDED"