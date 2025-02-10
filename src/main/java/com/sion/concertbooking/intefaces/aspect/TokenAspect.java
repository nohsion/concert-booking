package com.sion.concertbooking.intefaces.aspect;

import com.sion.concertbooking.domain.waitingtoken.TokenProvider;
import com.sion.concertbooking.domain.waitingtoken.WaitingTokenInfo;
import com.sion.concertbooking.domain.waitingtoken.WaitingTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class TokenAspect {

    private final TokenProvider tokenProvider;
    private final WaitingTokenService waitingTokenService;

    public TokenAspect(
            TokenProvider tokenProvider,
            WaitingTokenService waitingTokenService
    ) {
        this.tokenProvider = tokenProvider;
        this.waitingTokenService = waitingTokenService;
    }

    @Before("@annotation(com.sion.concertbooking.intefaces.aspect.TokenRequired)")
    public void validateToken(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String tokenId = tokenProvider.resolveToken(request);
        WaitingTokenInfo waitingToken = waitingTokenService.getToken(tokenId);

        request.setAttribute("tokenInfo", waitingToken);
    }
}
