package com.MovieTown.exceptions;

public class InvalidCinemaSizeException extends Exception{

    public InvalidCinemaSizeException() {
        super("Rows must be between 5 and 15. Columns must be between 10 and 30");
    }
}
