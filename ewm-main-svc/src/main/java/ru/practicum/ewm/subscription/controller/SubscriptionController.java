package ru.practicum.ewm.subscription.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.subscription.dto.SubscriberDto;
import ru.practicum.ewm.subscription.dto.SubscriptionDto;
import ru.practicum.ewm.subscription.dto.SubscriptionRequestDto;
import ru.practicum.ewm.subscription.service.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/subscriptions")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    /**
     * Получает список всех подписчиков пользователя с пагинацией.
     *
     * @param userId идентификатор пользователя
     * @param offset начальная позиция для пагинации (по умолчанию 0)
     * @param limit  количество записей на страницу (по умолчанию 10)
     * @return список подписчиков в формате {@link SubscriberDto}
     */
    @GetMapping("/subscribers")
    public List<SubscriberDto> fetchAllSubscribers(@PathVariable Long userId,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero int offset,
                                                   @RequestParam(defaultValue = "10") @Positive int limit) {
        log.info("Запрос списка подписчиков для пользователя с ID {}", userId);
        return subscriptionService.getAllSubscribers(userId, offset, limit);
    }

    /**
     * Получает количество подписчиков пользователя.
     *
     * @param userId идентификатор пользователя
     * @return количество подписчиков
     */
    @GetMapping("/subscribers/count")
    public Long fetchSubscriberCount(@PathVariable Long userId) {
        log.info("Запрос количества подписчиков для пользователя с ID {}", userId);
        return subscriptionService.getSubscriberCount(userId);
    }

    /**
     * Получает события от пользователей, на которых подписан указанный пользователь, с пагинацией.
     *
     * @param userId    идентификатор пользователя
     * @param start     начальная позиция для пагинации (по умолчанию 0)
     * @param pageSize  количество событий на страницу (по умолчанию 10)
     * @return список событий в формате {@link EventShortDto}
     */
    @GetMapping("/events")
    public List<EventShortDto> fetchSubscriptionEvents(@PathVariable Long userId,
                                                       @RequestParam(defaultValue = "0") @PositiveOrZero int start,
                                                       @RequestParam(defaultValue = "10") @Positive int pageSize) {
        log.info("Запрос событий от подписок для пользователя с ID {}", userId);
        return subscriptionService.getEventsFromSubscriptions(userId, start, pageSize);
    }

    /**
     * Отменяет подписку пользователя на другого пользователя.
     *
     * @param userId  идентификатор пользователя, отменяющего подписку
     * @param ownerId идентификатор пользователя, от которого отменяется подписка
     */
    @DeleteMapping("/{ownerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelSubscription(@PathVariable Long userId, @PathVariable Long ownerId) {
        log.info("Отмена подписки пользователя {} на пользователя {}", userId, ownerId);
        subscriptionService.unsubscribe(userId, ownerId);
    }

    /**
     * Создает новую подписку пользователя на другого пользователя.
     *
     * @param userId      идентификатор пользователя, создающего подписку
     * @param requestDto  данные запроса на подписку в формате {@link SubscriptionRequestDto}
     * @return созданная подписка в формате {@link SubscriptionDto}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionDto createSubscription(@PathVariable Long userId,
                                              @Valid @RequestBody SubscriptionRequestDto requestDto) {
        log.info("Создание подписки пользователя {} на пользователя {}", userId, requestDto.getOwnerId());
        return subscriptionService.subscribe(userId, requestDto);
    }
}