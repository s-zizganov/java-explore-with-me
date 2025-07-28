package ru.practicum.stat.base;

import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Базовый клиент для выполнения HTTP-запросов к внешним сервисам через RestTemplate.
 * Предоставляет методы для отправки GET и POST запросов с обработкой ошибок и стандартными заголовками.
 */
public class BaseClient {

    /**
     * RestTemplate для выполнения HTTP-запросов.
     */
    protected final RestTemplate restTemplate;

    /**
     * Конструктор базового клиента.
     * @param rest RestTemplate, используемый для отправки запросов
     */
    public BaseClient(RestTemplate rest) {
        this.restTemplate = rest;
    }

    /**
     * Выполняет GET-запрос по указанному пути.
     * @param path путь запроса
     * @return ответ сервера
     */
    protected ResponseEntity<Object> get(String path) {
        return sendRequest(path);
    }

    /**
     * Выполняет POST-запрос с телом запроса по указанному пути.
     * @param body тело запроса
     * @param path путь запроса
     * @return ответ сервера
     */
    protected ResponseEntity<Object> post(Object body, String path) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(body);
        return restTemplate.postForEntity(path, requestEntity, Object.class);
    }

    /**
     * Вспомогательный метод для выполнения GET-запроса с обработкой ошибок.
     * @param path путь запроса
     * @return ответ сервера
     */
    private <T> ResponseEntity<Object> sendRequest(String path) {
        HttpEntity<T> requestEntity = new HttpEntity<>(null, defaultHeaders());

        ResponseEntity<Object> responseEntity;
        try {
            responseEntity = restTemplate.exchange(path, HttpMethod.GET, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            // В случае ошибки возвращаем статус и тело ошибки
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
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