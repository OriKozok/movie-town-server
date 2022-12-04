package com.MovieTown.exceptions;

public class NoSuchMovieException extends Exception{

    public NoSuchMovieException() {
        super("Error! No such movie!");
    }
}
