package ru.practicum.ewm.location;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.location.dto.LocationMapper;
import ru.practicum.ewm.location.model.Location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DisplayName("Тестирование LocationMapper")
public class LocationMapperTest {
    @Autowired
    LocationMapper locationMapper;

    @DisplayName("Преобразовать корректный LocationDto в Location")
    @Test
    void toLocation_allLocationDtoFieldsFilled_returnCorrectLocation() {
        LocationDto locationDto = new LocationDto(55.7558f, 37.6173f);
        Location location = locationMapper.toLocation(locationDto);
        assertEquals(location.getLatitude(), locationDto.getLat());
        assertEquals(location.getLongitude(), locationDto.getLon());
    }

    @DisplayName("Преобразовать null в Location")
    @Test
    void toLocation_withNullLocationDto_returnNullLocation() {
        Location location = locationMapper.toLocation(null);
        assertNull(location);
    }

    @DisplayName("Преобразовать корректный Location в LocationDto")
    @Test
    void toLocationDto_allLocationFieldsFilled_returnCorrectLocationDto() {
        Location location = new Location(1L, 55.7558f, 37.6173f);
        LocationDto locationDto = locationMapper.toLocationDto(location);
        assertEquals(location.getLatitude(), locationDto.getLat());
        assertEquals(location.getLongitude(), locationDto.getLon());
    }

    @DisplayName("Преобразовать null в LocationDto")
    @Test
    void toLocationDto_withNullLocation_returnNullLocationDto() {
        LocationDto locationDto = locationMapper.toLocationDto(null);
        assertNull(locationDto);
    }
}
