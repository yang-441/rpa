package com.deepscience.rpa.rpc.api.live.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * LocalDateTime UTC时间序列化
 * @author yangzhuo
 * @date 2025/2/11 15:36
 */
public class LocalDateTimeUTCSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // 转换为时间戳（秒）
        long timestamp = value.toEpochSecond(ZoneOffset.UTC);
        gen.writeNumber(timestamp);
    }
}