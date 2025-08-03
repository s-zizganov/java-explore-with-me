package ru.practicum.ewm.compilation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.compilation.controller.AdminCompilationController;
import ru.practicum.ewm.compilation.dto.CompilationCreateDto;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminCompilationController.class)
@DisplayName("Тестирование AdminCompilationController")
public class AdminCompilationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompilationService compilationService;

    @DisplayName("Успешное создание подборки")
    @Test
    void createCompilation_validDto_shouldReturnCreated() throws Exception {
        CompilationCreateDto dto = new CompilationCreateDto();
        dto.setTitle("Новая подборка");
        dto.setPinned(false);
        dto.setEvents(new HashSet<>(Arrays.asList(1L, 2L)));

        CompilationDto responseDto = new CompilationDto();
        responseDto.setId(1L);
        responseDto.setTitle(dto.getTitle());
        responseDto.setPinned(dto.getPinned());
        responseDto.setEvents(Collections.emptyList());

        when(compilationService.create(dto)).thenReturn(responseDto);

        mockMvc.perform(post("/admin/compilations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Новая подборка"));
    }

    @DisplayName("Создание подборки без названия должно вернуть ошибку")
    @Test
    void createCompilation_missingTitle_shouldReturnBadRequest() throws Exception {
        CompilationCreateDto dto = new CompilationCreateDto();
        dto.setPinned(false);

        mockMvc.perform(post("/admin/compilations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Успешное обновление подборки")
    @Test
    void updateCompilation_validData_shouldReturnUpdated() throws Exception {
        CompilationCreateDto updateDto = new CompilationCreateDto();
        updateDto.setTitle("Обновлённое название");
        updateDto.setPinned(true);
        updateDto.setEvents(Collections.singleton(3L));

        CompilationDto responseDto = new CompilationDto();
        responseDto.setId(1L);
        responseDto.setTitle("Обновлённое название");
        responseDto.setPinned(true);
        responseDto.setEvents(Collections.emptyList());

        when(compilationService.update(1L, updateDto)).thenReturn(responseDto);

        mockMvc.perform(patch("/admin/compilations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Обновлённое название"))
                .andExpect(jsonPath("$.pinned").value(true));
    }

    @DisplayName("Обновление несуществующей подборки должно вернуть 404")
    @Test
    void updateCompilation_nonExistentId_shouldReturnNotFound() throws Exception {
        CompilationCreateDto updateDto = new CompilationCreateDto();
        updateDto.setTitle("Новое название");

        when(compilationService.update(999L, updateDto))
                .thenThrow(new NotFoundException("Подборка с id 999 не найдена"));

        mockMvc.perform(patch("/admin/compilations/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Успешное удаление подборки")
    @Test
    void deleteCompilation_existingId_shouldReturnNoContent() throws Exception {
        doNothing().when(compilationService).delete(1L);

        mockMvc.perform(delete("/admin/compilations/1"))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Удаление несуществующей подборки должно вернуть 404")
    @Test
    void deleteCompilation_nonExistentId_shouldReturnNotFound() throws Exception {
        doThrow(new NotFoundException("Подборки с id 999 не существует"))
                .when(compilationService).delete(999L);

        mockMvc.perform(delete("/admin/compilations/999"))
                .andExpect(status().isNotFound());
    }
}