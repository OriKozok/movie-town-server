package com.MovieTown.exceptions;

public class InvalidScreeningUpdateException extends Exception{

    public InvalidScreeningUpdateException() {
        super("Invalid update. Only screening time can be updated");
    }
}
