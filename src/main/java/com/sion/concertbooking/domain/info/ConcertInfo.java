package com.sion.concertbooking.domain.info;

import com.sion.concertbooking.domain.entity.Concert;

public record ConcertInfo(
        long concertId,
        long theaterId,
        String concertTitle
) {

    public static ConcertInfo fromEntity(Concert concert) {
        return new ConcertInfo(
                concert.getId(),
                concert.getTheatreId(),
                concert.getTitle()
        );
    }
}
