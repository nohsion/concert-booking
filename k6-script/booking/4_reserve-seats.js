import { check, sleep } from 'k6';
import http from 'k6/http';
import { randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.1.0/index.js';
import { BASE_URL } from "../options.js";

export function reserveSeats() {
    const payload = JSON.stringify({
        concertId: globalThis.concertId,
        concertScheduleId: globalThis.concertScheduleId,
        seatIds: createRandomSeatIds(),
    })
    const params = {
        headers: {
            'X-Concert-Token': globalThis.token,
            'Content-Type': 'application/json',
        },
        tags: { name: 'reserveSeats' }
    };
    const url = `${BASE_URL}/api/v1/reservation`;
    const response = http.post(url, payload, params);

    check(response,
        {
            '성공': (r) => r.status === 200 ||
                (r.status === 400 && r.json().message === '이미 예약중인 좌석입니다.'),
            'response is not empty': (r) => r.body.length > 0,
        }
    )

    if (response.status !== 200) {
        sleep(randomIntBetween(1, 5));
        console.log('예약 실패로 인해 다른 좌석 선점을 시도합니다.');
        reserveSeats();
    } else {
        const result = response.json()
        globalThis.reservationIds = result.map(reservation => reservation.reservationId);
        console.log('예약 성공. ReservationIds:', globalThis.reservationIds);
    }

}

function createRandomSeatIds() {
    const size = Math.random() < 0.5 ? 1 : 2;
    const result = [];

    for (let i = 0; i < size; i++) {
        let num;
        do {
            num = randomIntBetween(1, 50);
        } while (result.includes(num));
        result.push(num);
    }

    return result;
}