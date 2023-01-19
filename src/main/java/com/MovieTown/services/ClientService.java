package com.MovieTown.services;

import com.MovieTown.beans.*;
import com.MovieTown.exceptions.*;
import com.MovieTown.repositories.*;
import org.springframework.stereotype.Service;

import java.util.*;

//this class consists of methods and attributes every client has
@Service
public class ClientService {

    protected MovieRepository movieRepository;
    protected ScreeningRepository screeningRepository;
    protected CinemaRepository cinemaRepository;

    protected SeatRepository seatRepository;
    protected UserRepository userRepository;

    public ClientService(MovieRepository movieRepository, ScreeningRepository screeningRepository, CinemaRepository cinemaRepository,
                         SeatRepository seatRepository ,UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
        this.cinemaRepository = cinemaRepository;
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
    }

    /**
     * This method receives a User object and adds it to the DB
     * @param user a User object
     * @return the User object with an updated id
     * @throws UserExistsException if the user's email is already in use
     * @throws UnauthorizedEmailException if the user's email doesn't have @ and .
     */
    public User register(User user) throws UserExistsException, UnauthorizedEmailException {
        if(user.getEmail().equals("admin@admin.com"))
            throw new UserExistsException();
        if(userRepository.existsByEmail(user.getEmail()) || user.getEmail().equals("admin@admin.com"))
            throw new UserExistsException();
        if(!user.getEmail().contains("@") || !user.getEmail().contains("."))
            throw new UnauthorizedEmailException();
        return userRepository.save(user);
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

    public List<Movie> getMoviesByGenre(Genre genre){
        return movieRepository.findByGenre(genre);
    }

    /***
     * This method returns a list of movies that have screenings in the given city
     * @param city a string representing a city in which there are screenings
     * @return a list of movies that have screenings in the given city
     */
    public List<Movie> getMoviesByCity(String city){
        if(city == null)
            return new ArrayList<>();
        List<Cinema> halls = cinemaRepository.findByCity(city.toLowerCase());
        if(halls.size() == 0){
            return new ArrayList<>();
        }
        List<Screening> screenings = new LinkedList<>();
        for (Cinema hall : halls) {
            screenings.addAll(screeningRepository.findByCinemaId(hall.getId()));
        }
        Set<Movie> movies = new HashSet<>();
        for(Screening screening : screenings){
            movies.add(screening.getMovie());
        }
        return new LinkedList<>(movies);
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
    public Screening getScreeningById(int id) throws NoSuchScreeningException {
        return screeningRepository.findById(id).orElseThrow(NoSuchScreeningException::new);
    }

    /***
     * This method returns a list of screenings whose movie id corresponds to the given one that were not screened
     * @param id a movie id
     * @return a list of screenings whose movie id corresponds to the given one that were not screened
     */
    public List<Screening> getScreeningsByMovieId(int id){
        return screeningRepository.findByMovieIdAndTimeGreaterThan(id, new Date());
    }

    /***
     * This method returns a list of screenings whose cinema's city corresponds to the given one
     * @param city a cinema's city
     * @return a list of screenings whose cinema's city corresponds to the given one
     */
    public List<Screening> getScreeningsByCity(String city){//works
        List<Cinema> halls = cinemaRepository.findByCity(city.toLowerCase());
        if(halls.size() == 0){
            return new ArrayList<>();
        }
        List<Screening> screenings = new LinkedList<>();
        for (Cinema hall : halls) {
            screenings.addAll(screeningRepository.findByCinemaId(hall.getId()));
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

    /***
     * This method returns a list of the screening's seats corresponding to the given id
     * @param id an Screening id
     * @return a list of the screening's seats corresponding to the given id
     */
    public List<Seat> getSeatsOfScreening(int id) throws NoSuchScreeningException, ScreeningWasScreenedException {
        Screening screening = screeningRepository.findById(id).orElseThrow(NoSuchScreeningException::new);
        if(screening.getTime().getTime() < new Date().getTime())
            throw new ScreeningWasScreenedException();
        return this.seatRepository.findByScreeningId(id);
    }
}
