import { check } from 'k6';
import http from 'k6/http';
import { BASE_URL } from "../options.js";

export function issueWaitingToken() {
    const payload = JSON.stringify({
        concertId: globalThis.concertId,
        userId: globalThis.userId,
    });
    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
        tags: { name: 'issueWaitingToken' }
    };
    const url = BASE_URL + '/api/v1/waiting';
    const response = http.post(url, payload, params);

    check(response,
        {
            '성공': (r) => r.status === 200,
            'response is not empty': (r) => r.body.length > 0,
        }
    )

    globalThis.token = response.json().tokenId;
}