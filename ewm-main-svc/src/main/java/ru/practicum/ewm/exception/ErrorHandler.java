package ru.practicum.ewm.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Глобальный обработчик исключений для REST API.
 * Перехватывает различные типы исключений и возвращает стандартизированные ответы об ошибках.
 */
@RestControllerAdvice
public class ErrorHandler {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Обработчик исключения NotFoundException.
     * Возвращает HTTP 404 с информацией о том, что запрашиваемый объект не найден.
     *
     * @param ex исключение NotFoundException
     * @return стандартизированный ответ об ошибке
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex) {
        String errorCode = "NOT_FOUND";
        String generalDescription = "Запрашиваемый объект не найден";
        String errorDetails = ex.getMessage();
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);

        return new ErrorResponse(errorCode, generalDescription, errorDetails, timestamp);
    }

    /**
     * Обработчик исключения ConflictException.
     * Возвращает HTTP 409 с информацией о конфликте данных.
     *
     * @param ex исключение ConflictException
     * @return стандартизированный ответ об ошибке
     */
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(ConflictException ex) {
        String errorCode = "CONFLICT";
        String generalDescription = "Такой объект уже существует";
        String errorDetails = ex.getMessage();
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);

        return new ErrorResponse(errorCode, generalDescription, errorDetails, timestamp);
    }

    /**
     * Обработчик исключений валидации аргументов методов.
     * Возвращает HTTP 400 при ошибках валидации входных данных.
     *
     * @param ex исключение MethodArgumentNotValidException
     * @return стандартизированный ответ об ошибке
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorCode = "BAD_REQUEST";
        String generalDescription = "Ошибка валидации: переданы некорректные данные";
        String errorDetails = ex.getMessage();
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);

        return new ErrorResponse(errorCode, generalDescription, errorDetails, timestamp);
    }

    /**
     * Обработчик исключения ForbiddenException.
     * Возвращает HTTP 403 при отсутствии прав доступа.
     *
     * @param ex исключение ForbiddenException
     * @return стандартизированный ответ об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenErrors(final ForbiddenException ex) {
        String errorCode = "FORBIDDEN";
        String generalDescription = "У пользователя нет необходимых прав доступа к ресурсу";
        String errorDetails = ex.getMessage();
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);

        return new ErrorResponse(errorCode, generalDescription, errorDetails, timestamp);
    }

    /**
     * Обработчик исключения IncorrectRequestException.
     * Возвращает HTTP 400 при некорректном запросе.
     *
     * @param ex исключение IncorrectRequestException
     * @return стандартизированный ответ об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleIncorrectRequestErrors(final IncorrectRequestException ex) {
        String errorCode = "BAD_REQUEST";
        String generalDescription = "Некорректный запрос";
        String errorDetails = ex.getMessage();
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);

        return new ErrorResponse(errorCode, generalDescription, errorDetails, timestamp);
    }

    /**
     * Обработчик исключения ValidationException.
     * Возвращает HTTP 400 при ошибках валидации.
     *
     * @param ex исключение ValidationException
     * @return стандартизированный ответ об ошибке
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handlerValidationException(ValidationException ex) {
        String errorCode = "BAD_REQUEST";
        String generalDescription = "Некорректные данные";
        String errorDetails = ex.getMessage();
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);

        return new ErrorResponse(errorCode, generalDescription, errorDetails, timestamp);
    }

    /**
     * Обработчик исключения ConstraintViolationException.
     * Возвращает HTTP 400 при нарушениях ограничений валидации.
     *
     * @param ex исключение ConstraintViolationException
     * @return стандартизированный ответ об ошибке
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handlerValidationException(ConstraintViolationException ex) {
        String errorCode = "BAD_REQUEST";
        String generalDescription = "Некорректные данные";
        String errorDetails = ex.getMessage();
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);

        return new ErrorResponse(errorCode, generalDescription, errorDetails, timestamp);
    }

    /**
     * Обработчик исключения IllegalArgumentException.
     * Возвращает HTTP 400 при передаче некорректных аргументов.
     *
     * @param ex исключение IllegalArgumentException
     * @return стандартизированный ответ об ошибке
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        String errorCode = "BAD_REQUEST";
        String generalDescription = "Некорректные данные";
        String errorDetails = ex.getMessage();
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);

        return new ErrorResponse(errorCode, generalDescription, errorDetails, timestamp);
    }
}