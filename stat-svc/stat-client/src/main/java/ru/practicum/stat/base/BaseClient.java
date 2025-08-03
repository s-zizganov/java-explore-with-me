package ru.practicum.stat.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;

/**
 * Базовый клиент для выполнения HTTP-запросов к внешним сервисам через RestTemplate.
 * Предоставляет методы для отправки GET и POST запросов с обработкой ошибок и стандартными заголовками.
 */
@Slf4j
public class BaseClient {

    /**
     * RestTemplate для выполнения HTTP-запросов.
     */
    protected final RestTemplate rest;
    private final String statsUri;

    /**
     * Конструктор базового клиента.
     * @param rest RestTemplate, используемый для отправки запросов
     */
    public BaseClient(RestTemplate rest,@Value("${stats-server.url}") String statsUri) {
        this.rest = rest;
        this.statsUri = statsUri;
    }


    /**
     * Выполняет GET-запрос к сервису статистики по указанному пути.
     * @param path путь для запроса
     * @return ответ от сервиса статистики
     */
    protected ResponseEntity<Object> get(String path) {
        return makeAndSendRequest(statsUri + path);
    }

    /**
     * Выполняет POST-запрос к сервису статистики для отправки данных (например, информации о хите).
     * @param body тело запроса
     * @return ответ от сервиса статистики
     */
    protected ResponseEntity<Object> post(Object body) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(body);
        try {
            log.info("Отправка POST запроса на URL: {}, тело: {}", statsUri + "/hit", body);
            ResponseEntity<Object> response = rest.postForEntity(statsUri + "/hit", requestEntity, Object.class);
            log.info("Получен ответ от сервиса статистики, статус: {}", response.getStatusCode());
            return response;
        } catch (HttpStatusCodeException e) {
            log.error("Ошибка при отправке POST запроса: {}, тело ответа: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    /**
     * Вспомогательный метод для отправки GET-запроса с обработкой ошибок.
     * @param path полный путь для запроса
     * @param <T> тип тела запроса (обычно null)
     * @return ответ от сервиса статистики
     */
    private <T> ResponseEntity<Object> makeAndSendRequest(String path) {
        HttpEntity<T> requestEntity = new HttpEntity<>(null, defaultHeaders());
        ResponseEntity<Object> responseEntity;
        try {
            log.info("Отправка GET запроса на URL: {}", path);
            responseEntity = rest.exchange(path, HttpMethod.GET, requestEntity, Object.class);
            log.info("Получен ответ от сервиса статистики, статус: {}", responseEntity.getStatusCode());
        } catch (HttpStatusCodeException e) {
            log.error("Ошибка при отправке GET запроса: {}, тело ответа: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
        return prepareResponse(responseEntity);
    }

    /**
     * Формирует стандартные заголовки для запросов (Content-Type и Accept: application/json).
     * @return HttpHeaders с нужными заголовками
     */
    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    /**
     * Преобразует ответ сервера: если успешный — возвращает как есть, иначе формирует новый ResponseEntity с телом или без.
     * @param response исходный ответ
     * @return подготовленный ответ
     */
    private static ResponseEntity<Object> prepareResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }
}