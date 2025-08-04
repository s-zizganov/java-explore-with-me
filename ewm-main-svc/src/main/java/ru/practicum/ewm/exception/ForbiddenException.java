package ru.practicum.ewm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, возникающее при отсутствии прав доступа к ресурсу.
 * Автоматически возвращает HTTP статус 403 (FORBIDDEN).
 * Используется для обработки ситуаций, когда у пользователя
 * недостаточно прав для выполнения запрашиваемой операции.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
    /**
     * Конструктор с сообщением об ошибке.
     *
     * @param message сообщение, описывающее причину запрета доступа
     */
    public ForbiddenException(String message) {
        super(message);
    }
}