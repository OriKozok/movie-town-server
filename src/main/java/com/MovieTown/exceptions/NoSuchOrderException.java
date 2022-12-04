package com.MovieTown.exceptions;

public class NoSuchOrderException extends Exception{
    public NoSuchOrderException() {
        super("No such order exists");
    }
}
