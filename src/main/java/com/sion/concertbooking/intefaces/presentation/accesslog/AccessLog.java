package com.sion.concertbooking.intefaces.presentation.accesslog;

import java.time.ZonedDateTime;

public record AccessLog(
        String clientIp,
        String serverIp,
        ZonedDateTime time,
        String requestMethod,
        String requestUri,
        String requestProtocol,
        String referer,
        String userAgent,
        int requestBodySize,
        long startTime,
        long endTime,
        long elapsedTime
) {
}
