package com.MovieTown.exceptions;

public class SeatIsReservedException extends Exception{

    public SeatIsReservedException() {
        super("Seat is reserved");
    }
}
