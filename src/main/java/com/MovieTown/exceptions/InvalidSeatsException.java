package com.MovieTown.exceptions;

public class InvalidSeatsException extends Exception{

    public InvalidSeatsException() {
        super("Seats must be in the same row and have executive columns");
    }
}
