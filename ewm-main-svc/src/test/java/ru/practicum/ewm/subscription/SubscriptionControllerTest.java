package ru.practicum.ewm.subscription;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.subscription.controller.SubscriptionController;
import ru.practicum.ewm.subscription.dto.SubscriberDto;
import ru.practicum.ewm.subscription.dto.SubscriptionDto;
import ru.practicum.ewm.subscription.dto.SubscriptionRequestDto;
import ru.practicum.ewm.subscription.service.SubscriptionService;
import ru.practicum.ewm.utils.FriendshipsStatus;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionController.class)
@DisplayName("Тестирование контроллера подписок")
public class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubscriptionService subscriptionService;

    /**
     * Проверяет успешное получение списка подписчиков с пагинацией.
     *
     * @throws Exception если возникает ошибка при выполнении запроса
     */
    @Test
    @DisplayName("Успешное получение списка подписчиков")
    void fetchAllSubscribers_ReturnsPaginatedSubscribers() throws Exception {
        Long userId = 1L;
        SubscriberDto subscriber = new SubscriberDto();
        subscriber.setUserId(2L);
        subscriber.setOwnerName("Имя владельца");

        when(subscriptionService.getAllSubscribers(userId, 0, 10))
                .thenReturn(List.of(subscriber));

        mockMvc.perform(get("/users/{userId}/subscriptions/subscribers", userId)
                        .param("offset", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(2L))
                .andExpect(jsonPath("$[0].ownerName").value("Имя владельца"));
    }

    /**
     * Проверяет успешное получение количества подписчиков.
     *
     * @throws Exception если возникает ошибка при выполнении запроса
     */
    @Test
    @DisplayName("Успешное получение количества подписчиков")
    void fetchSubscriberCount_ReturnsCorrectCount() throws Exception {
        Long userId = 1L;
        Long count = 5L;

        when(subscriptionService.getSubscriberCount(userId)).thenReturn(count);

        mockMvc.perform(get("/users/{userId}/subscriptions/subscribers/count", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(count)));
    }

    /**
     * Проверяет успешное получение событий от подписок с пагинацией.
     *
     * @throws Exception если возникает ошибка при выполнении запроса
     */
    @Test
    @DisplayName("Успешное получение событий от подписок")
    void fetchSubscriptionEvents_ReturnsPaginatedEvents() throws Exception {
        Long userId = 1L;
        EventShortDto event = new EventShortDto();
        event.setId(1L);
        event.setTitle("Название события");

        when(subscriptionService.getEventsFromSubscriptions(userId, 0, 10))
                .thenReturn(List.of(event));

        mockMvc.perform(get("/users/{userId}/subscriptions/events", userId)
                        .param("start", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Название события"));
    }

    /**
     * Проверяет успешную отмену подписки.
     *
     * @throws Exception если возникает ошибка при выполнении запроса
     */
    @Test
    @DisplayName("Успешная отмена подписки")
    void cancelSubscription_Successful_ReturnsNoContent() throws Exception {
        Long userId = 1L;
        Long ownerId = 2L;

        doNothing().when(subscriptionService).unsubscribe(userId, ownerId);

        mockMvc.perform(delete("/users/{userId}/subscriptions/{ownerId}", userId, ownerId))
                .andExpect(status().isNoContent());
    }

    /**
     * Проверяет попытку отмены несуществующей подписки, что приводит к конфликту.
     *
     * @throws Exception если возникает ошибка при выполнении запроса
     */
    @Test
    @DisplayName("Отмена несуществующей подписки возвращает конфликт")
    void cancelSubscription_NoSubscription_ReturnsConflict() throws Exception {
        Long userId = 1L;
        Long ownerId = 2L;

        doThrow(new ConflictException("Подписка на пользователя отсутствует"))
                .when(subscriptionService).unsubscribe(userId, ownerId);

        mockMvc.perform(delete("/users/{userId}/subscriptions/{ownerId}", userId, ownerId))
                .andExpect(status().isConflict());
    }

    /**
     * Проверяет успешное создание подписки.
     *
     * @throws Exception если возникает ошибка при выполнении запроса
     */
    @Test
    @DisplayName("Успешное создание подписки")
    void createSubscription_Successful_ReturnsCreated() throws Exception {
        Long userId = 1L;
        SubscriptionRequestDto request = new SubscriptionRequestDto();
        request.setOwnerId(2L);

        SubscriptionDto response = new SubscriptionDto();
        response.setId(1L);
        response.setFollowerId(userId);
        response.setFriendshipsStatus(FriendshipsStatus.ONE_SIDED);

        when(subscriptionService.subscribe(userId, request)).thenReturn(response);

        mockMvc.perform(post("/users/{userId}/subscriptions", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.followerId").value(userId))
                .andExpect(jsonPath("$.friendshipsStatus").value(FriendshipsStatus.ONE_SIDED.name()));
    }

    /**
     * Проверяет попытку подписки на самого себя, что приводит к конфликту.
     *
     * @throws Exception если возникает ошибка при выполнении запроса
     */
    @Test
    @DisplayName("Попытка подписки на самого себя возвращает конфликт")
    void createSubscription_SelfSubscription_ReturnsConflict() throws Exception {
        Long userId = 1L;
        SubscriptionRequestDto request = new SubscriptionRequestDto();
        request.setOwnerId(userId);

        when(subscriptionService.subscribe(userId, request))
                .thenThrow(new ConflictException("Нельзя подписаться на самого себя"));

        mockMvc.perform(post("/users/{userId}/subscriptions", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}