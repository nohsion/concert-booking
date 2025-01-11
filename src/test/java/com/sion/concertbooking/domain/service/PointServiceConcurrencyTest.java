package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.info.PointInfo;
import com.sion.concertbooking.test.TestDataCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PointServiceConcurrencyTest {

    @Autowired
    PointService pointService;

    @Autowired
    TestDataCleaner testDataCleaner;

    @BeforeEach
    void setUp() {
        testDataCleaner.cleanUp();
    }

    @DisplayName("현재 포인트가 0원일 때, 100원씩 포인트를 20번 충전하면 총 2000원이 충전되어야 한다.")
    @Test
    void chargePointConcurrently() throws Exception {
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
                    pointService.chargePoint(userId, amount);
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
        assertThat(pointInfo.amount()).isEqualTo(2000);
    }

    @DisplayName("현재 포인트가 10,000원일 때 100원씩 포인트를 20번 사용하면, 도합 8000원이 남아야 한다.")
    @Test
    void usePointConcurrently() throws Exception {
        // given
        long userId = 1L;
        int amount = 100;
        pointService.createPoint(userId); // 포인트 지갑 생성
        pointService.chargePoint(userId, 10_000); // 테스트를 위해 포인트 충전을 미리 해놓는다.

        final int numOfExecute = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numOfExecute);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // when
        for (int i = 0; i < numOfExecute; i++) {
            executorService.submit(() -> {
                try {
                    pointService.usePoint(userId, amount);
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
        assertThat(pointInfo.amount()).isEqualTo(8000);
    }

    @DisplayName("현재 포인트가 1000원이고 (+1000원 충전) -> (-2000원 사용) 요청이 차례로 들어오면, 0원이 되어야 한다.")
    @Test
    void chargeAndUsePointConcurrently() throws Exception {
        // given
        long userId = 1L;
        pointService.createPoint(userId); // 포인트 지갑 생성
        pointService.chargePoint(userId, 1000); // 초기 포인트 1000원

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(2);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // when
        executorService.submit(() -> {
            try {
                pointService.chargePoint(userId, 1000);
                successCount.incrementAndGet();
            } catch (Exception e) {
                failCount.incrementAndGet();
            } finally {
                latch.countDown();
            }
        });

        executorService.submit(() -> {
            try {
                pointService.usePoint(userId, 2000);
                successCount.incrementAndGet();
            } catch (Exception e) {
                failCount.incrementAndGet();
            } finally {
                latch.countDown();
            }
        });
        latch.await();
        executorService.shutdown();

        // then
        assertThat(successCount.get()).isEqualTo(2);
        assertThat(failCount.get()).isZero();

        PointInfo pointInfo = pointService.getPointByUserId(userId);
        assertThat(pointInfo.amount()).isZero();
    }

    @DisplayName("현재 포인트가 1000원이고 (-2000원 사용) -> (+1000원 충전) 요청이 차례로 들어오면, 2000원이 되어야 한다.")
    @Test
    void useAndChargePointConcurrently() throws Exception {
        // given
        long userId = 1L;
        pointService.createPoint(userId); // 포인트 지갑 생성
        pointService.chargePoint(userId, 1000); // 초기 포인트 1000원

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(2);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // when
        executorService.submit(() -> {
            try {
                pointService.usePoint(userId, 2000);
                successCount.incrementAndGet();
            } catch (Exception e) {
                failCount.incrementAndGet();
            } finally {
                latch.countDown();
            }
        });

        executorService.submit(() -> {
            try {
                pointService.chargePoint(userId, 1000);
                successCount.incrementAndGet();
            } catch (Exception e) {
                failCount.incrementAndGet();
            } finally {
                latch.countDown();
            }
        });
        latch.await();
        executorService.shutdown();

        // then
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(1); // 첫번째 요청은 잔액부족으로 실패

        PointInfo pointInfo = pointService.getPointByUserId(userId);
        assertThat(pointInfo.amount()).isEqualTo(2000);
    }

}
