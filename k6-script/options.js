export const options = {
    vus: 1000,
    duration: '20s',
    thresholds: {
        http_req_duration: ['p(95)<200', 'p(99)<500'],
        'http_req_duration{name:checkConcertSchedules}': ['p(95)<200', 'p(99)<400'],
        'http_req_duration{name:issueWaitingToken}': ['p(95)<200', 'p(99)<400'],
        'http_req_duration{name:checkQueueInfo}': ['p(95)<200', 'p(99)<400'],
        'http_req_duration{name:reserveSeats}': ['p(95)<200', 'p(99)<400'],
        'http_req_duration{name:chargePoint}': ['p(95)<200', 'p(99)<400'],
        'http_req_duration{name:payment}': ['p(95)<200', 'p(99)<700'],
    },
    noConnectionReuse: true,
}

export const BASE_URL = 'http://localhost:8080';