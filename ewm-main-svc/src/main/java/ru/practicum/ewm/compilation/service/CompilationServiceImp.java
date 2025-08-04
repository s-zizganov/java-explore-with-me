package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationCreateDto;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Реализация сервисного интерфейса для управления подборками событий.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CompilationServiceImp implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public CompilationDto create(CompilationCreateDto newCompilationDto) {
        Set<Long> eventIds = Optional.ofNullable(newCompilationDto.getEvents()).orElse(Collections.emptySet());
        List<Event> events = eventRepository.findAllByIdIn(new ArrayList<>(eventIds));

        Compilation compilation = compilationMapper.toCompilationWithEvents(newCompilationDto, new HashSet<>(events));
        compilation.setPinned(Optional.ofNullable(compilation.getPinned()).orElse(false));
        Compilation savedCompilation = compilationRepository.save(compilation);
        log.info("Подборка сохранена: {}", savedCompilation);

        List<EventShortDto> eventShortDtoList = events.stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());

        return compilationMapper.toCompilationDto(savedCompilation, eventShortDtoList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompilationDto update(Long compId, CompilationCreateDto updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка c id " + compId + " не найдена"));
        log.info("Обновление подборки c {}, на {}", updateCompilationRequest.toString(), compilation.toString());
        if (updateCompilationRequest.getEvents() != null) {
            Set<Long> eventIds = updateCompilationRequest.getEvents();
            List<Event> events = eventRepository.findAllByIdIn(new ArrayList<>(eventIds));
            compilation.setEvents(new HashSet<>(events));
            log.trace("Events = {}", compilation.getEvents());
        }
        compilation.setPinned(Optional.ofNullable(updateCompilationRequest.getPinned()).orElse(false));
        log.trace("Pinned = {}", compilation.getPinned());

        compilation.setTitle(Optional.ofNullable(updateCompilationRequest.getTitle()).orElse(compilation.getTitle()));
        Compilation updatedCompilation = compilationRepository.save(compilation);
        log.info("Подборка обновлена: {}", compilation);
        List<EventShortDto> eventShortDtoList = updatedCompilation.getEvents().stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());

        return compilationMapper.toCompilationDto(updatedCompilation, eventShortDtoList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        if (!compilationRepository.existsById(id)) {
            throw new NotFoundException("Подборки с id " + id + " не существует");
        }
        compilationRepository.deleteById(id);
        log.info("Подборка с id: {} успешно удален", id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<CompilationDto> getAllCompilations(Integer from, Integer size, Boolean pinned) {
        PageRequest pageRequest = PageRequest.of(from, size);
        List<Compilation> compilations;
        if (pinned != null) {
            log.info("Получение всех подборок с pinned: {}", pinned);
            compilations = compilationRepository.findAllByPinned(pinned, pageRequest);
            log.info("Получены подборки с pinned {}: {}", pinned, compilations);
        } else {
            log.info("Получение всех подборок без фильтрации по pinned");
            compilations = compilationRepository.findAll(pageRequest).getContent();
            log.info("Получены все подборки: {}", compilations);

        }
        return compilations.stream()
                .map(compilation -> {
                    List<EventShortDto> eventShortDtoList = compilation.getEvents().stream()
                            .map(eventMapper::toShortDto)
                            .collect(Collectors.toList());

                    return compilationMapper.toCompilationDto(compilation, eventShortDtoList);
                })
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public CompilationDto findCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка c id " + compId + " не найдена"));

        log.info("Подборка найдена: {}", compilation);

        List<EventShortDto> eventShortDtoList = compilation.getEvents().stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());

        return compilationMapper.toCompilationDto(compilation, eventShortDtoList);
    }
}