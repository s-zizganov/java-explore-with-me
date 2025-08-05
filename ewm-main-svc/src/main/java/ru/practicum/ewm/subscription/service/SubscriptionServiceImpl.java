package ru.practicum.ewm.subscription.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.subscription.dto.SubscriberDto;
import ru.practicum.ewm.subscription.dto.SubscriptionDto;
import ru.practicum.ewm.subscription.dto.SubscriptionMapper;
import ru.practicum.ewm.subscription.dto.SubscriptionRequestDto;
import ru.practicum.ewm.subscription.model.Subscription;
import ru.practicum.ewm.subscription.repository.SubscriptionRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.utils.FriendshipsStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для управления подписками.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final SubscriptionMapper subscriptionMapper;

    /**
     * Получает список всех подписчиков пользователя с пагинацией.
     *
     * @param userId идентификатор пользователя
     * @param start  начальная позиция для пагинации
     * @param size   размер страницы
     * @return список подписчиков в формате {@link SubscriberDto}
     */
    @Transactional(readOnly = true)
    @Override
    public List<SubscriberDto> getAllSubscribers(Long userId, int start, int size) {
        User owner = findUserById(userId);
        Pageable pageable = PageRequest.of(start / size, size);
        List<Subscription> subscriptions = subscriptionRepository.findByOwner(owner, pageable);
        return subscriptions.stream()
                .map(subscriptionMapper::toSubscriberDto)
                .collect(Collectors.toList());
    }

    /**
     * Получает количество подписчиков пользователя.
     *
     * @param userId идентификатор пользователя
     * @return количество подписчиков
     */
    @Transactional(readOnly = true)
    @Override
    public Long getSubscriberCount(Long userId) {
        log.info("Запрос количества подписчиков для пользователя с ID: {}", userId);
        User user = findUserById(userId);
        long count = subscriptionRepository.countByOwnerAndFriendshipsStatusIn(user,
                List.of(FriendshipsStatus.ONE_SIDED, FriendshipsStatus.MUTUAL));
        log.info("У пользователя {} найдено {} подписчиков", userId, count);
        return count;
    }

    /**
     * Получает события от пользователей, на которых подписан пользователь.
     *
     * @param userId идентификатор пользователя
     * @param start  начальная позиция для пагинации
     * @param size   размер страницы
     * @return список событий в формате {@link EventShortDto}
     */
    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getEventsFromSubscriptions(Long userId, int start, int size) {
        User follower = findUserById(userId);
        List<Subscription> subscriptions = subscriptionRepository.findByFollower(follower);
        List<Long> ownerIds = subscriptions.stream()
                .map(subscription -> subscription.getOwner().getId())
                .collect(Collectors.toList());
        PageRequest pageRequest = PageRequest.of(start / size, size, Sort.by("eventDate").descending());
        List<Event> events = eventRepository.findByInitiatorIdIn(ownerIds, pageRequest);
        return events.stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    /**
     * Отменяет подписку пользователя на другого пользователя.
     *
     * @param userId  идентификатор пользователя
     * @param ownerId идентификатор владельца
     */
    @Override
    public void unsubscribe(Long userId, Long ownerId) {
        User follower = findUserById(userId);
        User owner = findUserById(ownerId);
        log.info("Обработка запроса на отмену подписки пользователя {} от пользователя {}", userId, ownerId);
        Optional<Subscription> subscription = subscriptionRepository.findByFollowerAndOwner(follower, owner);
        if (subscription.isEmpty()) {
            log.warn("Подписка пользователя {} на пользователя {} не найдена", userId, ownerId);
            throw new ConflictException("Подписка на пользователя отсутствует");
        }
        Optional<Subscription> reverseSubscription = subscriptionRepository.findByFollowerAndOwner(owner, follower);
        if (reverseSubscription.isPresent() &&
                reverseSubscription.get().getFriendshipsStatus().equals(FriendshipsStatus.MUTUAL)) {
            reverseSubscription.get().setFriendshipsStatus(FriendshipsStatus.ONE_SIDED);
            reverseSubscription.get().setUnsubscribeTime(LocalDateTime.now());
            subscriptionRepository.save(reverseSubscription.get());
        }
        subscriptionRepository.delete(subscription.get());
        log.info("Пользователь {} успешно отписался от пользователя {}", userId, ownerId);
    }

    /**
     * Создает подписку пользователя на другого пользователя.
     *
     * @param userId              идентификатор пользователя
     * @param subscriptionRequest данные запроса на подписку
     * @return объект {@link SubscriptionDto}
     */
    @Override
    public SubscriptionDto subscribe(Long userId, SubscriptionRequestDto subscriptionRequest) {
        User follower = findUserById(userId);
        User owner = findUserById(subscriptionRequest.getOwnerId());
        validateNewSubscription(follower, owner);
        Subscription subscription = manageSubscription(follower, owner);
        log.info("Создана подписка пользователя {} на пользователя {}. Статус: {}",
                userId, owner.getId(), subscription.getFriendshipsStatus());
        return subscriptionMapper.toSubscriptionDto(subscription);
    }

    /**
     * Находит пользователя по идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return объект {@link User}
     * @throws NotFoundException если пользователь не найден
     */
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
    }

    /**
     * Проверяет возможность создания подписки.
     *
     * @param follower подписчик
     * @param owner    владелец
     * @throws ConflictException если подписка невозможна
     */
    private void validateNewSubscription(User follower, User owner) {
        if (!follower.isAllowSubscriptions()) {
            log.warn("Пользователь {} не разрешает подписки", follower.getId());
            throw new ConflictException("Подписки на пользователя запрещены");
        }
        if (follower.getId().equals(owner.getId())) {
            log.warn("Попытка подписки пользователя {} на самого себя", follower.getId());
            throw new ConflictException("Нельзя подписаться на самого себя");
        }
        Optional<Subscription> existingSubscription = subscriptionRepository.findByFollowerAndOwner(follower, owner);
        if (existingSubscription.isPresent()) {
            Subscription sub = existingSubscription.get();
            if (sub.getFriendshipsStatus() == FriendshipsStatus.ONE_SIDED ||
                    sub.getFriendshipsStatus() == FriendshipsStatus.MUTUAL) {
                log.warn("Подписка пользователя {} на пользователя {} уже существует", follower.getId(), owner.getId());
                throw new ConflictException("Подписка уже существует");
            }
        }
    }

    /**
     * Создает или обновляет подписку.
     *
     * @param follower подписчик
     * @param owner    владелец
     * @return сущность {@link Subscription}
     */
    private Subscription manageSubscription(User follower, User owner) {
        Subscription subscription = subscriptionMapper.toSubscription(follower, owner, LocalDateTime.now(),
                null, FriendshipsStatus.NO_FRIENDSHIP);
        Optional<Subscription> reverseSubscription = subscriptionRepository.findByFollowerAndOwner(owner, follower);
        if (reverseSubscription.isPresent()) {
            subscription.setFriendshipsStatus(FriendshipsStatus.MUTUAL);
            subscription.setSubscribeTime(LocalDateTime.now());
            reverseSubscription.get().setFriendshipsStatus(FriendshipsStatus.MUTUAL);
            reverseSubscription.get().setSubscribeTime(LocalDateTime.now());
            subscriptionRepository.saveAll(List.of(subscription, reverseSubscription.get()));
        } else {
            subscription.setFriendshipsStatus(FriendshipsStatus.ONE_SIDED);
            subscription.setSubscribeTime(LocalDateTime.now());
            subscriptionRepository.save(subscription);
        }
        return subscription;
    }
}