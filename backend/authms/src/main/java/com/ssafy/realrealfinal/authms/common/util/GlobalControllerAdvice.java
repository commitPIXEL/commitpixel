package com.ssafy.realrealfinal.authms.common.util;

import com.ssafy.realrealfinal.authms.common.model.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception Handler
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> onBaseException(BaseException e) {
        log.info("GlobalControllerAdvice_onBaseException: codeName = " + e.getErrorCode().name()
            + " codeMessage = " + e.getMessage());

        return new ResponseEntity<>(e.getErrorCode().getMessage(),
            e.getErrorCode().getHttpStatus());
    }
}
