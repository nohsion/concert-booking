# 콘서트 예매 성능 테스트 - K6

## 개요

콘서트 예매 대기열 서비스에서 제공하는 API 들의 성능 테스트를 시나리오 기반 K6로 수행한 결과를 정리합니다.

## 시나리오

유저의 행동 기반 시나리오를 통해 인기 많은 콘서트를 기준으로 전체적인 시스템 성능을 확인합니다.

1. 콘서트별 예매 가능한 날짜 조회
2. 콘서트 예매 대기열 토큰 발급
3. 콘서트 예매 대기열 정보 조회
4. 콘서트 좌석 예약
5. 포인트 충전
6. 콘서트 좌석 결제

## k6 테스트

테스트 설정은 다음과 같습니다.

```javascript
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
```

- `vus`: 가상 사용자 수 1000명
- `duration`: 20초 동안 테스트

### 설정 이유

- 인기 있는 콘서트라고 가정하고, 동시에 1000명의 사용자가 접속하는 상황을 가정
- 20초 동안의 테스트를 통해 각 API 의 성능을 확인

## 결과

```
✗ 성공
↳  87% — ✓ 13682 / ✗ 1918
✗ response is not empty
↳  94% — ✓ 14695 / ✗ 905

     checks.............................: 91.35% 29820 out of 32643
     data_received......................: 7.4 MB 148 kB/s
     data_sent..........................: 3.8 MB 76 kB/s
     http_req_blocked...................: avg=4.21ms   min=42µs    med=227µs    max=1.01s    p(90)=872µs    p(95)=23.2ms   p(99)=63.42ms
     http_req_connecting................: avg=4.07ms   min=37µs    med=193µs    max=1s       p(90)=781.6µs  p(95)=23.03ms  p(99)=61.58ms
   ✗ http_req_duration..................: avg=477.35ms min=123µs   med=6.45ms   max=11.46s   p(90)=128.81ms p(95)=4.29s    p(99)=9.71s
       { expected_response:true }.......: avg=679.04ms min=890µs   med=3.26ms   max=11.46s   p(90)=2.48s    p(95)=5.8s     p(99)=10.3s
     ✓ { name:chargePoint }.............: avg=22.51ms  min=2.67ms  med=5.47ms   max=883.03ms p(90)=44.9ms   p(95)=79.32ms  p(99)=129.47ms
     ✗ { name:checkConcertSchedules }...: avg=3.1s     min=123µs   med=602.02ms max=11.46s   p(90)=9.38s    p(95)=10.52s   p(99)=11.23s
     ✓ { name:checkQueueInfo }..........: avg=7.68ms   min=890µs   med=2.18ms   max=750.82ms p(90)=6.81ms   p(95)=11.63ms  p(99)=27.2ms
     ✗ { name:issueWaitingToken }.......: avg=1.82s    min=1.46ms  med=526.15ms max=8.43s    p(90)=5.25s    p(95)=6.24s    p(99)=7.76s
     ✓ { name:payment }.................: avg=14.91ms  min=5.61ms  med=8.96ms   max=210.25ms p(90)=24.33ms  p(95)=51.78ms  p(99)=114.38ms
     ✗ { name:reserveSeats }............: avg=32.98ms  min=7.24ms  med=14.83ms  max=912.07ms p(90)=49.64ms  p(95)=95.36ms  p(99)=875.78ms
     http_req_failed....................: 30.98% 5281 out of 17043
     http_req_receiving.................: avg=130.55µs min=0s      med=62µs     max=17.16ms  p(90)=227µs    p(95)=534µs    p(99)=1.08ms
     http_req_sending...................: avg=96.37µs  min=4µs     med=24µs     max=24.48ms  p(90)=71µs     p(95)=182.79µs p(99)=2.27ms
     http_req_tls_handshaking...........: avg=0s       min=0s      med=0s       max=0s       p(90)=0s       p(95)=0s       p(99)=0s
     http_req_waiting...................: avg=477.12ms min=116µs   med=6.26ms   max=11.46s   p(90)=128.58ms p(95)=4.29s    p(99)=9.71s
     http_reqs..........................: 17043  340.795276/s
     iteration_duration.................: avg=13.76s   min=18.44ms med=219.84ms max=49.58s   p(90)=37.27s   p(95)=41.11s   p(99)=46.76s
     iterations.........................: 1616   32.313863/s
     vus................................: 324    min=324            max=1000
     vus_max............................: 1000   min=1000           max=1000


running (50.0s), 0000/1000 VUs, 1616 complete and 321 interrupted iterations
default ✓ [======================================] 1000 VUs  20s
ERRO[0050] thresholds on metrics 'http_req_duration, http_req_duration{name:checkConcertSchedules}, http_req_duration{name:issueWaitingToken}, http_req_duration{name:reserveSeats}' have been crossed
```

### 결과 분석

- 1000명의 가상 사용자가 20초 동안 테스트를 진행한 결과

#### 1. 콘서트별 예매 가능 날짜 조회 (checkConcertSchedules) ❌

- 평균 응답 시간: 3.1s
- P(95): 10.52s
- P(99): 11.23s
- 성능이슈 해결 방안: 로컬 캐시
  - 현재, concert_id에 대해 인덱스가 적용되었는데도 조회 속도가 느리다.
  - `CREATE INDEX idx_concert_id ON concert_schedule (concert_id);`
  - 따라서, 로컬 캐시를 적용하여 조회 속도를 향상 시킬 수 있을 것이다.
  - 또한, 콘서트별 스케쥴은 변경이 매우 적기 때문에, 캐시를 통한 성능 개선이 크게 기대된다.

#### 2. 콘서트 예매 대기열 토큰 발급 (issueWaitingToken) ❌

- 평균 응답 시간: 1.82s
- P(95): 6.24s
- P(99): 7.76s
- 성능이슈 해결 방안: redis 클러스터
  - 대기열 토큰 발급 로직에는 redis만을 적용하고 있다.
  - 토큰 발급 및 저장 -> 대기열 저장 -> (부가로직) 대기 콘서트 저장
  - 하나의 API에서 3번의 redis 호출이 발생하고 있다.
  - 다수의 사용자가 동시에 API를 호출하면, 단일 서버로 구성된 redis에서 싱글 스레드로 동작하므로 병목 현상이 발생할 수 있다.
  - 따라서, redis 클러스터를 적용하여 병목 현상을 해결할 수 있을 것이다.

#### 3. 콘서트 예매 대기열 정보 조회 (checkQueueInfo) ✅

- 평균 응답 시간: 7.68ms
- P(95): 11.63ms
- P(99): 27.2ms

#### 4. 콘서트 좌석 예약 (reserveSeats) ⚠️

- 평균 응답 시간: 32.98ms
- P(95): 95.36ms
- P(99): 875.78ms
- 성능이슈 해결 방안: 유지
  - 콘서트 좌석예약은 예매 페이지에 접속한 사용자만 호출 가능한 API이다.
  - 따라서, 토큰 발급보다는 안정적인 트래픽이 예상된다.
  - 대부분의 유저는 빠른 응답을 보이나, 1% 정도의 유저만(P(99)) 느린 응답을 보이고 있다.
  - 일단 유지하고, 사용자가 증가하면서 성능 이슈가 발생할 경우 lock 성능을 고려해야 한다.
  - 또한, 동시에 예약 가능한 최대 좌석 수를 제한하는 방법도 고려할 수 있다.

#### 5. 포인트 충전 (chargePoint) ✅

- 평균 응답 시간: 22.51ms
- P(95): 79.32ms
- P(99): 129.47ms

#### 6. 결제 (payment) ✅

- 평균 응답 시간: 14.91ms
- P(95): 51.78ms
- P(99): 114.38ms


## 결론

- 콘서트 예매 시스템의 성능 테스트를 통해 예상치 못했던 성능 이슈를 발견할 수 있었다.
- 특히, 콘서트별 예매 날짜 조회는 DB 인덱스를 적용했기 때문에 괜찮은 성능을 기대했으나 매우 느린 응답 시간을 보였다.
- 이를 통해 DB 인덱스와 캐시를 같이 사용하여 최적화하는 방안을 찾을 수 있었다.
- 또한, 대기열 토큰 발급 API에서의 redis 병목 현상을 해결하기 위해 redis 클러스터를 적용하는 방안을 고려할 수 있었다.
- 시나리오 기반의 성능 테스트를 통해 전체적인 시스템 성능을 확인할 수 있었다.
- 다행히 다소 느릴 거라 예상했던 예약, 포인트 충전, 결제 API는 매우 빠른 응답을 보여 성능 이슈가 없음을 확인할 수 있었다.


## References

- https://grafana.com/docs/k6/latest/using-k6/k6-options/how-to/
- https://github.com/grafana/k6-example-woocommerce