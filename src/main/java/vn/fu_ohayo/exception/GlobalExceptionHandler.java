package vn.fu_ohayo.exception;

import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.fu_ohayo.dto.response.ApiErrorResponse;
import vn.fu_ohayo.enums.ErrorEnum;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(AppException exception) {
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleArgumentNotValidationException(MethodArgumentNotValidException exception) {

        // Extract field validation errors
        List<FieldValidateException> errors = new java.util.ArrayList<>(exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> new FieldValidateException(e.getField(), e.getDefaultMessage()))
                .toList());


        // Add global errors to the list
        var globalErrors = exception.getBindingResult()
                .getGlobalErrors()
                .stream()
                .map(e -> new FieldValidateException(e.getObjectName(), e.getDefaultMessage()))
                .toList();
        errors.addAll(globalErrors);

        ApiErrorResponse errorResponse = ApiErrorResponse.<FieldValidateException>builder()
                .code(ErrorEnum.INVALID_FIELDS.getCode())
                .message(ErrorEnum.INVALID_FIELDS.getMessage())
                .data(errors)
                .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
