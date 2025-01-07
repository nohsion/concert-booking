package com.sion.concertbooking.domain.entity;

import com.sion.concertbooking.domain.enums.ReservationStatus;
import com.sion.concertbooking.domain.enums.SeatGrade;
import com.sion.concertbooking.domain.model.entity.Reservation;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

class ReservationTest {

    @DisplayName("예약의 취소가능시간은 현재시간으로부터 7일 후이다.")
    @Test
    void cancelableTimeIs7DaysAfterNow() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 6, 23, 0, 0);
        LocalDateTime playDateTime = LocalDateTime.of(2025, 2, 1, 19, 0, 0);

        // when
        Reservation reservation = Reservation.of(
                1L, 1L, "콘서트 타이틀",
                1L, playDateTime, now,
                1L, 1, SeatGrade.VIP, 100_000
        );

        // then
        assertThat(reservation.getCancelDeadlineDateTime()).isEqualTo(now.plusDays(7));
    }

    @DisplayName("예약의 취소가능시간은 현재시간으로부터 7일 후이지만, 공연 시작 하루전까지만 가능하다.")
    @Test
    void cancelableTimeIs1DayBeforePlay() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 6, 23, 0, 0);
        LocalDateTime playDateTime = LocalDateTime.of(2025, 1, 10, 19, 0, 0);

        // when
        Reservation reservation = Reservation.of(
                1L, 1L, "콘서트 타이틀",
                1L, playDateTime, now,
                1L, 1, SeatGrade.VIP, 100_000
        );

        // then
        assertThat(reservation.getCancelDeadlineDateTime()).isEqualTo(playDateTime.minusDays(1));
    }

    @DisplayName("예약 만료 시간이 지났다면 결제 대기 상태가 아니다.")
    @Test
    void isSupendFalseWhenExpired() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 6, 23, 0, 0);
        Reservation reservation = Instancio.of(Reservation.class)
                .set(field(Reservation::getStatus), ReservationStatus.SUSPEND)
                .set(field(Reservation::getExpiredAt), now.minusMinutes(1))
                .create();

        // when
        boolean result = reservation.isSuspend(now);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("예약 만료 시간이 지나지 않았다면 결제 대기 상태이다.")
    @Test
    void isSupendTrueWhenNotExpired() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 6, 23, 0, 0);
        Reservation reservation = Instancio.of(Reservation.class)
                .set(field(Reservation::getStatus), ReservationStatus.SUSPEND)
                .set(field(Reservation::getExpiredAt), now.plusMinutes(1))
                .create();

        // when
        boolean result = reservation.isSuspend(now);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("현재시간보다 만료시간이 지났다면 true를 반환한다.")
    @Test
    void isExpiredTrueWhenExpired() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 6, 23, 0, 0);
        Reservation reservation = Instancio.of(Reservation.class)
                .set(field(Reservation::getExpiredAt), now.minusMinutes(1))
                .create();

        // when
        boolean result = reservation.isExpired(now);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("현재시간보다 만료시간과 완전히 동일하다면 false를 반환한다.")
    @Test
    void isExpiredFalseWhenEqual() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 6, 23, 0, 0);
        Reservation reservation = Instancio.of(Reservation.class)
                .set(field(Reservation::getExpiredAt), now)
                .create();

        // when
        boolean result = reservation.isExpired(now);

        // then
        assertThat(result).isFalse();
    }

}