package com.sion.concertbooking.intefaces.aspect;

import com.sion.concertbooking.domain.waitingtoken.WaitingTokenInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenUtils {

    public static WaitingTokenInfo getTokenInfo() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        return (WaitingTokenInfo) request.getAttribute("tokenInfo");
    }
}
