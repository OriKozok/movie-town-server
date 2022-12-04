package com.MovieTown.exceptions;

public class NoSuchCinemaException extends Exception{

    public NoSuchCinemaException() {
        super("No such cinema");
    }
}
