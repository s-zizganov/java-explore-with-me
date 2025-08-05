package ru.practicum.ewm.subscription.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.utils.FriendshipsStatus;
import java.time.LocalDateTime;

/**
 * DTO для представления информации о подписчике.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriberDto {
    Long userId;
    String ownerName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime subscribeTime;
    FriendshipsStatus friendshipsStatus;
}