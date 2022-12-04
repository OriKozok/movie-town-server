package com.MovieTown.exceptions;

public class InvalidMovieUpdateException extends Exception{

    public InvalidMovieUpdateException() {
        super("Error! You can't change the movie's name");
    }
}
