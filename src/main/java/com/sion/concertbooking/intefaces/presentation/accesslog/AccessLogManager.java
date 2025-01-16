package com.sion.concertbooking.intefaces.presentation.accesslog;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class AccessLogManager implements LogManager {

    private static final Logger RESERVATION_LOGGER = LoggerFactory.getLogger("RESERVATION_LOGGER");
    private static final Logger PAYMENT_LOGGER = LoggerFactory.getLogger("PAYMENT_LOGGER");
    private static final Logger LOGGER = LoggerFactory.getLogger("LOGGER");

    private static final Map<LogGroup, Logger> LOGGER_MAP = Map.of(
            LogGroup.RESERVATION, RESERVATION_LOGGER,
            LogGroup.PAYMENT, PAYMENT_LOGGER,
            LogGroup.DEFAULT, LOGGER
    );

    @Async("postProcessExecutor")
    @Override
    public void write(final AccessLog accessLog, final LogGroup logGroup) {
        Logger logger = LOGGER_MAP.getOrDefault(logGroup, LOGGER);
        logger.info("AccessLog: {}", accessLog);
    }
}
