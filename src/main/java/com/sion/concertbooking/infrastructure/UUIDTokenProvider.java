package com.sion.concertbooking.infrastructure;

import com.sion.concertbooking.domain.service.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Component
public class UUIDTokenProvider implements TokenProvider {

    private static final String CONCERT_TOKEN_HEADER = "X-Concert-Token";

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
