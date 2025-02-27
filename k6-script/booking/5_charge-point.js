import http from 'k6/http';
import { checkStatus } from "../utils.js";
import { BASE_URL } from "../options.js";

export function chargePoint() {
    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
        tags: { name: 'chargePoint' }
    };
    const url = `${BASE_URL}/api/v1/point/charge?userId=${globalThis.userId}&amount=400000`;
    const response = http.post(url, params, { tags: { name: 'chargePoint' } });

    checkStatus({
        response: response,
        expectedStatus: 200,
        printOnError: true,
        printOnSuccess: true
    });

}