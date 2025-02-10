package com.sion.concertbooking.domain.waitingtoken;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenProvider {

    String CONCERT_TOKEN_HEADER = "X-Concert-Token";

    String generateToken();
    String resolveToken(HttpServletRequest request);
}
