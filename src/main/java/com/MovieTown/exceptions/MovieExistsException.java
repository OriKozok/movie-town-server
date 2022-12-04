package com.MovieTown.exceptions;

public class MovieExistsException extends Exception{

    public MovieExistsException() {
        super("Movie already exists");
    }
}
