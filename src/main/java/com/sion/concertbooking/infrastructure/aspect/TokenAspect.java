package com.sion.concertbooking.infrastructure.aspect;

import com.sion.concertbooking.domain.info.WaitingQueueInfo;
import com.sion.concertbooking.domain.service.TokenProvider;
import com.sion.concertbooking.domain.service.WaitingQueueService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;

@Aspect
@Component
public class TokenAspect {

    private final TokenProvider tokenProvider;
    private final WaitingQueueService waitingQueueService;

    public TokenAspect(TokenProvider tokenProvider, WaitingQueueService waitingQueueService) {
        this.tokenProvider = tokenProvider;
        this.waitingQueueService = waitingQueueService;
    }

    @Before("@annotation(TokenRequired)")
    public void validateToken(JoinPoint joinPoint) throws AuthenticationException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String tokenId = tokenProvider.resolveToken(request);
        boolean tokenValid = waitingQueueService.isTokenValid(tokenId, LocalDateTime.now());
        if (!tokenValid) {
            throw new AuthenticationException("Invalid token");
        }
        WaitingQueueInfo waitingQueueInfo = waitingQueueService.getQueueByTokenId(tokenId);
        TokenInfo tokenInfo = new TokenInfo(
                waitingQueueInfo.tokenId(),
                waitingQueueInfo.userId(),
                waitingQueueInfo.concertId(),
                waitingQueueInfo.status(),
                waitingQueueInfo.expiredAt()
        );

        request.setAttribute("tokenInfo", tokenInfo);
    }
}
