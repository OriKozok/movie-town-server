package com.MovieTown.services;

import org.springframework.stereotype.Service;

//This interface is used for services that require a registered account: admin service and user service
@Service
public interface Registered {

    public abstract Registered login(String email, String password);
}
