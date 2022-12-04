package com.MovieTown.exceptions;

public class UnavailableTimeException extends Exception{

    public UnavailableTimeException() {
        super("The screening time is not available");
    }
}
