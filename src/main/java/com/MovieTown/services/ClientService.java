package com.MovieTown.services;

import com.MovieTown.beans.Cinema;
import com.MovieTown.beans.Genre;
import com.MovieTown.beans.Movie;
import com.MovieTown.beans.Screening;
import com.MovieTown.exceptions.NoSuchMovieException;
import com.MovieTown.exceptions.NoSuchScreeningException;
import com.MovieTown.repositories.CinemaRepository;
import com.MovieTown.repositories.MovieRepository;
import com.MovieTown.repositories.ScreeningRepository;
import com.MovieTown.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//this class consists of methods and attributes every client has
@Service
public class ClientService {

    protected MovieRepository movieRepository;
    protected ScreeningRepository screeningRepository;
    protected CinemaRepository cinemaRepository;

    protected UserRepository userRepository;

    public ClientService(MovieRepository movieRepository, ScreeningRepository screeningRepository, CinemaRepository cinemaRepository,
                         UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
        this.cinemaRepository = cinemaRepository;
        this.userRepository = userRepository;
    }

    /***
     * This method returns a list of all the movies in the DB
     * @return a list of all the movies in the DB
     */
    public List<Movie> getAllMovies(){//works
        return this.movieRepository.findAll();
    }

    /***
     * This method returns a movie object with a given id
     * @param id a movie's id
     * @return a movie object
     * @throws NoSuchMovieException if there's no movie in the DB with that id
     */
    public Movie getMovieById(int id) throws NoSuchMovieException {//works
        return this.movieRepository.findById(id).orElseThrow(NoSuchMovieException::new);
    }

    /***
     * This method returns a list of all the screenings in the DB
     * @return a list of all the screenings in the DB
     */
    public List<Screening> getAllScreenings(){//works
        return this.screeningRepository.findAll();
    }

    /***
     * This method returns a screening object with a given id
     * @param id a screening's id
     * @return a screening object
     * @throws NoSuchScreeningException if there's no screening in the DB with that id
     */
    public Screening getScreeningById(int id) throws NoSuchScreeningException {//works
        return screeningRepository.findById(id).orElseThrow(NoSuchScreeningException::new);
    }

    /***
     * This method returns a list of screenings whose movie id corresponds to the given one
     * @param id a movie id
     * @return a list of screenings whose movie id corresponds to the given one
     */
    public List<Screening> getScreeningsByMovieId(int id){//works
        return screeningRepository.findByMovieId(id);
    }

    /***
     * This method returns a list of screenings whose cinema's city corresponds to the given one
     * @param city a cinema's city
     * @return a list of screenings whose cinema's city corresponds to the given one
     */
    public List<Screening> getScreeningsByCity(String city){//works
        List<Cinema> cinemas = cinemaRepository.findByCity(city.toLowerCase());
        if(cinemas.size() == 0){
            return new ArrayList<>();
        }
        List<Screening> screenings = new LinkedList<>();
        for (Cinema cinema : cinemas) {
            screenings.addAll(screeningRepository.findByCinemaId(cinema.getId()));
        }
        return screenings;
    }

    /***
     * This method returns a list of screenings whose movie's genre corresponds to the given one
     * @param genre a movie's genre
     * @return a list of screenings whose movie's genre corresponds to the given one
     */
    public List<Screening> getScreeningsByGenre(Genre genre){//works
        List<Movie> movies = movieRepository.findByGenre(genre);
        if(movies.size() == 0){
            return new ArrayList<>();
        }
        List<Screening> screenings = new LinkedList<>();
        for (Movie movie : movies) {
            screenings.addAll(screeningRepository.findByMovieId(movie.getId()));
        }
        return screenings;
    }
}
