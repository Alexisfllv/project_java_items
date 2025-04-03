package corporation.proyect.exception;

import corporation.proyect.exception.Errors.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Constantes para keys de respuesta
    private static final String MESSAGE = "message";
    private static final String CODE = "code";
    private static final String ERRORS = "errors";

    // ---- Manejo de excepciones personalizadas ----
    @ExceptionHandler(ExAccesoNoAutorizadoException.class)
    public ResponseEntity<Map<String, Object>> handleAccesoNoAutorizado(ExAccesoNoAutorizadoException ex) {
        HttpStatus status = ex.getMessage().contains("Forbidden") ? HttpStatus.FORBIDDEN : HttpStatus.UNAUTHORIZED;
        return buildResponse(ex.getMessage(), status);
    }

    @ExceptionHandler(ExDataNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDataNotFound(ExDataNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExInvalidDataException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidData(ExInvalidDataException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExIncorrectJsonException.class)
    public ResponseEntity<Map<String, Object>> handleIncorrectJson(ExIncorrectJsonException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExDatabaseErrorException.class)
    public ResponseEntity<Map<String, Object>> handleDatabaseError(ExDatabaseErrorException ex) {
        return buildResponse("Database error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ---- Manejo de excepciones de Spring ----
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleMalformedJson(HttpMessageNotReadableException ex) {
        return buildResponse("Invalid JSON format", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage() != null ?
                                fieldError.getDefaultMessage() : "Invalid value"
                ));

        Map<String, Object> response = new HashMap<>();
        response.put(MESSAGE, "Validation error");
        response.put(CODE, HttpStatus.BAD_REQUEST.value());
        response.put(ERRORS, errors);
        return ResponseEntity.badRequest().body(response);
    }

    // ---- Último recurso (Error genérico) ----
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Metodo helper para respuestas consistentes
    private ResponseEntity<Map<String, Object>> buildResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put(MESSAGE, message);
        response.put(CODE, status.value());
        return ResponseEntity.status(status).body(response);
    }
}
