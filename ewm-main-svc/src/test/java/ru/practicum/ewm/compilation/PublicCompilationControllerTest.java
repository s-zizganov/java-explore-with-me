package ru.practicum.ewm.compilation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.compilation.controller.PublicCompilationController;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublicCompilationController.class)
@DisplayName("Тестирование PublicCompilationController")
public class PublicCompilationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompilationService compilationService;

    @DisplayName("Получение всех подборок")
    @Test
    void getAllCompilations_withoutFilter_shouldReturnList() throws Exception {
        CompilationDto dto1 = new CompilationDto();
        dto1.setId(1L);
        dto1.setTitle("Подборка 1");
        dto1.setPinned(false);

        CompilationDto dto2 = new CompilationDto();
        dto2.setId(2L);
        dto2.setTitle("Подборка 2");
        dto2.setPinned(true);

        List<CompilationDto> list = Arrays.asList(dto1, dto2);
        when(compilationService.getAllCompilations(0, 10, null)).thenReturn(list);

        mockMvc.perform(get("/compilations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Подборка 1"))
                .andExpect(jsonPath("$[1].pinned").value(true));
    }

    @DisplayName("Получение подборок с фильтром pinned = true")
    @Test
    void getAllCompilations_withPinnedTrue_shouldReturnFiltered() throws Exception {
        CompilationDto dto = new CompilationDto();
        dto.setId(1L);
        dto.setTitle("Pinned подборка");
        dto.setPinned(true);

        when(compilationService.getAllCompilations(0, 10, true)).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/compilations")
                        .param("pinned", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].pinned").value(true));
    }

    @DisplayName("Получение подборок с заданными from и size")
    @Test
    void getAllCompilations_withCustomFromAndSize_shouldUseThem() throws Exception {
        when(compilationService.getAllCompilations(5, 20, null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/compilations")
                        .param("from", "5")
                        .param("size", "20"))
                .andExpect(status().isOk());
    }

    @DisplayName("Получение подборки по ID")
    @Test
    void getCompilationById_existingId_shouldReturnDto() throws Exception {
        CompilationDto dto = new CompilationDto();
        dto.setId(1L);
        dto.setTitle("Тестовая подборка");
        dto.setPinned(false);

        when(compilationService.findCompilationById(1L)).thenReturn(dto);

        mockMvc.perform(get("/compilations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Тестовая подборка"));
    }

    @DisplayName("Получение несуществующей подборки по ID")
    @Test
    void getCompilationById_nonExistentId_shouldReturnNotFound() throws Exception {
        when(compilationService.findCompilationById(999L))
                .thenThrow(new NotFoundException("Подборка с id 999 не найдена"));

        mockMvc.perform(get("/compilations/999"))
                .andExpect(status().isNotFound());
    }
}