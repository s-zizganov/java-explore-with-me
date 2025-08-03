package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.dto.UserCreateDto;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с пользователями.
 * Содержит бизнес-логику создания, удаления и получения пользователей.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public User createUser(UserCreateDto userCreateDto) {
        if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new ConflictException("Пользователь с email " + userCreateDto.getEmail() + " уже существует");
        }
        User user = userMapper.toUser(userCreateDto);
        User createdUser = userRepository.save(user);
        log.info("Создан пользователь: {}", createdUser);
        return createdUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Пользователь с id " + id + " не существует");
        }
        userRepository.deleteById(id);
        log.info("Пользователь с id: {} успешно удален", id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers(List<Long> ids, int from, int size) {
        int page = from > 0 ? from / size : 0;
        Pageable pageable = PageRequest.of(page, size);
        return (ids != null) ? userRepository.findByIdIn(ids, pageable) : userRepository.findAll(pageable)
                .stream().collect(Collectors.toList());
    }
}
