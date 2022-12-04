package com.MovieTown.exceptions;

public class CinemaExistsException extends Exception{

    public CinemaExistsException() {
        super("Cinema already exists");
    }
}
