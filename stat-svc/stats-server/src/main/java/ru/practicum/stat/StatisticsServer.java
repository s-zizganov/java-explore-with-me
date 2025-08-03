package ru.practicum.stat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс запуска Spring Boot приложения сервиса статистики.
 */
@SpringBootApplication
public class StatisticsServer {
    public static void main(String[] args) {
        SpringApplication.run(StatisticsServer.class, args);
    }
}