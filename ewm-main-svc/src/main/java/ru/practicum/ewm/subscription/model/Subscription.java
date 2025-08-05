package ru.practicum.ewm.subscription.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.utils.FriendshipsStatus;
import java.time.LocalDateTime;

/**
 * Сущность, представляющая подписку между пользователями.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subscriptions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    @ToString.Exclude
    User follower;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @ToString.Exclude
    User owner;

    @Column(name = "subscribe_time")
    LocalDateTime subscribeTime;

    @Column(name = "unsubscribe_time")
    LocalDateTime unsubscribeTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "friendships_status", nullable = false)
    FriendshipsStatus friendshipsStatus;
}