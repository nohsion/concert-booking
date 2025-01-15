package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.seat.SeatRepository;
import com.sion.concertbooking.domain.seat.SeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class SeatServiceTest {

    private SeatService sut;

    private SeatRepository seatRepository = mock(SeatRepository.class);

    @BeforeEach
    void setUp() {
        sut = new SeatService(seatRepository);
    }

    @DisplayName("존재하지 않는 좌석 ID로 조회하면 NoSuchElementException이 발생한다.")
    @Test
    void getSeatsByIdFail() {
        // given
        List<Long> seatId = List.of(1L, 2L);

        // when
        // then
        assertThatThrownBy(() -> sut.getSeatsById(seatId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("존재하지 않는 좌석입니다. seatId=1");
    }

}