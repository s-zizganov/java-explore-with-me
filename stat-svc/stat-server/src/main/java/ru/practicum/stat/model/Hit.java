package ru.practicum.stat.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * Сущность для хранения информации о посещении эндпоинта (hit) в базе данных.
 * Отражает структуру таблицы statistic.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "statistic")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hit {
    /**
     * Уникальный идентификатор записи (генерируется автоматически).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    /**
     * Имя приложения, отправившего событие.
     */
    @Column(nullable = false)
    String app;
    /**
     * URI, к которому был выполнен запрос.
     */
    @Column(nullable = false)
    String uri;
    /**
     * IP-адрес пользователя, выполнившего запрос.
     */
    @Column(nullable = false)
    String ip;
    /**
     * Время запроса (поле created в таблице).
     */
    @Column(name = "created", nullable = false)
    LocalDateTime timestamp;
}
