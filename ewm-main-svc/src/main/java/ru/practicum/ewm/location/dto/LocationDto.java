package ru.practicum.ewm.location.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для передачи данных о местоположении.
 * Содержит географические координаты (широта и долгота).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    /**
     * Широта (latitude) в градусах.
     */
    @NotNull(message = "Поле latitude не может быть пустым")
    private float lat;
    /**
     * Долгота (longitude) в градусах.
     */
    @NotNull(message = "Поле longitude не может быть пустым")
    private float lon;
}
