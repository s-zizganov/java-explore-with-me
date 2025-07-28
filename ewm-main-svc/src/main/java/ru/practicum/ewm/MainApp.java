package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс запуска Spring Boot приложения основного сервиса Explore With Me.
 */
@SpringBootApplication
public class MainApp {
    /**
     * Точка входа в приложение. Запускает Spring Boot приложение.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }
}