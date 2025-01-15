package com.sion.concertbooking.domain.policy;

import org.springframework.stereotype.Component;

@Component
public class ReservationEnterPolicy {

    private static final int MAX_TOKENS_PER_ADMISSION = 100; // 한 번에 입장 가능한 인원 제한
    private static final int MAX_TOKENS_ENTERED = 10_000; // 최대 입장 가능한 인원 제한

    /**
     * 현재 입장 가능한 상태인지 검증한다.
     */
    public void validateIsAvailableForEntry(int enteredCount) {
        if (enteredCount >= MAX_TOKENS_ENTERED) {
            throw new IllegalStateException("현재 인원이 꽉차서 추가로 입장할 수 없습니다.");
        }
    }

    /**
     * 대기중인 인원 중에서 한 번에 입장 가능한 인원수 만큼 차례로 입장시키기 위한 최대 인원수를 구한다.
     * @param enteredCount 현재 입장한 인원수
     * @param waitingCount 대기중인 인원수
     * @return 최대로 입장 가능한 인원수
     */
    public int getMaxLimitToEnter(int enteredCount, int waitingCount) {
        int maxAdmissionCount = Math.min(MAX_TOKENS_ENTERED - enteredCount, MAX_TOKENS_PER_ADMISSION);
        return Math.min(waitingCount, maxAdmissionCount);
    }
}
