package com.MovieTown.exceptions;

public class OrderCancellationException extends Exception{
    public OrderCancellationException() {
        super("Order is cancelled or was already watched");
    }
}
