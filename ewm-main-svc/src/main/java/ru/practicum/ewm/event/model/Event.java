package ru.practicum.ewm.event.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.utils.EventState;

import java.time.LocalDateTime;

/**
 * Сущность события.
 * Описывает все основные параметры события, включая связи с пользователем, категорией и локацией.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    /**
     * Уникальный идентификатор события.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * Местоположение события.
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    @ToString.Exclude
    Location location;

    /**
     * Инициатор события (пользователь).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    @ToString.Exclude
    User initiator;

    /**
     * Категория события.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    Category category;

    /**
     * Заголовок события.
     */
    @Column(length = 120)
    String title;

    /**
     * Краткое описание события (аннотация).
     */
    @Column(length = 2000)
    String annotation;

    /**
     * Полное описание события.
     */
    @Column(length = 7000)
    String description;

    /**
     * Дата и время проведения события.
     */
    @Column(name = "event_date")
    LocalDateTime eventDate;

    /**
     * Признак платности события.
     */
    Boolean paid;
    /**
     * Лимит участников события.
     */
    @Column(name = "participant_limit")
    Integer participantLimit;
    /**
     * Требуется ли модерация заявок на участие.
     */
    @Column(name = "request_moderation")
    Boolean requestModeration;

    /**
     * Текущее состояние события.
     */
    @Enumerated(EnumType.STRING)
    EventState state;

    /**
     * Дата и время создания события.
     */
    @Column(name = "created_on")
    LocalDateTime createdOn;

    /**
     * Дата и время публикации события.
     */
    @Column(name = "published_on")
    LocalDateTime publishedOn;

    /**
     * Количество подтверждённых заявок на участие.
     */
    @Column(name = "confirmed_requests")
    int confirmedRequests;
    /**
     * Количество просмотров события.
     */
    long views;
}
