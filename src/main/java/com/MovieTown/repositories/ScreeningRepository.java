package com.MovieTown.repositories;

import com.MovieTown.beans.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Integer> {

    public List<Screening> findByCinemaId(int id);

    public List<Screening> findByMovieId(int id);

    public List<Screening> findByTimeLessThan(Date time);
}
