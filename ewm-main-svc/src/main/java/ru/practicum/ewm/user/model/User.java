package ru.practicum.ewm.user.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Сущность пользователя системы.
 * Описывает основные данные пользователя: id, имя и email.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    /**
     * Имя пользователя.
     */
    @Column(nullable = false, length = 250)
    String name;
    /**
     * Email пользователя (уникальный).
     */
    @Column(nullable = false, unique = true, length = 254)
    String email;

    @Column(name = "allow_subscriptions", nullable = false)
    boolean allowSubscriptions = true;
}
