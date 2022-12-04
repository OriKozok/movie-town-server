package com.MovieTown.exceptions;

public class DirectorExistsException extends Exception{

    public DirectorExistsException() {
        super("Director already exists");
    }
}
