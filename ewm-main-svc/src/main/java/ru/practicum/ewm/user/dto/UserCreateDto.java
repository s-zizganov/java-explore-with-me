package ru.practicum.ewm.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * DTO для создания нового пользователя.
 * Используется для передачи данных при регистрации или добавлении пользователя администратором.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateDto {

    /**
     * Имя пользователя.
     */
    @NotBlank(message = "Поле name не может быть пустым")
    @Size(min = 2, max = 250, message = "Длина name должна быть от 2 до 250 символов!")
    String name;

    /**
     * Email пользователя.
     */
    @NotBlank(message = "Поле email не может быть пустым")
    @Size(min = 6, max = 254, message = "Длина email должна быть от 6 до 254 символов!")
    @Email(message = "Email должен быть в подходящем формате!")
    String email;
}
