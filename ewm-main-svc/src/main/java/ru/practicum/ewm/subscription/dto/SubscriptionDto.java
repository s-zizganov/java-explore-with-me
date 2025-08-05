package ru.practicum.ewm.subscription.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.utils.FriendshipsStatus;
import java.time.LocalDateTime;

/**
 * DTO для представления информации о подписке.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto {
    private Long id;
    private Long followerId;
    private User owner;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime subscribeTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime unsubscribeTime;
    private FriendshipsStatus friendshipsStatus;
}