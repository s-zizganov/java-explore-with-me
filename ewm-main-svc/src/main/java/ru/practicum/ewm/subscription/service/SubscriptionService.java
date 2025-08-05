package ru.practicum.ewm.subscription.service;

import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.subscription.dto.SubscriberDto;
import ru.practicum.ewm.subscription.dto.SubscriptionDto;
import ru.practicum.ewm.subscription.dto.SubscriptionRequestDto;
import java.util.List;

/**
 * Интерфейс сервиса для управления подписками.
 */
public interface SubscriptionService {

    /**
     * Создает подписку пользователя на другого пользователя.
     *
     * @param userId              идентификатор пользователя
     * @param subscriptionRequest данные запроса на подписку
     * @return объект {@link SubscriptionDto}
     */
    SubscriptionDto subscribe(Long userId, SubscriptionRequestDto subscriptionRequest);

    /**
     * Отменяет подписку пользователя на другого пользователя.
     *
     * @param userId  идентификатор пользователя
     * @param ownerId идентификатор владельца
     */
    void unsubscribe(Long userId, Long ownerId);

    /**
     * Получает события от пользователей, на которых подписан пользователь.
     *
     * @param userId идентификатор пользователя
     * @param start  начальная позиция для пагинации
     * @param size   размер страницы
     * @return список событий в формате {@link EventShortDto}
     */
    List<EventShortDto> getEventsFromSubscriptions(Long userId, int start, int size);

    /**
     * Получает количество подписчиков пользователя.
     *
     * @param userId идентификатор пользователя
     * @return количество подписчиков
     */
    Long getSubscriberCount(Long userId);

    /**
     * Получает список всех подписчиков пользователя.
     *
     * @param userId идентификатор пользователя
     * @param start  начальная позиция для пагинации
     * @param size   размер страницы
     * @return список подписчиков в формате {@link SubscriberDto}
     */
    List<SubscriberDto> getAllSubscribers(Long userId, int start, int size);
}