package com.sion.concertbooking.infrastructure.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtils {

    private static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");

    public static long toEpochMillis(LocalDateTime localDateTime) {
        return localDateTime.atZone(UTC_ZONE_ID).toInstant().toEpochMilli();
    }

    public static LocalDateTime fromEpochMillis(long epochMillis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), UTC_ZONE_ID);
    }
}
