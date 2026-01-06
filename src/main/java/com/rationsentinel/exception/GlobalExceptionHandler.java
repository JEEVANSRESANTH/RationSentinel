package com.rationsentinel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;
import jakarta.validation.ConstraintViolationException;



@RestControllerAdvice
public class GlobalExceptionHandler {
    // 🔴 Database constraint violation (duplicates, FK issues)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(
            DataIntegrityViolationException ex
    ) {
        ApiError error = new ApiError(
                HttpStatus.CONFLICT.value(),
                "Duplicate or invalid data constraint violation"
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // 🔴 Transaction / DB constraint wrapper
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ApiError> handleTransactionException(
            TransactionSystemException ex
    ) {
        ApiError error = new ApiError(
                HttpStatus.CONFLICT.value(),
                "Database transaction failed due to constraint violation"
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }



    // 🔴 Resource not found (FPS, User, Role, etc.)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(
            IllegalArgumentException ex
    ) {
        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 🔴 Business rule violation (duplicates, invalid state)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalState(
            IllegalStateException ex
    ) {
        ApiError error = new ApiError(
                HttpStatus.CONFLICT.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // 🔴 Fallback (unexpected bugs)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(
            Exception ex
    ) {
        ex.printStackTrace();
        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error"
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
