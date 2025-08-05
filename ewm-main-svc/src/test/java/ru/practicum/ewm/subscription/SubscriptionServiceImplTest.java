package ru.practicum.ewm.subscription;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.ewm.MainApp;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.subscription.dto.SubscriptionDto;
import ru.practicum.ewm.subscription.dto.SubscriptionMapper;
import ru.practicum.ewm.subscription.dto.SubscriptionRequestDto;
import ru.practicum.ewm.subscription.model.Subscription;
import ru.practicum.ewm.subscription.repository.SubscriptionRepository;
import ru.practicum.ewm.subscription.service.SubscriptionService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.utils.FriendshipsStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестирование реализации сервиса подписок.
 */
@SpringBootTest(classes = MainApp.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Тестирование сервиса подписок")
public class SubscriptionServiceImplTest {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    private EventMapper eventMapper;

    /**
     * Проверяет подсчет количества подписчиков.
     */
    @Test
    @DisplayName("Успешный подсчет подписчиков")
    void countSubscribers_ValidUser_ReturnsCorrectCount() {
        User owner = new User();
        owner.setName("Владелец");
        owner.setEmail("owner@example.com");
        owner.setAllowSubscriptions(true);
        userRepository.save(owner);

        User follower1 = new User();
        follower1.setName("Подписчик1");
        follower1.setEmail("follower1@example.com");
        follower1.setAllowSubscriptions(true);
        userRepository.save(follower1);

        User follower2 = new User();
        follower2.setName("Подписчик2");
        follower2.setEmail("follower2@example.com");
        follower2.setAllowSubscriptions(true);
        userRepository.save(follower2);

        Subscription subscription1 = new Subscription();
        subscription1.setFollower(follower1);
        subscription1.setOwner(owner);
        subscription1.setSubscribeTime(LocalDateTime.now());
        subscription1.setFriendshipsStatus(FriendshipsStatus.ONE_SIDED);
        subscriptionRepository.save(subscription1);

        Subscription subscription2 = new Subscription();
        subscription2.setFollower(follower2);
        subscription2.setOwner(owner);
        subscription2.setSubscribeTime(LocalDateTime.now());
        subscription2.setFriendshipsStatus(FriendshipsStatus.MUTUAL);
        subscriptionRepository.save(subscription2);

        Long count = subscriptionService.getSubscriberCount(owner.getId());
        assertEquals(2, count);
    }

    /**
     * Проверяет получение событий от подписок.
     */
    @Test
    @DisplayName("Успешное получение событий от подписок")
    void fetchSubscriptionEvents_ValidUser_ReturnsEvents() {
        User follower = new User();
        follower.setName("Подписчик");
        follower.setEmail("follower@example.com");
        follower.setAllowSubscriptions(true);
        userRepository.save(follower);

        User owner = new User();
        owner.setName("Владелец");
        owner.setEmail("owner@example.com");
        owner.setAllowSubscriptions(true);
        userRepository.save(owner);

        Subscription subscription = new Subscription();
        subscription.setFollower(follower);
        subscription.setOwner(owner);
        subscription.setSubscribeTime(LocalDateTime.now());
        subscription.setFriendshipsStatus(FriendshipsStatus.ONE_SIDED);
        subscriptionRepository.save(subscription);

        Event event = new Event();
        event.setInitiator(owner);
        event.setEventDate(LocalDateTime.now());
        eventRepository.save(event);

        List<EventShortDto> events = subscriptionService.getEventsFromSubscriptions(follower.getId(), 0, 10);
        assertFalse(events.isEmpty());
        assertEquals(1, events.size());
        assertEquals(eventMapper.toShortDto(event).getTitle(), events.get(0).getTitle());
    }

    /**
     * Проверяет успешную отмену подписки.
     */
    @Test
    @DisplayName("Успешная отмена подписки")
    void cancelSubscription_ValidSubscription_DeletesSubscription() {
        User follower = new User();
        follower.setName("Подписчик");
        follower.setEmail("follower@example.com");
        follower.setAllowSubscriptions(true);
        userRepository.save(follower);

        User owner = new User();
        owner.setName("Владелец");
        owner.setEmail("owner@example.com");
        owner.setAllowSubscriptions(true);
        userRepository.save(owner);

        Subscription subscription = new Subscription();
        subscription.setFollower(follower);
        subscription.setOwner(owner);
        subscription.setSubscribeTime(LocalDateTime.now());
        subscription.setFriendshipsStatus(FriendshipsStatus.ONE_SIDED);
        subscriptionRepository.save(subscription);

        subscriptionService.unsubscribe(follower.getId(), owner.getId());
        Optional<Subscription> deletedSubscription = subscriptionRepository.findByFollowerAndOwner(follower, owner);
        assertTrue(deletedSubscription.isEmpty());
    }

    /**
     * Проверяет попытку отмены несуществующей подписки.
     */
    @Test
    @DisplayName("Попытка отмены несуществующей подписки")
    void cancelSubscription_NonExistingSubscription_ThrowsConflictException() {
        User follower = new User();
        follower.setName("Подписчик");
        follower.setEmail("follower@example.com");
        follower.setAllowSubscriptions(true);
        userRepository.save(follower);

        User owner = new User();
        owner.setName("Владелец");
        owner.setEmail("owner@example.com");
        owner.setAllowSubscriptions(true);
        userRepository.save(owner);

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> subscriptionService.unsubscribe(follower.getId(), owner.getId()),
                "Ожидается исключение ConflictException"
        );
        assertTrue(exception.getMessage().contains("Подписка на пользователя отсутствует"));
    }

    /**
     * Проверяет попытку подписки на самого себя.
     */
    @Test
    @DisplayName("Попытка подписки на самого себя")
    void createSubscription_SelfSubscription_ThrowsConflictException() {
        User user = new User();
        user.setName("Пользователь");
        user.setEmail("user@example.com");
        user.setAllowSubscriptions(true);
        userRepository.save(user);

        SubscriptionRequestDto request = new SubscriptionRequestDto();
        request.setOwnerId(user.getId());

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> subscriptionService.subscribe(user.getId(), request),
                "Ожидается исключение ConflictException"
        );
        assertTrue(exception.getMessage().contains("Нельзя подписаться на самого себя"));
    }

    /**
     * Проверяет успешное создание подписки.
     */
    @Test
    @DisplayName("Успешное создание подписки")
    void createSubscription_ValidSubscription_ReturnsSubscriptionDto() {
        User follower = new User();
        follower.setName("Подписчик");
        follower.setEmail("follower@example.com");
        follower.setAllowSubscriptions(true);
        userRepository.save(follower);

        User owner = new User();
        owner.setName("Владелец");
        owner.setEmail("owner@example.com");
        owner.setAllowSubscriptions(true);
        userRepository.save(owner);

        SubscriptionRequestDto request = new SubscriptionRequestDto();
        request.setOwnerId(owner.getId());

        SubscriptionDto result = subscriptionService.subscribe(follower.getId(), request);
        assertNotNull(result);
        assertEquals(follower.getId(), result.getFollowerId());
        assertEquals(owner.getId(), result.getOwner().getId());
        assertEquals(FriendshipsStatus.ONE_SIDED, result.getFriendshipsStatus());
    }
}