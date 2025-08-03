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
        return new ErrorResponse(
                "NOT_FOUND",
                "Запрашиваемый объект не найден",
                ex.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
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
        return new ErrorResponse(
                "CONFLICT",
                "Такой объект уже существует",
                ex.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
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
        return new ErrorResponse(
                "BAD_REQUEST",
                "Ошибка валидации: переданы некорректные данные",
                ex.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
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
        return new ErrorResponse(
                "FORBIDDEN",
                "У пользователя нет необходимых прав доступа к ресурсу",
                ex.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
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
        return new ErrorResponse(
                "BAD_REQUEST",
                "Некорректный запрос",
                ex.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
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
        return new ErrorResponse(
                "BAD_REQUEST",
                "Некорректные данные",
                ex.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
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
        return new ErrorResponse(
                "BAD_REQUEST",
                "Некорректные данные",
                ex.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
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
        return new ErrorResponse(
                "BAD_REQUEST",
                "Некорректные данные",
                ex.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }
}
