package ru.practicum.ewm.exception;

/**
 * Исключение, возникающее при конфликте данных или бизнес-логики.
 * Используется для обработки ситуаций, когда запрашиваемая операция
 * не может быть выполнена из-за конфликта с текущим состоянием данных.
 */
public class ConflictException extends RuntimeException {
    /**
     * Конструктор с сообщением об ошибке.
     *
     * @param message сообщение, описывающее причину конфликта
     */
    public ConflictException(String message) {
        super(message);
    }
}