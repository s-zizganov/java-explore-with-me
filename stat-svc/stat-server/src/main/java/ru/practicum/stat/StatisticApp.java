package ru.practicum.stat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс запуска Spring Boot приложения сервиса статистики.
 */
@SpringBootApplication
public class StatisticApp {
    /**
     * Точка входа в приложение. Запускает Spring Boot приложение.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(StatisticApp.class, args);
    }
}
