package ru.practicum.stat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик ошибок для контроллеров Spring.
 * Позволяет централизованно обрабатывать исключения и возвращать корректные ответы клиенту.
 */
@ControllerAdvice
public class ErrorHandler {

    /**
     * Обрабатывает исключения IllegalArgumentException, возникающие в приложении.
     * Возвращает ответ с кодом 400 (BAD_REQUEST) и сообщением об ошибке.
     * @param ex выброшенное исключение
     * @return тело ответа с описанием ошибки
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
