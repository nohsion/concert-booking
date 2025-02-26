package com.sion.concertbooking.domain.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventOutboxUtils {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final ObjectMapper OBJECT_MAPPER = Jackson2ObjectMapperBuilder.json()
            .serializers(new LocalDateTimeSerializer(DATE_TIME_FORMATTER))
            .deserializers(new LocalDateTimeDeserializer(DATE_TIME_FORMATTER))
            .build();
}
