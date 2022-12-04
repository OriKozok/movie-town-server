package com.MovieTown.exceptions;

public class ScreeningExistsException extends Exception{

    public ScreeningExistsException() {
        super("No such screening exists");
    }
}
