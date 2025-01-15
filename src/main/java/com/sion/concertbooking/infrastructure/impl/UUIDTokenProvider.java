package com.sion.concertbooking.infrastructure.impl;

import com.sion.concertbooking.domain.token.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Component
public class UUIDTokenProvider implements TokenProvider {

    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        String concertTokenHeader = request.getHeader(CONCERT_TOKEN_HEADER);
        if (!StringUtils.hasText(concertTokenHeader)) {
            return "";
        }
        return concertTokenHeader;
    }

}
