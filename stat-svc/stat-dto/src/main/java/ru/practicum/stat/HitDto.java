package ru.practicum.stat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * DTO для передачи полной информации о посещении эндпоинта (hit), включая идентификатор.
 * Используется для получения данных из сервиса статистики.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HitDto {
    /**
     * Уникальный идентификатор записи о посещении.
     */
    Long id;
    /**
     * Имя приложения, отправившего событие.
     */
    String app;
    /**
     * URI, к которому был выполнен запрос.
     */
    String uri;
    /**
     * IP-адрес пользователя, выполнившего запрос.
     */
    String ip;
    /**
     * Время запроса.
     */
    LocalDateTime timestamp;
}
