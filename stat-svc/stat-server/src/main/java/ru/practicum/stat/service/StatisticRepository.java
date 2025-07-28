package ru.practicum.stat.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stat.model.Hit;
import ru.practicum.stat.model.Statistic;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для работы с сущностями Hit и получения агрегированной статистики по посещениям.
 * Содержит методы для получения статистики с учётом уникальных IP и без, а также с фильтрацией по URI.
 */
public interface StatisticRepository extends JpaRepository<Hit, Long> {

    /**
     * Получает статистику по уникальным IP-адресам для заданных URI за указанный период.
     * @param start начало периода
     * @param end конец периода
     * @param uris список URI для фильтрации
     * @return список статистики по приложениям и URI
     */
    @Query("SELECT new ru.practicum.stat.model.Statistic(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "AND h.uri IN (?3) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<Statistic> findStatsUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    /**
     * Получает статистику по уникальным IP-адресам за указанный период для всех URI.
     * @param start начало периода
     * @param end конец периода
     * @return список статистики по приложениям и URI
     */
    @Query("SELECT new ru.practicum.stat.model.Statistic(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<Statistic> findStatsUniqueIpAllUris(LocalDateTime start, LocalDateTime end);

    /**
     * Получает статистику по всем посещениям (не только уникальным) для заданных URI за указанный период.
     * @param start начало периода
     * @param end конец периода
     * @param uris список URI для фильтрации
     * @return список статистики по приложениям и URI
     */
    @Query("SELECT new ru.practicum.stat.model.Statistic(h.app, h.uri, COUNT(h.ip)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "AND h.uri IN (?3) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<Statistic> findStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    /**
     * Получает статистику по всем посещениям (не только уникальным) за указанный период для всех URI.
     * @param start начало периода
     * @param end конец периода
     * @return список статистики по приложениям и URI
     */
    @Query("SELECT new ru.practicum.stat.model.Statistic(h.app, h.uri, COUNT(h.ip)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<Statistic> findStatsAllUris(LocalDateTime start, LocalDateTime end);
}
