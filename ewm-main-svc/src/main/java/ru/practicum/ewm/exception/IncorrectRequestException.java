package ru.practicum.ewm.exception;

/**
 * Исключение, возникающее при некорректном запросе.
 * Используется для обработки ситуаций, когда запрос содержит
 * некорректные параметры или не соответствует ожидаемому формату.
 */
public class IncorrectRequestException extends RuntimeException {
    /**
     * Конструктор с сообщением об ошибке.
     *
     * @param message сообщение, описывающее причину некорректного запроса
     */
    public IncorrectRequestException(String message) {
        super(message);
    }
}