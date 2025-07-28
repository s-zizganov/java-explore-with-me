package ru.practicum.stat;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.stat.base.BaseClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Клиент для взаимодействия с сервисом статистики.
 * Позволяет отправлять информацию о посещениях и получать статистику по обращениям.
 */
@Service
public class StatisticsClient extends BaseClient {

    /**
     * Форматтер для преобразования даты и времени в строку нужного формата.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Имя приложения, от которого отправляются события в статистику.
     */
    private final String appName;

    /**
     * Конструктор клиента статистики.
     * @param serverUrl URL сервиса статистики
     * @param appName имя приложения (используется для идентификации источника событий)
     * @param builder RestTemplateBuilder для настройки RestTemplate
     */
    @Autowired
    public StatisticsClient(@Value("${stat-server.url}") String serverUrl,
                            @Value("${app.name}") String appName,
                            RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
        this.appName = appName;
    }

    /**
     * Отправляет информацию о посещении эндпоинта в сервис статистики.
     * @param request HTTP-запрос пользователя
     * @return ответ сервиса статистики
     */
    public ResponseEntity<Object> createHit(HttpServletRequest request) {
        HitCreateDto hitCreateDto = new HitCreateDto(appName, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        return post(hitCreateDto, "/hit");
    }

    /**
     * Получает статистику посещений за указанный период, с возможностью фильтрации по URI и уникальности IP.
     * @param start начало периода
     * @param end конец периода
     * @param uris список URI для фильтрации (может быть null или пустым)
     * @param unique учитывать только уникальные IP-адреса
     * @return ответ сервиса статистики с данными
     */
    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/stats")
                .queryParam("start", start.format(formatter))
                .queryParam("end", end.format(formatter))
                .queryParam("unique", unique);

        if (uris != null && !uris.isEmpty()) {
            builder.queryParam("uris", String.join(",", uris));
        }
        String url = builder.toUriString();
        return get(url);
    }
}