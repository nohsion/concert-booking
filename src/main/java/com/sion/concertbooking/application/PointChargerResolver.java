package com.sion.concertbooking.application;

import com.sion.concertbooking.domain.service.PaymentCharger;
import com.sion.concertbooking.infrastructure.payment.FreePaymentCharger;
import com.sion.concertbooking.infrastructure.payment.KakaoPaymentCharger;
import com.sion.concertbooking.infrastructure.payment.NaverPaymentCharger;
import org.springframework.stereotype.Component;

@Component
public class PointChargerResolver {

    private final FreePaymentCharger freePayCharger;
    private final NaverPaymentCharger naverPayCharger;
    private final KakaoPaymentCharger kakaoPayCharger;

    public PointChargerResolver(
            FreePaymentCharger freePayCharger,
            NaverPaymentCharger naverPayCharger,
            KakaoPaymentCharger kakaoPayCharger
    ) {
        this.freePayCharger = freePayCharger;
        this.naverPayCharger = naverPayCharger;
        this.kakaoPayCharger = kakaoPayCharger;
    }

    public PaymentCharger resolve(PaymentType paymentType) {
        return switch (paymentType) {
            case KAKAO_PAY -> kakaoPayCharger;
            case NAVER_PAY -> naverPayCharger;
            default -> freePayCharger;
        };
    }
}
