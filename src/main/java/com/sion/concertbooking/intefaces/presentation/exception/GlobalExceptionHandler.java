package com.sion.concertbooking.intefaces.presentation.exception;

import com.sion.concertbooking.intefaces.presentation.response.ApiErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.naming.AuthenticationException;
import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            BindException.class,
            NoSuchElementException.class
    })
    public ApiErrorResponse badRequest(Exception e) {
        log.info("[BadRequest] {}", e.getMessage(), e);
        return new ApiErrorResponse(e.getMessage());
    }

    /**
     * AOP에서 발생한 AuthenticationException을 여기서 처리하려면, 컨트롤러 메소드에서 throws로 명시적으로 선언해야 한다.
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ApiErrorResponse handleAuthenticationException(AuthenticationException e) {
        log.info("[AuthenticationException] {}", e.getMessage(), e);
        return new ApiErrorResponse(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiErrorResponse handleException(Exception e) {
        log.error("[UnhandledException] {}", e.getMessage(), e);
        return new ApiErrorResponse("관리자에게 문의해주세요.");
    }
}
