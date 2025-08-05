package ru.practicum.ewm.subscription.model;

import jakarta.persistence.*;
import lombok.*;
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
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    @ToString.Exclude
    private User follower;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @ToString.Exclude
    private User owner;

    @Column(name = "subscribe_time")
    private LocalDateTime subscribeTime;

    @Column(name = "unsubscribe_time")
    private LocalDateTime unsubscribeTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "friendships_status", nullable = false)
    private FriendshipsStatus friendshipsStatus;
}