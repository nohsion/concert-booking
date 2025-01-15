package com.sion.concertbooking.application;

import com.sion.concertbooking.application.payment.PaymentType;
import com.sion.concertbooking.application.point.PointCharger;
import com.sion.concertbooking.domain.point.PointInfo;
import com.sion.concertbooking.domain.point.PointService;
import com.sion.concertbooking.domain.pointhistory.PointHistoryService;
import com.sion.concertbooking.test.TestDataCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PointChargerTest {

    @Autowired
    PointCharger pointCharger;

    @Autowired
    PointService pointService;

    @Autowired
    PointHistoryService pointHistoryService;

    @Autowired
    TestDataCleaner testDataCleaner;

    @BeforeEach
    void setUp() {
        testDataCleaner.cleanUp();
    }

    @DisplayName("100원씩 20번 충전하면 총 2000원이 충전되고, 20번의 충전 내역이 기록되어야 한다.")
    @Test
    void chargePointCuncurrently() throws Exception {
        // given
        long userId = 1L;
        int amount = 100;
        pointService.createPoint(userId); // 포인트 지갑 생성

        final int numOfExecute = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numOfExecute);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // when
        for (int i = 0; i < numOfExecute; i++) {
            executorService.submit(() -> {
                try {
                    pointCharger.chargePoint(userId, amount, PaymentType.FREE);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        assertThat(successCount.get()).isEqualTo(numOfExecute);
        assertThat(failCount.get()).isZero();
        
        PointInfo pointInfo = pointService.getPointByUserId(userId);
        assertThat(pointInfo.amount())
                .as("총 충전된 금액은 2000원이어야 한다.")
                .isEqualTo(2000);

        assertThat(pointHistoryService.getChargedPointHistories(pointInfo.id(), PageRequest.of(0, 50)))
                .as("20번의 충전 내역이 기록되어야 한다.")
                .hasSize(numOfExecute);
    }
}