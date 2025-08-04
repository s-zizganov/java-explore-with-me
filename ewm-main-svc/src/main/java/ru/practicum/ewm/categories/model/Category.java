package ru.practicum.ewm.categories.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Сущность категории для хранения в базе данных.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {
    /**
     * Уникальный идентификатор категории (генерируется автоматически).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    /**
     * Название категории (обязательное, не более 50 символов).
     */
    @Column(nullable = false, length = 50)
    String name;
}
