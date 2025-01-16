package com.sion.concertbooking.intefaces.presentation.accesslog;

public interface LogManager {

    void write(AccessLog accessLog, LogGroup logGroup);
}
