package com.artembredak.sporteventscalendar.service;

import com.artembredak.sporteventscalendar.domain.model.Sport;
import com.artembredak.sporteventscalendar.domain.repository.SportRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class SportServiceTest {

    @Mock
    SportRepository sportRepository;

    @InjectMocks
    SportService sportService;

    @Test
    @DisplayName("getAllSports_returnsAll: delegates to repository and returns all sports")
    void getAllSports_returnsAll() {
        List<Sport> expected = List.of(new Sport(1L, "Football"), new Sport(2L, "Basketball"));
        given(sportRepository.findAll()).willReturn(expected);

        List<Sport> result = sportService.findAll();

        assertThat(result).hasSize(2).isEqualTo(expected);
        then(sportRepository).should().findAll();
    }

    @Test
    @DisplayName("getSportById_found: returns Optional containing the sport when it exists")
    void getSportById_found() {
        Sport sport = new Sport(1L, "Football");
        given(sportRepository.findById(1L)).willReturn(Optional.of(sport));

        Optional<Sport> result = sportService.findById(1L);

        assertThat(result).isPresent().contains(sport);
        then(sportRepository).should().findById(1L);
    }

    @Test
    @DisplayName("getSportById_notFound_throwsException: returns empty Optional when sport does not exist")
    void getSportById_notFound_returnsEmpty() {
        given(sportRepository.findById(99L)).willReturn(Optional.empty());

        Optional<Sport> result = sportService.findById(99L);

        assertThat(result).isEmpty();
        then(sportRepository).should().findById(99L);
    }
}