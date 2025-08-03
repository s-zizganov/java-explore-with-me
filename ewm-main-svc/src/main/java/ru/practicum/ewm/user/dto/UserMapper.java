package ru.practicum.ewm.user.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.user.model.User;

/**
 * Маппер для преобразования между сущностью User и её DTO.
 * Использует MapStruct для автоматического маппинга.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Преобразует UserCreateDto в сущность User.
     * Игнорирует поле id.
     *
     * @param userCreateDto DTO с данными пользователя
     * @return сущность User
     */
    @Mapping(target = "id", ignore = true)
    User toUser(UserCreateDto userCreateDto);

    /**
     * Преобразует сущность User в UserShortDto (краткое представление пользователя).
     *
     * @param user сущность пользователя
     * @return краткое DTO пользователя
     */
    UserShortDto toShortDto(User user);
}
