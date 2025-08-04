package ru.practicum.ewm.location.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Сущность местоположения.
 * Описывает географические координаты места проведения события.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "locations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {
    /**
     * Уникальный идентификатор местоположения.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    /**
     * Широта (latitude) в градусах.
     */
    @Column(nullable = false, name = "latitude")
    float latitude;
    /**
     * Долгота (longitude) в градусах.
     */
    @Column(nullable = false, name = "longitude")
    float longitude;
}
