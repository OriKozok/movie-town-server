package com.MovieTown.repositories;

import com.MovieTown.beans.Genre;
import com.MovieTown.beans.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    public List<Movie> findByGenre(Genre genre);
}
