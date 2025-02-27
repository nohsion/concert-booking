import http from 'k6/http';
import { checkStatus } from "../utils.js";
import { BASE_URL } from "../options.js";

export function payment() {
    const payload = JSON.stringify({
        concertId: globalThis.concertId,
        reservationIds: globalThis.reservationIds,
    });
    const params = {
        headers: {
            'X-Concert-Token': globalThis.token,
            'Content-Type': 'application/json',
        },
        tags: { name: 'payment' }
    };
    const url = BASE_URL + '/api/v1/payment';
    const response = http.post(url, payload, params);

    checkStatus({
        response: response,
        expectedStatus: 200,
        printOnError: true,
        printOnSuccess: true
    });

}