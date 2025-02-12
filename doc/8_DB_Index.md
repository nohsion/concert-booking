# DB 인덱스 추가 및 성능개선

# 1. 쿼리 분석 및 인덱스 필요성

## 1.1. 유저 포인트 조회

```java
public interface PointJpaRepository extends JpaRepository<Point, Long> {
    Point findByUserId(long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Point p WHERE p.userId = :userId")
    Point findByUserIdWithLock(@Param("userId") long userId);
}
```

```sql
SELECT * FROM point WHERE user_id = 100; -- 포인트 조회

SELECT * FROM point WHERE user_id = 100 FOR UPDATE; -- 포인트 적립, 사용 전에 비관적 락
```

- 기능: 사용자 ID로 포인트 조회
- 인덱스 필요한 이유
  - 사용자별로 포인트 잔액 조회가 자주 발생할 것으로 예상한다. (포인트 적립, 사용, 조회 등)
  - 사용자가 많아질 수록 조회 속도가 느려질 수 있다.
- 단일 인덱스 적용: `user_id`
  - `CREATE INDEX idx_user_id ON point (user_id);`
- 기대 효과
  - 잔액 조회 성능이 향상될 것으로 기대한다.
- 인덱스 적용 전과 후의 성능 비교 (Execution Time 기준)
  - 더미데이터 개수: 200만건 (사용자 200만명으로 가정)
  - 인덱스 적용 전: `256 ms`
    - ```text
      +--+-----------+-----+----+-------------+----+-------+----+-------+-----------+
      |id|select_type|table|type|possible_keys|key |key_len|ref |rows   |Extra      |
      +--+-----------+-----+----+-------------+----+-------+----+-------+-----------+
      |1 |SIMPLE     |point|ALL |null         |null|null   |null|1995000|Using where|
      +--+-----------+-----+----+-------------+----+-------+----+-------+-----------+
      ``` 
  - 인덱스 적용 후: `3 ms`
    - ```text
      +--+-----------+-----+----+-------------+-----------+-------+-----+----+-----+
      |id|select_type|table|type|possible_keys|key        |key_len|ref  |rows|Extra|
      +--+-----------+-----+----+-------------+-----------+-------+-----+----+-----+
      |1 |SIMPLE     |point|ref |idx_user_id  |idx_user_id|4      |const|1   |     |
      +--+-----------+-----+----+-------------+-----------+-------+-----+----+-----+
      ```
  - 결과: 인덱스 적용으로 `256ms -> 3ms` 조회 성능이 약 85배 향상되었다.

## 1.2. 포인트 내역 조회

```java
public interface PointHistoryJpaRepository extends JpaRepository<PointHistory, Long> {
    Page<PointHistory> findByPointIdAndType(long pointId, TransactionType type, Pageable pageable);
    Page<PointHistory> findByPointId(long pointId, Pageable pageable);
}
```

```sql
SELECT * 
FROM point_history
WHERE point_id = 100 AND type = 'CHARGE'
ORDER BY id DESC LIMIT 10;

SELECT *
FROM point_history
WHERE point_id = 100
ORDER BY id DESC LIMIT 10;
```

- 기능: 포인트 ID 및 타입(`CHARGE, USE`)으로 포인트 내역 조회
- 인덱스 필요한 이유
  - 모든 사용자의 포인트 충전과 사용 기록이 저장되므로, 조회 성능이 저하될 수 있다. 
  - 사용자별로 포인트 내역 조회가 자주 발생할 것으로 예상한다. (전체, 충전, 사용)
- 복합 인덱스 적용: `point_id`, `type`
  - `CREATE INDEX idx_point_id_type ON point_history (point_id, type);`
- 기대 효과
  - 특정 사용자의 포인트 내역 조회 성능이 향상될 것으로 기대한다.
  - 특정 사용자의 충전, 사용이 많아져도 조회 성능이 유지될 것으로 기대한다. 
- 인덱스 적용 전과 후의 성능 비교 (Execution Time 기준)
  - 더미데이터 개수: 500만건 (사용자 10만명당 50건의 내역 생성. 각 'CHARGE': 25건, 'USE': 25건)
    1) 사용자의 전체 내역 조회: `SELECT * FROM point_history WHERE point_id = 100 ORDER BY id DESC LIMIT 10;`
      - 인덱스 적용 전: `1 s 133 ms` 
        - ```text
          +--+-----------+-------------+-----+-------------+-------+-------+----+----+-----------+
          |id|select_type|table        |type |possible_keys|key    |key_len|ref |rows|Extra      |
          +--+-----------+-------------+-----+-------------+-------+-------+----+----+-----------+
          |1 |SIMPLE     |point_history|index|null         |PRIMARY|4      |null|10  |Using where|
          +--+-----------+-------------+-----+-------------+-------+-------+----+----+-----------+
          ```
      - 인덱스 적용 후: `5 ms`
        - ```text
          +--+-----------+-------------+----+-----------------+-----------------+-------+-----+----+---------------------------+
          |id|select_type|table        |type|possible_keys    |key              |key_len|ref  |rows|Extra                      |
          +--+-----------+-------------+----+-----------------+-----------------+-------+-----+----+---------------------------+
          |1 |SIMPLE     |point_history|ref |idx_point_id_type|idx_point_id_type|5      |const|50  |Using where; Using filesort|
          +--+-----------+-------------+----+-----------------+-----------------+-------+-----+----+---------------------------+
          ```
      - 결과: 인덱스 적용으로 `1s 133ms -> 5ms` 조회 성능이 약 220배 향상되었다. (기대했던 사용자별 내역 50 rows 개수와 일치한다.)
    2) 사용자의 충전 내역 조회: `SELECT * FROM point_history WHERE point_id = 100 AND type = 'CHARGE' ORDER BY id DESC LIMIT 10;`
      - 인덱스 적용 전: `1 s 83 ms`
        - ```text
          위와 동일
          ```
      - 인덱스 적용 후: `4 ms`
        - ```text
          +--+-----------+-------------+----+-----------------+-----------------+-------+-----------+----+-----------+
          |id|select_type|table        |type|possible_keys    |key              |key_len|ref        |rows|Extra      |
          +--+-----------+-------------+----+-----------------+-----------------+-------+-----------+----+-----------+
          |1 |SIMPLE     |point_history|ref |idx_point_id_type|idx_point_id_type|47     |const,const|25  |Using where|
          +--+-----------+-------------+----+-----------------+-----------------+-------+-----------+----+-----------+
          ```
      - 결과: 인덱스 적용으로 `1s 83ms -> 4ms` 조회 성능이 약 270배 향상되었다. (기대했던 사용자별 충전 내역 25 rows 개수와 일치한다.)

## 1.3. 콘서트 스케쥴 조회

```java
public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertSchedule, Long> {
    ConcertSchedule findByConcertId(long concertId);
}
```

```sql
SELECT * FROM concert_schedule WHERE concert_id = 100;
```

- 기능: 콘서트의 공연 일정 조회
- 인덱스 필요한 이유
  - 모든 콘서트의 공연 일정이 저장되므로, 조회 성능이 저하될 수 있다.
  - 콘서트별로 공연 일정 조회가 자주 발생할 것으로 예상한다. 특히, 예매 기간동안 많은 조회가 발생할 수 있다.
- 단일 인덱스 적용: `concert_id`
  - `CREATE INDEX idx_concert_id ON concert_schedule (concert_id);`
- 기대 효과
  - 특정 콘서트의 공연 일정 조회 성능이 향상될 것으로 기대한다.
  - 인기 있는 공연 예매기간에도 조회 성능이 유지될 것으로 기대한다.
- 인덱스 적용 전과 후의 성능 비교 (Execution Time 기준)
  - 더미데이터 개수: 900만건 (콘서트 30만개당 30건의 일정 생성. 한달동안 1일 1회 공연이라고 가정)
    - 인덱스 적용 전: `1 s 240 ms`
      - ```text
        +--+-----------+----------------+----+-------------+----+-------+----+-------+-----------+
        |id|select_type|table           |type|possible_keys|key |key_len|ref |rows   |Extra      |
        +--+-----------+----------------+----+-------------+----+-------+----+-------+-----------+
        |1 |SIMPLE     |concert_schedule|ALL |null         |null|null   |null|8750325|Using where|
        +--+-----------+----------------+----+-------------+----+-------+----+-------+-----------+
        ```
    - 인덱스 적용 후: `4 ms`
      - ```text
        +--+-----------+----------------+----+--------------+--------------+-------+-----+----+-----+
        |id|select_type|table           |type|possible_keys |key           |key_len|ref  |rows|Extra|
        +--+-----------+----------------+----+--------------+--------------+-------+-----+----+-----+
        |1 |SIMPLE     |concert_schedule|ref |idx_concert_id|idx_concert_id|4      |const|30  |     |
        +--+-----------+----------------+----+--------------+--------------+-------+-----+----+-----+
        ```
    - 결과: 인덱스 적용으로 `1s 240ms -> 4ms` 조회 성능이 약 300배 향상되었다. (기대했던 콘서트별 일정 30 rows 개수와 일치한다.)

## 1.4. 콘서트 예매 조회

```java
public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByConcertScheduleIdAndSeatIdIn(long concertScheduleId, List<Long> seatIds);
    List<Reservation> findByUserId(long userId);
}
```

```sql
SELECT * FROM reservation WHERE concert_schedule_id = 35 AND seat_id IN (1, 2);

SELECT * FROM reservation WHERE user_id = 100;
```

- 기능: 콘서트의 특정 일정과 좌석에 대한 예약 내역 조회
- 인덱스 필요한 이유
  - 모든 콘서트의 예약 내역이 저장되므로, 조회 성능이 크게 저하될 수 있다.
  - 또한, 기존 좌석 50개보다 더 많은 좌석이 추가될 수 있으므로, 조회 성능이 더욱 저하될 수 있다. 
  - 콘서트 서비스에서 가장 데이터가 많이 쌓이는 테이블이고, 예매시 동시 사용자가 많아질 수 있어 조회 성능이 매우 중요하다. 
  - 시나리오 
    1. 콘서트 좌석 예매: 특정 일정과 좌석에 대해 예약을 요청할 때
    2. 사용자의 예약 내역 조회: 특정 사용자의 예약 내역을 조회할 때
- 시나리오에 따른 인덱스 적용
  1. 콘서트 좌석 예매: 복합인덱스 적용 `concert_schedule_id`, `seat_id`
     - `CREATE INDEX idx_concert_schedule_id_seat_id ON reservation (concert_schedule_id, seat_id);`
  2. 사용자의 예약 내역 조회: 단일인덱스 적용 `user_id`
     - `CREATE INDEX idx_user_id ON reservation (user_id);`
- 기대 효과
  - 콘서트 좌석 예매시, 특정 일정과 좌석에 대한 예약 내역 조회 성능이 향상될 것으로 기대한다.
  - 사용자의 예약 내역 조회 성능이 향상될 것으로 기대한다.
- 인덱스 적용 전과 후의 성능 비교 (Execution Time 기준)
  - 더미데이터 개수: 6천만건 (콘서트 2만개 * 스케쥴 30개 * 좌석 50개 * 예약상태 2개)
  1) 콘서트 좌석 예매: `SELECT * FROM reservation WHERE concert_schedule_id = 35 AND seat_id IN (1, 2);`
    - 인덱스 적용 전: `22 s 448 ms`
      - ```text
        +--+-----------+-----------+----+-------------+----+-------+----+--------+-----------+
        |id|select_type|table      |type|possible_keys|key |key_len|ref |rows    |Extra      |
        +--+-----------+-----------+----+-------------+----+-------+----+--------+-----------+
        |1 |SIMPLE     |reservation|ALL |null         |null|null   |null|59492302|Using where|
        +--+-----------+-----------+----+-------------+----+-------+----+--------+-----------+
        ```
    - 인덱스 적용 후: `5 ms`
      - ```text
        +--+-----------+-----------+-----+-------------------------------+-------------------------------+-------+----+----+---------------------+
        |id|select_type|table      |type |possible_keys                  |key                            |key_len|ref |rows|Extra                |
        +--+-----------+-----------+-----+-------------------------------+-------------------------------+-------+----+----+---------------------+
        |1 |SIMPLE     |reservation|range|idx_concert_schedule_id_seat_id|idx_concert_schedule_id_seat_id|8      |null|100 |Using index condition|
        +--+-----------+-----------+-----+-------------------------------+-------------------------------+-------+----+----+---------------------+
        ```
    - 결과: 인덱스 적용으로 `22s 448ms -> 5ms` 조회 성능이 약 4500배 향상되었다.
  2) 사용자의 예약 내역 조회: `SELECT * FROM reservation WHERE user_id = 100;`
    - 인덱스 적용 전: `23 s 603 ms`
      - ```text
        +--+-----------+-----------+----+-------------+----+-------+----+--------+-----------+
        |id|select_type|table      |type|possible_keys|key |key_len|ref |rows    |Extra      |
        +--+-----------+-----------+----+-------------+----+-------+----+--------+-----------+
        |1 |SIMPLE     |reservation|ALL |null         |null|null   |null|59492302|Using where|
        +--+-----------+-----------+----+-------------+----+-------+----+--------+-----------+
        ```
    - 인덱스 적용 후: `4 ms`
      - ```text
        +--+-----------+-----------+----+-------------+-----------+-------+-----+----+-----+
        |id|select_type|table      |type|possible_keys|key        |key_len|ref  |rows|Extra|
        +--+-----------+-----------+----+-------------+-----------+-------+-----+----+-----+
        |1 |SIMPLE     |reservation|ref |idx_user_id  |idx_user_id|4      |const|3000|     |
        +--+-----------+-----------+----+-------------+-----------+-------+-----+----+-----+
        ```
    - 결과: 인덱스 적용으로 `23s 603ms -> 4ms` 조회 성능이 약 6000배 향상되었다.

# 2. 결론

자주 사용되거나 조회가 오래 걸리는 콘서트 대기열 시스템에 대한 DB 인덱스 적용 방안을 검토하고 적용했다.

### 2.1. 조회 성능 개선

- 주요 쿼리 4군데에 대해 적절한 인덱스를 적용하여, 조회 성능을 약 85배 ~ 6000배 향상시킬 수 있었다.
- 특히, 콘서트 일정 조회와 예약 내역 조회의 성능은 초 단위에서 밀리초 단위로 개선되어, 사용자 경험을 크게 향상시킬 수 있었다.

### 2.2. 인덱스 적용 전략

- 쿼리의 기능과 사용 빈도에 따라 단일 인덱스와 복합 인덱스를 적절히 적용하여 성능을 개선할 수 있었다.
- 특히, 복합 인덱스를 통해 쿼리 조건에 따른 조회 성능을 크게 향상시킬 수 있었다.

### 2.3. 더미 데이터

- 더미데이터를 통해 인덱스 적용 전과 후의 성능을 비교하고, 성능 개선 효과를 확인할 수 있었다.
- 사용자 수: 200만명, 콘서트 수: 30만개, 콘서트 일정 수: 900만건, 예약 내역 수: 6천만건
- 예약 내역 수는 예매 시스템에 따라 적절히 산정하였다. (핵심 테이블이라 생각한다.)
- 한계점
  - 사용자, 콘서트, 일정 수는 인덱스 적용 후의 성능 개선 효과를 확인하기 위해 과도하게 잡은 경향이 있다.
- 추후 개선 방향
  - 예약 내역은 프로덕션 환경에서는 더 많은 데이터가 쌓일 수 있으므로, 주기적인 Slow Query Log 분석 및 인덱스 최적화가 필요하다.
