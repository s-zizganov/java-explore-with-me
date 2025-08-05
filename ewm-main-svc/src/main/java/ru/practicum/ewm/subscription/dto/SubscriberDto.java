package ru.practicum.ewm.subscription.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.utils.FriendshipsStatus;
import java.time.LocalDateTime;

/**
 * DTO для представления информации о подписчике.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriberDto {
    private Long userId;
    private String ownerName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime subscribeTime;
    private FriendshipsStatus friendshipsStatus;
}