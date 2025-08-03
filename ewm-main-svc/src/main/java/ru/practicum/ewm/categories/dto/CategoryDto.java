package ru.practicum.ewm.categories.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для передачи данных о категории.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    /**
     * Название категории (обязательное, не более 50 символов).
     */
    @NotBlank(message = "Название категории должно быть указано!")
    @Size(max = 50, message = "Длина name не должна превышать 50 символов!")
    private String name;
}
