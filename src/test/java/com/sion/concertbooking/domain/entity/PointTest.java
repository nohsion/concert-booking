package com.sion.concertbooking.domain.entity;

import com.sion.concertbooking.domain.model.entity.Point;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;

class PointTest {

    @DisplayName("사용자의 최대 잔고를 초과하여 충전을 시도하면 IllegalArgumentException이 발생한다.")
    @Test
    void chargeFailWhenMaxPoint() {
        // given
        int amount = 1000;
        Point point = Instancio.of(Point.class)
                .set(field(Point::getAmount), 99_999_999)
                .create();

        // when
        // then
        assertThatThrownBy(() -> point.chargePoint(amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("최대 잔고를 초과하여 충전할 수 없습니다.");
    }

    @DisplayName("한번에 충전 가능한 금액이 일정 금액을 초과하면 IllegalArgumentException이 발생한다.")
    @Test
    void chargeFailWhenOnceMaxPoint() {
        // given
        int amount = 2_000_001;
        Point point = Instancio.of(Point.class)
                .set(field(Point::getAmount), 0)
                .create();

        // when
        // then
        assertThatThrownBy(() -> point.chargePoint(amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("한번에 충전 가능한 금액을 초과했습니다.");
    }

    @DisplayName("0 이하의 금액을 충전할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void chargeFailWhenMinusAmount(int amount) {
        // given
        Point point = Instancio.of(Point.class)
                .set(field(Point::getAmount), 1000)
                .create();

        // when
        // then
        assertThatThrownBy(() -> point.chargePoint(amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("0 이하의 금액을 충전할 수 없습니다.");
    }

    @DisplayName("0 이하의 금액을 사용할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void useFailWhenMinusAmount(int amount) {
        // given
        Point point = Instancio.of(Point.class)
                .set(field(Point::getAmount), 1000)
                .create();

        // when
        // then
        assertThatThrownBy(() -> point.usePoint(amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("0 이하의 금액을 사용할 수 없습니다.");
    }

    @DisplayName("잔액보다 큰 금액을 사용할 수 없다.")
    @Test
    void useFailWhenOverAmount() {
        // given
        int existingAmount = 1000;
        int useAmount = 1001;
        Point point = Instancio.of(Point.class)
                .set(field(Point::getAmount), existingAmount)
                .create();

        // when
        // then
        assertThatThrownBy(() -> point.usePoint(useAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잔액이 부족합니다.");
    }
}