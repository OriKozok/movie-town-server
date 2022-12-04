package com.MovieTown.exceptions;

public class NoSuchSeatException extends Exception{
    public NoSuchSeatException() {
        super("No such seat");
    }
}
