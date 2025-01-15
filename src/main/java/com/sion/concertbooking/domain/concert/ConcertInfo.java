package com.sion.concertbooking.domain.concert;

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
