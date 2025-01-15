package com.sion.concertbooking.domain.policy;

import com.sion.concertbooking.domain.watingqueue.WaitingQueueInfo;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class WaitingQueueRemainingPolicyTest {

    @DisplayName("대기열에 타겟 대기자가 없다면 IllegalArgumentException 예외가 발생한다.")
    @Test
    void calculateRemainingWaitingOrderWhenTargetIsNotInWaitingQueues() {
        // given
        WaitingQueueRemainingPolicy sut = new WaitingQueueRemainingPolicy();

        List<WaitingQueueInfo> waitingQueues = Instancio.ofList(WaitingQueueInfo.class).size(3).create();
        WaitingQueueInfo target = Instancio.of(WaitingQueueInfo.class).create();

        // when
        // then
        assertThatThrownBy(() -> sut.calculateRemainingWaitingOrder(target, waitingQueues))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("대기열에 존재하지 않는 대기자입니다. tokenId=" + target.tokenId());
    }

    @DisplayName("대기열에 타겟 대기자가 없다면 IllegalArgumentException 예외가 발생한다.")
    @Test
    void calculateRemainingTimeSecWhenTargetIsNotInWaitingQueues() {
        // given
        WaitingQueueRemainingPolicy sut = new WaitingQueueRemainingPolicy();

        List<WaitingQueueInfo> waitingQueues = Instancio.ofList(WaitingQueueInfo.class).size(3).create();
        WaitingQueueInfo target = Instancio.of(WaitingQueueInfo.class).create();
        List<WaitingQueueInfo> processingQueues = Instancio.ofList(WaitingQueueInfo.class).size(1).create();
        int processingCapacity = 10_000;

        // when
        // then
        assertThatThrownBy(() -> sut.calculateRemainingTimeSec(target, waitingQueues, processingQueues, processingCapacity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("대기열에 존재하지 않는 대기자입니다. tokenId=" + target.tokenId());
    }

    @DisplayName("대기중인 대기열 중에서 3번째 인덱스에 위치해있다면 대기 순서 4를 반환한다.")
    @Test
    void calculateRemainingWaitingOrder() {
        // given
        WaitingQueueRemainingPolicy sut = new WaitingQueueRemainingPolicy();

        List<WaitingQueueInfo> waitingQueuesInFrontOfTarget = Instancio.ofList(WaitingQueueInfo.class).size(3)
                .create();
        WaitingQueueInfo target = Instancio.of(WaitingQueueInfo.class).create();
        List<WaitingQueueInfo> waitingQueuesBehindTarget = Instancio.ofList(WaitingQueueInfo.class).size(3)
                .create();

        List<WaitingQueueInfo> waitingQueues = new ArrayList<>();
        waitingQueues.addAll(waitingQueuesInFrontOfTarget);
        waitingQueues.add(target);
        waitingQueues.addAll(waitingQueuesBehindTarget);

        // when
        int waitingQueueRemaining = sut.calculateRemainingWaitingOrder(target, waitingQueues);

        // then
        assertThat(waitingQueueRemaining).isEqualTo(4);
    }

    @DisplayName("타겟 대기자가 대기열에서 첫번째 순서이면서 처리중인 비율이 90%라면, 대기 시간은 최소 대기시간인 5초여야 한다.")
    @Test
    void calculateRemainingTimeSecWhenTargetIsFirstAndProcessingRatioIsHigh() {
        // given
        WaitingQueueRemainingPolicy sut = new WaitingQueueRemainingPolicy();

        List<WaitingQueueInfo> waitingQueues = Instancio.ofList(WaitingQueueInfo.class).size(3).create();
        WaitingQueueInfo target = waitingQueues.get(0);
        List<WaitingQueueInfo> processingQueues = Instancio.ofList(WaitingQueueInfo.class).size(9_000).create();
        int processingCapacity = 10_000;

        // when
        int remainingTimeSec = sut.calculateRemainingTimeSec(target, waitingQueues, processingQueues, processingCapacity);

        // then
        assertThat(remainingTimeSec).isEqualTo(5);
    }

    @DisplayName("타겟 대기자가 대기열에서 첫번째 순서이면서 처리중인 비율이 1%라면, 대기 시간은 29초여야 한다.")
    @Test
    void calculateRemainingTimeSecWhenTargetIsFirstAndProcessingRatioIsLow() {
        // given
        WaitingQueueRemainingPolicy sut = new WaitingQueueRemainingPolicy();

        List<WaitingQueueInfo> waitingQueues = Instancio.ofList(WaitingQueueInfo.class).size(3).create();
        WaitingQueueInfo target = waitingQueues.get(0);
        List<WaitingQueueInfo> processingQueues = Instancio.ofList(WaitingQueueInfo.class).size(100).create();
        int processingCapacity = 10_000;

        // when
        int remainingTimeSec = sut.calculateRemainingTimeSec(target, waitingQueues, processingQueues, processingCapacity);

        // then
        assertThat(remainingTimeSec).isEqualTo(29);
    }

    @DisplayName("타겟 대기자가 대기열에서 100번째 순서이면서 처리중인 비율이 높다면, 대기 시간은 (최소 대기시간인 5초) * 100 = 500초여야 한다.")
    @Test
    void calculateRemainingTimeSecWhenTargetIsHundredthAndProcessingRatioIsHigh() {
        // given
        WaitingQueueRemainingPolicy sut = new WaitingQueueRemainingPolicy();

        List<WaitingQueueInfo> waitingQueues = Instancio.ofList(WaitingQueueInfo.class).size(100).create();
        WaitingQueueInfo target = waitingQueues.get(99);
        List<WaitingQueueInfo> processingQueues = Instancio.ofList(WaitingQueueInfo.class).size(9_000).create();
        int processingCapacity = 10_000;

        // when
        int remainingTimeSec = sut.calculateRemainingTimeSec(target, waitingQueues, processingQueues, processingCapacity);

        // then
        assertThat(remainingTimeSec).isEqualTo(500);
    }

    @DisplayName("타겟 대기자가 대기열에서 100번째 순서이면서 처리중인 비율이 1%라면, 대기 시간은 29.7 * 100 = 2970초여야 한다.")
    @Test
    void calculateRemainingTimeSecWhenTargetIsHundredthAndProcessingRatioIsLow() {
        // given
        WaitingQueueRemainingPolicy sut = new WaitingQueueRemainingPolicy();

        List<WaitingQueueInfo> waitingQueues = Instancio.ofList(WaitingQueueInfo.class).size(100).create();
        WaitingQueueInfo target = waitingQueues.get(99);
        List<WaitingQueueInfo> processingQueues = Instancio.ofList(WaitingQueueInfo.class).size(100).create();
        int processingCapacity = 10_000;

        // when
        int remainingTimeSec = sut.calculateRemainingTimeSec(target, waitingQueues, processingQueues, processingCapacity);

        // then
        assertThat(remainingTimeSec).isEqualTo(2970);
    }
}