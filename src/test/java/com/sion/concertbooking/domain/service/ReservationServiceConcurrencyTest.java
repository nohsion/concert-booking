package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.concert.Concert;
import com.sion.concertbooking.domain.concert.ConcertRepository;
import com.sion.concertbooking.domain.concertschedule.ConcertSchedule;
import com.sion.concertbooking.domain.concertschedule.ConcertScheduleRepository;
import com.sion.concertbooking.domain.reservation.Reservation;
import com.sion.concertbooking.domain.reservation.ReservationCreateCommand;
import com.sion.concertbooking.domain.reservation.ReservationService;
import com.sion.concertbooking.domain.reservation.ReservationStatus;
import com.sion.concertbooking.domain.seat.Seat;
import com.sion.concertbooking.domain.seat.SeatGrade;
import com.sion.concertbooking.domain.seat.SeatRepository;
import com.sion.concertbooking.infrastructure.repository.ReservationJpaRepository;
import com.sion.concertbooking.test.TestDataCleaner;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(ReservationServiceConcurrencyTest.class);

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

        // 초기 Reservation 데이터 수가 0이면 데드락이 발생하므로 초기 데이터를 넣어준다. (그런데 왜..??)
        reservationJpaRepository.save(Instancio.of(Reservation.class)
                .set(field(Reservation::getId), null)
                .set(field(Reservation::getConcertScheduleId), concertSchedule.getId() + 1000)
                .create());

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
        List.of(createCommandUser1, createCommandUser2, createCommandUser3).forEach(command -> {
            executorService.submit(() -> {
                try {
                    reservationService.createReservations(command);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    assertThat(e).isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("이미 예약중인 좌석입니다.");
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        });
        latch.await();
        executorService.shutdown();

        // then
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(2);

        List<Reservation> allReservations = reservationJpaRepository.findAll();
        allReservations.removeFirst(); // 데드락 방지를 위한 초기 데이터는 제거한다.

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
