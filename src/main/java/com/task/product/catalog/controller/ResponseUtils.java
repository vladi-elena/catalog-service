package com.task.product.catalog.controller;

import com.task.product.catalog.representation.ErrorResponseDto;
import com.task.product.catalog.service.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ResponseUtils {

    public static <T> ResponseEntity<T> createResponse(final T body) {
        if (isEmpty(body)) {
            throw new NotFoundException("NOT FOUND");
        }
        return ResponseEntity.ok(body);
    }

    private static boolean isEmpty(final Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof Collection) {
            return CollectionUtils.isEmpty((Collection<?>) object);
        }

        return false;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity notFoundExceptionHandler(final NotFoundException exception, WebRequest request) {
        logException(exception, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), exception.getMessage(), Instant.now())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity validationExceptionHandler(final MethodArgumentNotValidException exception, WebRequest request) {
        logException(exception, request);
        List<String> errors = exception.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), errors.toString(), Instant.now())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionHandler(final NotFoundException exception, WebRequest request) {
        logException(exception, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), Instant.now())
        );
    }

    private void logException(Exception exception, WebRequest request) {
        String requestURL = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
        log.error("Exception in request: {}, message: {}", requestURL, exception.getMessage());
    }
}
