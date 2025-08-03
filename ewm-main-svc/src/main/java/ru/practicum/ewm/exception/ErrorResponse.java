package ru.practicum.ewm.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Стандартизированный ответ об ошибке для REST API.
 * Используется для передачи информации об ошибках в едином формате.
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    /**
     * HTTP статус ошибки.
     */
    private String status;
    /**
     * Причина возникновения ошибки.
     */
    private String reason;
    /**
     * Детальное сообщение об ошибке.
     */
    private String message;
    /**
     * Временная метка возникновения ошибки.
     */
    private String timestamp;
}