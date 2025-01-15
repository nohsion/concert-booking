package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.concert.ConcertService;
import com.sion.concertbooking.domain.concert.ConcertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConcertServiceTest {

    private ConcertService sut;

    private ConcertRepository concertRepository = mock(ConcertRepository.class);

    @BeforeEach
    void setUp() {
        sut = new ConcertService(concertRepository);
    }

    @DisplayName("존재하지 않는 콘서트 ID로 조회하면 NoSuchElementException 발생한다.")
    @Test
    void getConcertByIdFail() {
        // given
        long concertId = 1L;

        when(concertRepository.findById(concertId))
                .thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> sut.getConcertById(concertId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("존재하지 않는 콘서트입니다. concertId=" + concertId);
    }
}