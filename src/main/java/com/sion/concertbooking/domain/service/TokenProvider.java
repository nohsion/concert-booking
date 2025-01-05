package com.sion.concertbooking.domain.service;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenProvider {

    String generateToken();
    String resolveToken(HttpServletRequest request);
}
