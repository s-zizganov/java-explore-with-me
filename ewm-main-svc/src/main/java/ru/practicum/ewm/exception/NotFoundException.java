package ru.practicum.ewm.exception;

/**
 * Исключение, возникающее при попытке доступа к несуществующему ресурсу.
 * Используется для обработки ситуаций, когда запрашиваемый объект
 * (пользователь, событие, категория и т.д.) не найден в системе.
 */
public class NotFoundException extends RuntimeException {
    /**
     * Конструктор с сообщением об ошибке.
     *
     * @param message сообщение, описывающее что именно не найдено
     */
    public NotFoundException(String message) {
        super(message);
    }
}