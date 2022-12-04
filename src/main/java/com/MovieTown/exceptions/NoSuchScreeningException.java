package com.MovieTown.exceptions;

public class NoSuchScreeningException extends Exception{

    public NoSuchScreeningException() {
        super("No such screening");
    }
}
