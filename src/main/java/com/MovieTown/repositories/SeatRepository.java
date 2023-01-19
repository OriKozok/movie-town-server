package com.MovieTown.repositories;

import com.MovieTown.beans.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {

    public List<Seat> findByScreeningId(int id);
    public List<Seat> findByOrderId(int id);
}
