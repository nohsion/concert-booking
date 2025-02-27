import { check, sleep } from 'k6';
import http from 'k6/http';
import { BASE_URL } from "../options.js";

export function checkQueueInfo() {
    const params = {
        headers: {
            'X-Concert-Token': globalThis.token,
            'Content-Type': 'application/json',
        },
        tags: { name: 'checkQueueInfo' }
    };
    const url = `${BASE_URL}/api/v1/waiting?concertId=${globalThis.concertId}`;
    const response = http.get(url, params);

    // 만약 대기중이다가 예매 페이지로 이동했는데도 대기열 정보 요청을 보내면 400 에러가 발생할 수 있어서
    check(response,
        {
            '성공': (r) => r.status === 200 ||
                (r.status === 400 && r.json().message.includes("해당하는 토큰이 없습니다")),
            'response is not empty': (r) => r.body.length > 0,
        }
    )

    if (response.status === 200 && response.json().remainingWaitingOrder > 0) {
        sleep(1);
        console.log(response.json().remainingWaitingOrder, '명이 예매 페이지 대기 중입니다.');
        checkQueueInfo();
    }

}