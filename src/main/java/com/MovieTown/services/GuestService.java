package com.MovieTown.services;

import com.MovieTown.beans.*;
import com.MovieTown.exceptions.UnauthorizedEmailException;
import com.MovieTown.exceptions.UserExistsException;
import com.MovieTown.repositories.CinemaRepository;
import com.MovieTown.repositories.MovieRepository;
import com.MovieTown.repositories.ScreeningRepository;
import com.MovieTown.repositories.UserRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class GuestService extends ClientService{

    public GuestService(MovieRepository movieRepository, ScreeningRepository screeningRepository, CinemaRepository cinemaRepository, UserRepository userRepository) {
        super(movieRepository, screeningRepository, cinemaRepository, userRepository);
    }

    public User register(User user) throws UserExistsException, UnauthorizedEmailException {//works
        if(userRepository.existsByEmail(user.getEmail()) || user.getEmail().equals("admin@admin.com"))
            throw new UserExistsException();
        if(!user.getEmail().contains("@") || !user.getEmail().contains("."))
            throw new UnauthorizedEmailException();
        return userRepository.save(user);
    }
}
