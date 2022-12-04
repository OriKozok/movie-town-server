package com.MovieTown.exceptions;

public class UnauthorizedException extends Exception{
    public UnauthorizedException() {
        super("You are unathorized for this action");
    }
}
