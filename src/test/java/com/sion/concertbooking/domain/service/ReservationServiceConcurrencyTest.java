package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.command.ReservationCreateCommand;
import com.sion.concertbooking.domain.entity.Concert;
import com.sion.concertbooking.domain.entity.ConcertSchedule;
import com.sion.concertbooking.domain.entity.Reservation;
import com.sion.concertbooking.domain.entity.Seat;
import com.sion.concertbooking.domain.enums.ReservationStatus;
import com.sion.concertbooking.domain.enums.SeatGrade;
import com.sion.concertbooking.domain.repository.ConcertRepository;
import com.sion.concertbooking.domain.repository.ConcertScheduleRepository;
import com.sion.concertbooking.domain.repository.SeatRepository;
import com.sion.concertbooking.infrastructure.repository.ReservationJpaRepository;
import com.sion.concertbooking.test.TestDataCleaner;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@SpringBootTest
class ReservationServiceConcurrencyTest {

    @Autowired
    ReservationService reservationService;

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    ConcertRepository concertRepository;

    @Autowired
    ConcertScheduleRepository concertScheduleRepository;

    @Autowired
    ReservationJpaRepository reservationJpaRepository;

    @Autowired
    TestDataCleaner testDataCleaner;

    @BeforeEach
    void setUp() {
        testDataCleaner.cleanUp();
    }

    @DisplayName("동시에 같은 좌석을 3명이 시도하면, 1명만 성공하고 2명은 실패해야 한다.")
    @Test
    void createReservationsConcurrently() throws Exception {
        // given
        Concert concert = concertRepository.save(createConcert());
        ConcertSchedule concertSchedule = concertScheduleRepository.save(createConcertSchedule(concert.getId()));
        Seat seat1 = seatRepository.save(createSeat(1, SeatGrade.VIP, 170_000));
        Seat seat2 = seatRepository.save(createSeat(2, SeatGrade.VIP, 170_000));

        long concertId = concert.getId();
        long concertScheduleId = concertSchedule.getId();
        LocalDateTime now = LocalDateTime.of(2025, 1, 9, 1, 0);
        LocalDateTime playDateTime = LocalDateTime.of(2025, 1, 20, 17, 0);

        List<ReservationCreateCommand.SeatCreateCommand> seatCreateCommands = List.of(
                new ReservationCreateCommand.SeatCreateCommand(seat1.getId(), seat1.getSeatNum(), seat1.getSeatGrade(), seat1.getSeatPrice()),
                new ReservationCreateCommand.SeatCreateCommand(seat2.getId(), seat1.getSeatNum(), seat2.getSeatGrade(), seat2.getSeatPrice())
        );
        ReservationCreateCommand createCommandUser1 = new ReservationCreateCommand(
                1L, concertId, "지킬앤하이드", concertScheduleId, playDateTime, now,
                seatCreateCommands);
        ReservationCreateCommand createCommandUser2 = new ReservationCreateCommand(
                2L, concertId, "지킬앤하이드", concertScheduleId, playDateTime, now,
                seatCreateCommands);
        ReservationCreateCommand createCommandUser3 = new ReservationCreateCommand(
                3L, concertId, "지킬앤하이드", concertScheduleId, playDateTime, now,
                seatCreateCommands);

        final int numOfExecute = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numOfExecute);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // when
        executorService.submit(() -> {
            try {
                reservationService.createReservations(createCommandUser1);
                successCount.incrementAndGet();
            } catch (Exception e) {
                failCount.incrementAndGet();
            } finally {
                latch.countDown();
            }
        });
        executorService.submit(() -> {
            try {
                reservationService.createReservations(createCommandUser2);
                successCount.incrementAndGet();
            } catch (Exception e) {
                failCount.incrementAndGet();
            } finally {
                latch.countDown();
            }
        });
        executorService.submit(() -> {
            try {
                reservationService.createReservations(createCommandUser3);
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
        assertThat(failCount.get()).isEqualTo(2);

        List<Reservation> allReservations = reservationJpaRepository.findAll();
        assertThat(allReservations)
                .as("오직 유저 한명이 시도한 좌석 2개에 대한 예약만 생성되어야 하고 초기상태는 SUSPEND여야 한다.")
                .hasSize(2);
        assertThat(allReservations.get(0).getUserId())
                .isEqualTo(allReservations.get(1).getUserId());
        allReservations.forEach(reservation -> {
            assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.SUSPEND);
        });
    }

    private Concert createConcert() {
        return Instancio.of(Concert.class)
                .set(field(Concert::getId), null)
                .create();
    }

    private ConcertSchedule createConcertSchedule(long concertId) {
        return Instancio.of(ConcertSchedule.class)
                .set(field(ConcertSchedule::getId), null)
                .set(field(ConcertSchedule::getConcertId), concertId)
                .create();
    }

    private Seat createSeat(int seatNum, SeatGrade seatGrade, int seatPrice) {
        return Instancio.of(Seat.class)
                .set(field(Seat::getId), null)
                .set(field(Seat::getTheatreId), 1L)
                .set(field(Seat::getSeatNum), seatNum)
                .set(field(Seat::getSeatGrade), seatGrade)
                .set(field(Seat::getSeatPrice), seatPrice)
                .create();
    }


}
