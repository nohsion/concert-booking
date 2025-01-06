package com.sion.concertbooking.domain.dto;

import com.sion.concertbooking.domain.entity.Concert;

public record ConcertDto(
        long concertId,
        long theaterId,
        String concertTitle
) {

    public static ConcertDto fromEntity(Concert concert) {
        return new ConcertDto(
                concert.getId(),
                concert.getTheatreId(),
                concert.getTitle()
        );
    }
}
