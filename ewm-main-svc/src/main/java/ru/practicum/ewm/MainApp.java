package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс запуска Spring Boot приложения Explore With Me.
 * Сканирует все необходимые пакеты и инициализирует приложение.
 */
@SpringBootApplication(scanBasePackages = {"ru.practicum.ewm", "ru.practicum.stat"})
public class MainApp {
    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }
}