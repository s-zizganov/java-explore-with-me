package ru.practicum.ewm.subscription;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.ewm.subscription.dto.SubscriberDto;
import ru.practicum.ewm.subscription.dto.SubscriptionDto;
import ru.practicum.ewm.subscription.dto.SubscriptionMapper;
import ru.practicum.ewm.subscription.dto.SubscriptionRequestDto;
import ru.practicum.ewm.subscription.model.Subscription;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.utils.FriendshipsStatus;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестирование маппера подписок.
 */
@SpringBootTest
@DisplayName("Тестирование маппера подписок")
public class SubscriptionMapperTest {

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    private User follower;
    private User owner;

    /**
     * Подготовка данных перед каждым тестом.
     */
    @BeforeEach
    void init() {
        follower = new User(null, "Подписчик", "follower@example.com", true);
        owner = new User(null, "Владелец", "owner@example.com", false);
    }

    /**
     * Проверяет преобразование сущности подписки в {@link SubscriptionDto}.
     */
    @Test
    @DisplayName("Преобразование корректной подписки в SubscriptionDto")
    void mapToSubscriptionDto_ValidSubscription_ReturnsCorrectDto() {
        Subscription subscription = new Subscription(2L, follower, owner, LocalDateTime.now(), null, FriendshipsStatus.NO_FRIENDSHIP);
        SubscriptionDto dto = subscriptionMapper.toSubscriptionDto(subscription);
        assertEquals(subscription.getId(), dto.getId());
        assertEquals(subscription.getFollower().getId(), dto.getFollowerId());
        assertEquals(subscription.getOwner(), dto.getOwner());
        assertEquals(subscription.getSubscribeTime(), dto.getSubscribeTime());
        assertEquals(subscription.getUnsubscribeTime(), dto.getUnsubscribeTime());
        assertEquals(subscription.getFriendshipsStatus(), dto.getFriendshipsStatus());
    }

    /**
     * Проверяет преобразование null в {@link SubscriptionDto}.
     */
    @Test
    @DisplayName("Преобразование null в SubscriptionDto")
    void mapToSubscriptionDto_NullSubscription_ReturnsNull() {
        SubscriptionDto dto = subscriptionMapper.toSubscriptionDto(null);
        assertNull(dto);
    }

    /**
     * Проверяет преобразование сущности подписки в {@link SubscriberDto}.
     */
    @Test
    @DisplayName("Преобразование корректной подписки в SubscriberDto")
    void mapToSubscriberDto_ValidSubscription_ReturnsCorrectDto() {
        Subscription subscription = new Subscription(2L, follower, owner, LocalDateTime.now(), null, FriendshipsStatus.NO_FRIENDSHIP);
        SubscriberDto dto = subscriptionMapper.toSubscriberDto(subscription);
        assertEquals(subscription.getFollower().getId(), dto.getUserId());
        assertEquals(subscription.getOwner().getName(), dto.getOwnerName());
        assertEquals(subscription.getSubscribeTime(), dto.getSubscribeTime());
        assertEquals(subscription.getFriendshipsStatus(), dto.getFriendshipsStatus());
    }

    /**
     * Проверяет преобразование null в {@link SubscriberDto}.
     */
    @Test
    @DisplayName("Преобразование null в SubscriberDto")
    void mapToSubscriberDto_NullSubscription_ReturnsNull() {
        SubscriberDto dto = subscriptionMapper.toSubscriberDto(null);
        assertNull(dto);
    }

    /**
     * Проверяет преобразование параметров в сущность подписки.
     */
    @Test
    @DisplayName("Преобразование параметров в Subscription")
    void mapToSubscription_ValidParameters_ReturnsCorrectSubscription() {
        LocalDateTime time = LocalDateTime.now();
        SubscriptionRequestDto request = new SubscriptionRequestDto(owner.getId());
        Subscription subscription = subscriptionMapper.toSubscription(follower, owner, time, null, FriendshipsStatus.NO_FRIENDSHIP);
        assertEquals(follower, subscription.getFollower());
        assertEquals(owner, subscription.getOwner());
        assertEquals(time, subscription.getSubscribeTime());
        assertNull(subscription.getUnsubscribeTime());
        assertEquals(FriendshipsStatus.NO_FRIENDSHIP, subscription.getFriendshipsStatus());
    }

    /**
     * Проверяет преобразование null-параметров в сущность подписки.
     */
    @Test
    @DisplayName("Преобразование null-параметров в Subscription")
    void mapToSubscription_NullParameters_ReturnsNull() {
        Subscription subscription = subscriptionMapper.toSubscription(null, null, null, null, null);
        assertNull(subscription);
    }
}