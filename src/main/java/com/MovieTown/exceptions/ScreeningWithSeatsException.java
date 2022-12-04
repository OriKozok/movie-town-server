package com.MovieTown.exceptions;

public class ScreeningWithSeatsException extends Exception{

    public ScreeningWithSeatsException() {
        super("A new screening does not have seats");
    }
}
