package com.wtc.backend.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler global de exceções — retorna respostas JSON padronizadas
 * em vez de páginas de erro HTML.
 *
 * Formato de erro padrão:
 * {
 *   "error": "mensagem",
 *   "status": 400,
 *   "timestamp": "2024-..."
 * }
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Erros de validação de Bean Validation (@NotBlank, @Email, etc.) */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(Map.of(
                "error", "Erro de validação",
                "fields", fieldErrors,
                "status", 400,
                "timestamp", Instant.now().toString()
        ));
    }

    /** Credenciais inválidas no login */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "error", "E-mail ou senha inválidos",
                "status", 401,
                "timestamp", Instant.now().toString()
        ));
    }

    /** Recursos não encontrados (RuntimeException genérica) */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        String message = ex.getMessage();
        boolean isNotFound = message != null && (
                message.contains("não encontrado") ||
                message.contains("não encontrada")
        );
        HttpStatus status = isNotFound ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(Map.of(
                "error", message != null ? message : "Erro interno",
                "status", status.value(),
                "timestamp", Instant.now().toString()
        ));
    }

    /** Arquivo de upload excede o limite configurado */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleMaxUploadSize(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(Map.of(
                "error", "Arquivo excede o tamanho máximo permitido (50MB)",
                "status", 413,
                "timestamp", Instant.now().toString()
        ));
    }

    /** Fallback para qualquer outra exceção */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Erro interno do servidor",
                "details", ex.getMessage() != null ? ex.getMessage() : "Sem detalhes",
                "status", 500,
                "timestamp", Instant.now().toString()
        ));
    }
}
