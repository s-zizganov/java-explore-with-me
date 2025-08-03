package ru.practicum.ewm.request.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.utils.RequestStatus;

import java.time.LocalDateTime;

/**
 * Сущность заявки на участие в событии.
 * Описывает связь между пользователем и событием, а также статус заявки.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request {
    /**
     * Уникальный идентификатор заявки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    /**
     * Событие, на участие в котором подана заявка.
     */
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    @ToString.Exclude
    Event event;
    /**
     * Пользователь, подавший заявку.
     */
    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    @ToString.Exclude
     User requester;
    /**
     * Статус заявки (например, CONFIRMED, PENDING, REJECTED).
     */
    @Enumerated(EnumType.STRING)
    RequestStatus status;
    /**
     * Дата и время создания заявки.
     */
    @Column(nullable = false)
    LocalDateTime created;
}
