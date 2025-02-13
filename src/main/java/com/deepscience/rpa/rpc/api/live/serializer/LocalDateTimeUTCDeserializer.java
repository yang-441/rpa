package com.deepscience.rpa.rpc.api.live.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * LocalDateTime UTC时间反序列化
 * @author yangzhuo
 * @date 2025/2/11 15:37
 */
public class LocalDateTimeUTCDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        long timestamp = p.getLongValue();
        // 从时间戳恢复 LocalDateTime
        return LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC);
    }
}
