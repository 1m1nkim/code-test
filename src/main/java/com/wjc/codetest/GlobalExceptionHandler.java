package com.wjc.codetest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice(value = {"com.wjc.codetest.product.controller"})
public class GlobalExceptionHandler {
/*
    문제 1: 500에러에 대한 Handler 밖에 존재하지 않음
    개선점 : 500 뿐 아니라 400, 404 등 추가적으로 많이 사용되는 오류 추가 개발

    문제 2: 여러 코드를 사용하여 중복되는 log 생성이 많아짐
    개선점 : 공통 log 처리하는 메소드 사용하여 유지보수 용이성 높힘
 */
    @ResponseBody
    @ExceptionHandler(RuntimeException.class)

    public ResponseEntity<String> runTimeException(Exception e) {
        logException(HttpStatus.INTERNAL_SERVER_ERROR, "runtimeException", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> illegalArgumentException(IllegalArgumentException e) {
        logException(HttpStatus.BAD_REQUEST, "IllegalArgumentException", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ResponseBody
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<String> noSuchElementException(NoSuchElementException e) {
        logException(HttpStatus.NOT_FOUND, "NoSuchElementException", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 중복 log 메소드 처리
    private void logException(HttpStatus httpStatus, String message, Exception e) {
        log.error("status :: {}, errorType :: {}, message :: {}",
                httpStatus,
                message,
                e.getMessage()
        );
    }
}
