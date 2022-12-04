package com.MovieTown.exceptions;

public class NoSuchUserException extends Exception{

    public NoSuchUserException() {
        super("User does not exists");
    }
}
