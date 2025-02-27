import { check } from 'k6';
import http from 'k6/http';
import { randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.1.0/index.js';
import {BASE_URL} from "../options.js";

export function checkConcertSchedules() {
    const concertId = globalThis.concertId;

    const response = http.get(BASE_URL + '/api/v1/concert/schedules?concertId=' + concertId,
        { tags: { name: 'checkConcertSchedules' } });

    check(response,
        {
            '성공': (r) => r.status === 200 ||
                (r.status === 400 && r.json().message === '이미 예약중인 좌석입니다.'),
            'response is not empty': (r) => r.body.length > 0,
        }
    )

    const result = response.json()
    if (result.length === 0) {
        throw new Error('No concert schedules found');
    }
    const randomSchedule = result[randomIntBetween(0, result.length - 1)];
    globalThis.concertScheduleId = randomSchedule.concertScheduleId;
}