CREATE TABLE `waiting_queue`
(
    `id`         int PRIMARY KEY AUTO_INCREMENT,
    `token_id`   varchar(36) NOT NULL COMMENT '서비스 토큰 UUID 128비트',
    `user_id`    int         NOT NULL COMMENT '유저 ID',
    `concert_id` int         NOT NULL COMMENT '공연 ID',
    `status`     varchar(10) NOT NULL COMMENT 'WAITING: 대기중, ENTERED: 입장완료',
    `created_at` datetime    NOT NULL DEFAULT (CURRENT_TIMESTAMP()),
    `updated_at` datetime    NOT NULL DEFAULT (CURRENT_TIMESTAMP()),
    `expired_at` datetime    NOT NULL DEFAULT (CURRENT_TIMESTAMP()) COMMENT '만료 시각'
);

CREATE TABLE `reservation`
(
    `id`                   int PRIMARY KEY AUTO_INCREMENT,
    `user_id`              int      NOT NULL COMMENT '특강 신청한 유저 ID',
    `concert_id`           int      NOT NULL COMMENT '콘서트 ID',
    `concert_title`        varchar(100) COMMENT '콘서트명',
    `play_date`            datetime COMMENT '공연 날짜, e.g. 2024-12-25 12:00:00',
    `cancel_deadline_date` datetime NOT NULL COMMENT '취소 가능 날짜, e.g. 2025-01-03 10:00:00',
    `seat_id`              int      NOT NULL COMMENT '좌석 ID',
    `seat_num`             int COMMENT '좌석 번호',
    `seat_grade`           varchar(10) COMMENT '좌석 등급',
    `seat_price`           int COMMENT '좌석 가격',
    `status`               varchar(10) COMMENT '예약 상태. SUSPEND: 결제 대기, SUCCESS: 완료, CANCEL: 예약 취소',
    `created_at`           datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP()),
    `updated_at`           datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP())
);

CREATE TABLE `user_point`
(
    `id`         int PRIMARY KEY AUTO_INCREMENT,
    `user_id`    int      NOT NULL COMMENT '유저 ID',
    `amount`     int      NOT NULL DEFAULT 0 COMMENT '잔여 포인트',
    `created_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP()),
    `updated_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP())
);

CREATE TABLE `user_point_history`
(
    `id`            int PRIMARY KEY AUTO_INCREMENT,
    `user_point_id` int,
    `amount`        int         NOT NULL DEFAULT 0 COMMENT '충전/사용 포인트',
    `type`          varchar(10) NOT NULL COMMENT '트랜잭션 종류. CHARGE: 충전, USE: 사용',
    `created_at`    datetime    NOT NULL DEFAULT (CURRENT_TIMESTAMP()),
    `updated_at`    datetime    NOT NULL DEFAULT (CURRENT_TIMESTAMP())
);

CREATE TABLE `concert`
(
    `id`         int PRIMARY KEY AUTO_INCREMENT,
    `title`      varchar(100) COMMENT '콘서트명',
    `theatre_id` int      NOT NULL COMMENT '극장 ID',
    `created_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP()),
    `updated_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP())
);

CREATE TABLE `concert_schedule`
(
    `id`         int PRIMARY KEY AUTO_INCREMENT,
    `concert_id` int      NOT NULL COMMENT '콘서트명',
    `play_date`  datetime COMMENT '공연 날짜, e.g. 2024-12-25 12:00:00',
    `created_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP()),
    `updated_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP())
);

CREATE TABLE `seat`
(
    `id`         int PRIMARY KEY AUTO_INCREMENT,
    `theatre_id` int      NOT NULL COMMENT '극장 ID',
    `seat_num`   int COMMENT '좌석 번호',
    `seat_grade` varchar(10) COMMENT '좌석 등급',
    `seat_price` int COMMENT '좌석 가격',
    `created_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP()),
    `updated_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP())
);