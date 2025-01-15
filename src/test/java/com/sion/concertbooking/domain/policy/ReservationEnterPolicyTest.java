package com.sion.concertbooking.domain.policy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationEnterPolicyTest {

    @DisplayName("현재 인원이 꽉찼다면 IllegalStateException이 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {10_000, 20_000})
    void validateIsAvailableForEntryWhenFull(int enteredCount) {
        // given
        ReservationEnterPolicy sut = new ReservationEnterPolicy();

        // when
        // then
        assertThatThrownBy(() -> sut.validateIsAvailableForEntry(enteredCount))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("현재 인원이 꽉차서 추가로 입장할 수 없습니다.");
    }

    @DisplayName("최대 인원 10_000명 중 8_000명이 입장해있고, 대기 인원이 5_000명이어도 한번에는 100명만 입장 가능하다.")
    @Test
    void getMaxLimitToEnter() {
        // given
        ReservationEnterPolicy sut = new ReservationEnterPolicy();

        // when
        int actual = sut.getMaxLimitToEnter(8_000, 5_000);

        // then
        assertThat(actual).isEqualTo(100);
    }

    @DisplayName("최대 인원 10_000명 중 8_000명이 입장해있고, 대기 인원이 50명이면 한번에 100명이 입장 가능해도 50명만 입장 가능하다.")
    @Test
    void getMaxLimitToEnterWhenWaitingIsLessThanMaxAdmission() {
        // given
        ReservationEnterPolicy sut = new ReservationEnterPolicy();

        // when
        int actual = sut.getMaxLimitToEnter(8_000, 50);

        // then
        assertThat(actual).isEqualTo(50);
    }


}