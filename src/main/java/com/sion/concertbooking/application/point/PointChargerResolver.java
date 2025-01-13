package com.sion.concertbooking.application.point;

import com.sion.concertbooking.application.payment.PaymentType;
import com.sion.concertbooking.domain.payment.PaymentCharger;
import com.sion.concertbooking.infrastructure.impl.FreePaymentCharger;
import com.sion.concertbooking.infrastructure.impl.KakaoPaymentCharger;
import com.sion.concertbooking.infrastructure.impl.NaverPaymentCharger;
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
