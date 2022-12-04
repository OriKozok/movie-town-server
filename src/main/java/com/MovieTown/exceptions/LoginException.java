package com.MovieTown.exceptions;

public class LoginException extends Exception{

    public LoginException() {
        super("Can't log in. Check the email or the password");
    }
}
