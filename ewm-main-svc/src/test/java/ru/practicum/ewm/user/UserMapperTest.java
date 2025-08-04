package ru.practicum.ewm.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.ewm.user.dto.UserCreateDto;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DisplayName("Тестирование UserMapper")
public class UserMapperTest {
    @Autowired
    UserMapper userMapper;

    @DisplayName("Преобразовать корректный UserCreateDto в User")
    @Test
    void toUser_allUserCreateDtoFieldsFilled_returnCorrectUser() {
        UserCreateDto userCreateDto = new UserCreateDto("User name", "User@mail.ru");
        User user = userMapper.toUser(userCreateDto);
        assertEquals(userCreateDto.getEmail(), user.getEmail());
        assertEquals(userCreateDto.getName(), user.getName());
    }

    @DisplayName("Преобразовать null в User")
    @Test
    void toUser_withNullUserCreateDto_returnNullUser() {
        User user = userMapper.toUser(null);
        assertNull(user);
    }

    @DisplayName("Преобразовать корректный User в UserShortDto")
    @Test
    void toUser_allUserFieldsFilled_returnCorrectUserShortDto() {
        User user = new User(1L, "User name", "User@mail.ru");
        UserShortDto shortDto = userMapper.toShortDto(user);
        assertEquals(user.getName(), shortDto.getName());
        assertEquals(user.getId(), shortDto.getId());
    }

    @DisplayName("Преобразовать null в UserShortDto")
    @Test
    void toUser_withNullUser_returnNullUserShortDto() {
        UserShortDto shortDto = userMapper.toShortDto(null);
        assertNull(shortDto);
    }
}
