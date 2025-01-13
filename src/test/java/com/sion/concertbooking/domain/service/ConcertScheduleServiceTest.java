package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.concertschedule.ConcertScheduleService;
import com.sion.concertbooking.domain.concertschedule.ConcertScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConcertScheduleServiceTest {

    private ConcertScheduleService sut;

    private ConcertScheduleRepository concertScheduleRepository = mock(ConcertScheduleRepository.class);

    @BeforeEach
    void setUp() {
        sut = new ConcertScheduleService(concertScheduleRepository);
    }

    @DisplayName("존재하지 않는 공연 일정 ID로 조회하면 IllegalArgumentException이 발생한다.")
    @Test
    void getConcertScheduleByIdFail() {
        // given
        long concertScheduleId = 1L;

        when(concertScheduleRepository.findById(concertScheduleId))
                .thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> sut.getConcertScheduleById(concertScheduleId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 공연 일정입니다.");
    }
}