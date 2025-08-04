package ru.practicum.ewm.compilation.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.model.Event;

import java.util.Set;

/**
 * Сущность подборки событий для хранения в базе данных.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compilations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Compilation {
    /**
     * Уникальный идентификатор подборки (генерируется автоматически).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    /**
     * Признак закрепления подборки на главной странице.
     */
    @Column(nullable = false)
    Boolean pinned;
    /**
     * Заголовок подборки (обязательный, не более 50 символов).
     */
    @Column(nullable = false, length = 50)
    String title;
    /**
     * Множество событий, входящих в подборку.
     */
    @ManyToMany
    @JoinTable(name = "compilations_event",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    @ToString.Exclude
    Set<Event> events;
}
