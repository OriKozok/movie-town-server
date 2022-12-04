package com.MovieTown.exceptions;

public class UserExistsException extends Exception{

    public UserExistsException() {
        super("User already exists with this email");
    }
}
