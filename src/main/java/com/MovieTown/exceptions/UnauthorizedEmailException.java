package com.MovieTown.exceptions;

public class UnauthorizedEmailException extends Exception{

    public UnauthorizedEmailException() {
        super("Email must have @ and .");
    }
}
