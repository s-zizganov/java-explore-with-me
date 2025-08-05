package ru.practicum.ewm.subscription.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.subscription.model.Subscription;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.utils.FriendshipsStatus;
import java.time.LocalDateTime;

/**
 * Маппер для преобразования между сущностью подписки и DTO.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    /**
     * Преобразует сущность подписки в {@link SubscriptionDto}.
     *
     * @param subscription сущность подписки
     * @return объект {@link SubscriptionDto}
     */
    @Mapping(target = "followerId", source = "follower.id")
    SubscriptionDto toSubscriptionDto(Subscription subscription);

    /**
     * Преобразует сущность подписки в {@link SubscriberDto}.
     *
     * @param subscription сущность подписки
     * @return объект {@link SubscriberDto}
     */
    @Mapping(target = "userId", source = "follower.id")
    @Mapping(target = "ownerName", source = "owner.name")
    SubscriberDto toSubscriberDto(Subscription subscription);

    /**
     * Преобразует параметры в сущность подписки.
     *
     * @param follower          пользователь, который подписывается
     * @param owner            пользователь, на которого подписываются
     * @param subscribeTime    время подписки
     * @param unsubscribeTime  время отписки
     * @param friendshipsStatus статус дружбы
     * @return сущность {@link Subscription}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "follower", source = "follower")
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "subscribeTime", source = "subscribeTime")
    @Mapping(target = "unsubscribeTime", source = "unsubscribeTime")
    @Mapping(target = "friendshipsStatus", source = "friendshipsStatus")
    Subscription toSubscription(User follower, User owner, LocalDateTime subscribeTime,
                                LocalDateTime unsubscribeTime, FriendshipsStatus friendshipsStatus);
}