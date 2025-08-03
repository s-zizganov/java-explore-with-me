package ru.practicum.ewm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, возникающее при ошибках валидации данных.
 * Автоматически возвращает HTTP статус 400 (BAD_REQUEST).
 * Используется для обработки ситуаций, когда переданные данные
 * не соответствуют бизнес-правилам или ограничениям валидации.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {
    /**
     * Конструктор с сообщением об ошибке.
     *
     * @param message сообщение, описывающее причину ошибки валидации
     */
    public ValidationException(String message) {
        super(message);
    }
}