package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class SeatServiceTest {

    private SeatService sut;

    private SeatRepository seatRepository = mock(SeatRepository.class);

    @BeforeEach
    void setUp() {
        sut = new SeatService(seatRepository);
    }

    @DisplayName("존재하지 않는 좌석 ID로 조회하면 IllegalArgumentException이 발생한다.")
    @Test
    void getSeatByIdFail() {
        // given
        long seatId = 1L;

        // when
        // then
        assertThatThrownBy(() -> sut.getSeatById(seatId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 좌석입니다.");
    }

}