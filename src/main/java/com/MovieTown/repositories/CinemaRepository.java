package com.MovieTown.repositories;

import com.MovieTown.beans.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema,Integer> {

    public List<Cinema> findByCity(String city);

    public boolean existsByCityAndHallId(String city, int hallId);
}
