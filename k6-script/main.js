import {sleep} from 'k6';
import {SharedArray} from 'k6/data';
import {randomIntBetween} from 'https://jslib.k6.io/k6-utils/1.1.0/index.js';

import {options} from "./options.js";
import {checkConcertSchedules} from './booking/1_check-concert-schedules.js';
import {issueWaitingToken} from "./booking/2_issue-waiting-token.js";
import {checkQueueInfo} from "./booking/3_check-queue-info.js";
import {reserveSeats} from "./booking/4_reserve-seats.js";
import {chargePoint} from "./booking/5_charge-point.js";
import {payment} from "./booking/6_payment.js";

export {options};

const PAUSE_MIN_SEC = 1;
const PAUSE_MAX_SEC = 5;

globalThis = {concertId: 2}; // 전역 객체 (concertId, userId, token)

const userIds = new SharedArray('userIds', function () {
    return Array.from({length: options.vus + 10}, (_, i) => i + 1);
});

export default function () {
    globalThis.userId = userIds[__VU % userIds.length];

    checkConcertSchedules();
    sleep(randomIntBetween(PAUSE_MIN_SEC, PAUSE_MAX_SEC));
    issueWaitingToken();
    sleep(2);
    checkQueueInfo();
    sleep(randomIntBetween(PAUSE_MIN_SEC, PAUSE_MAX_SEC));
    reserveSeats();
    sleep(randomIntBetween(PAUSE_MIN_SEC, PAUSE_MAX_SEC));
    chargePoint();
    sleep(randomIntBetween(PAUSE_MIN_SEC, PAUSE_MAX_SEC));
    payment();

};