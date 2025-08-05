package ru.practicum.ewm.subscription.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для запроса на создание подписки.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequestDto {
    @NotNull
    private Long ownerId;
}