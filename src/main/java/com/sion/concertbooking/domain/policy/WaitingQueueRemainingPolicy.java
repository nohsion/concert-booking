package com.sion.concertbooking.domain.policy;

import com.sion.concertbooking.domain.watingqueue.WaitingQueueInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WaitingQueueRemainingPolicy {

    private static final int WAITING_TIME_PER_PERSON_SEC = 30;
    private static final int MIN_WAIT_TIME_SEC = 5;

    /**
     * 대기열에서 타겟 대기자의 남은 대기 순서를 계산합니다.
     * @param target 대상 대기열
     * @param waitingQueues 대기중인 모든 대기열
     * @return 타겟 대기자의 남은 대기 순서
     */
    public int calculateRemainingWaitingOrder(WaitingQueueInfo target, List<WaitingQueueInfo> waitingQueues) {
        int targetIndex = waitingQueues.indexOf(target);
        if (targetIndex == -1) {
            throw new IllegalArgumentException("대기열에 존재하지 않는 대기자입니다. tokenId=" + target.tokenId());
        }
        return targetIndex + 1;
    }

    /**
     * 대기열에서 타겟 대기자의 남은 대기 시간을 계산합니다.
     * @param target 대상 대기열
     * @param waitingQueues 대기중인 모든 대기열
     * @param processingQueues 입장해서 처리중인 대기열
     * @param processingCapacity 수용 가능한 최대 인원
     * @return 타겟 대기자의 남은 대기 시간 (초)
     */
    public int calculateRemainingTimeSec(
            WaitingQueueInfo target,
            List<WaitingQueueInfo> waitingQueues,
            List<WaitingQueueInfo> processingQueues,
            int processingCapacity
    ) {
        int targetIndex = waitingQueues.indexOf(target);
        if (targetIndex == -1) {
            throw new IllegalArgumentException("대기열에 존재하지 않는 대기자입니다. tokenId=" + target.tokenId());
        }

        // 수용 가능한 최대 인원 중, 입장해서 처리중인 사람의 비율
        double processingRatio = (double) processingQueues.size() / processingCapacity;

        // 한 사람당 추정 대기 시간 (입장한 사람 비율에 따라 조정)
        double estimatedWaitTimePerPersonSec = WAITING_TIME_PER_PERSON_SEC * (1 - processingRatio);

        // 대기 시간이 너무 짧아지지 않도록 최소 대기 시간을 설정
        double waitTimePerPersonSec = Math.max(estimatedWaitTimePerPersonSec, MIN_WAIT_TIME_SEC);

        // 대기 순서
        int waitingOrder = targetIndex + 1;

        return (int) (waitingOrder * waitTimePerPersonSec);
    }
}
