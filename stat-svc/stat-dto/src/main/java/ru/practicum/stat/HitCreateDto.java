package ru.practicum.stat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * DTO для передачи информации о посещении эндпоинта (hit) в сервис статистики.
 * Содержит данные о приложении, URI, IP пользователя и времени запроса.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HitCreateDto {
    /**
     * Имя приложения, отправляющего событие.
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
     * Время запроса (формат: yyyy-MM-dd HH:mm:ss).
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp;
}
