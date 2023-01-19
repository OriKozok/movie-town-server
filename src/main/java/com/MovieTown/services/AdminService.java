package com.MovieTown.services;

import com.MovieTown.beans.*;
import com.MovieTown.exceptions.*;
import com.MovieTown.repositories.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService extends ClientService implements Registered{
    private UserRepository userRepository;
    private OrderRepository orderRepository;

    public AdminService(MovieRepository movieRepository, ScreeningRepository screeningRepository, CinemaRepository cinemaRepository,
                        UserRepository userRepository, SeatRepository seatRepository, UserRepository userRepository1, OrderRepository orderRepository) {
        super(movieRepository, screeningRepository, cinemaRepository,seatRepository, userRepository);
        this.seatRepository = seatRepository;
        this.userRepository = userRepository1;
        this.orderRepository = orderRepository;
    }

    /**
     * This method attempts to log in as the admin.
     * @param email the email of the admin
     * @param password the password of the admin
     * @return AdminService if the login succeeded and null if it failed.
     */
    public AdminService login(String email, String password) {
        return email.equals("admin@admin.com") && password.equals("admin") ? this : null;
    }

    /***
     * This method receives a Movie object and adds it to the DB
     * @param movie a Movie object
     * @return a Movie object with an updated id
     * @throws MovieExistsException if the a movie with that name or id exists in the DB
     */
    public Movie addMovie(Movie movie) throws MovieExistsException {
        if(movieRepository.existsById(movie.getId()) || movieRepository.findAll().stream().anyMatch(m -> m.getName().equals(movie.getName())))
            throw new MovieExistsException();
        movie.setName(movie.getName().toUpperCase());
        movie.setDescription(movie.getDescription().toUpperCase().charAt(0)+movie.getDescription().substring(1));//Make description start with capital letter
        return movieRepository.save(movie);
    }

    /***
     * This method receives a Movie object and updates it in the DB
     * @param movie a Movie object
     * @return an updated Movie object
     * @throws NoSuchMovieException if there's no movie in the DB with that id
     * @throws InvalidMovieUpdateException if the received movie's name doesn't match the one in the DB
     */
    public Movie updateMovie(Movie movie) throws NoSuchMovieException, InvalidMovieUpdateException, MovieExistsException {
        Movie movie1 = movieRepository.findById(movie.getId()).orElseThrow(NoSuchMovieException::new);
        if(!movie.getName().equals(movie1.getName()))
            throw new InvalidMovieUpdateException();
        if(movieRepository.findAll().stream().anyMatch(m -> m.getName().equals(movie.getName()) && movie.getId() != m.getId()))
            throw new MovieExistsException();
        return movieRepository.save(movie);
    }

    /***
     * This method receives a movie id and deletes the corresponding object in the DB
     * @param id a Movie id
     * @return a confirmation of deletion
     * @throws NoSuchMovieException if there's no movie in the DB with that id
     */
    public String deleteMovie(int id) throws NoSuchMovieException {
        if(!movieRepository.existsById(id))
            throw new NoSuchMovieException();
        movieRepository.deleteById(id);
        return "Movie deleted";
    }

    /***
     * This method receives a Cinema object and adds it to the DB
     * @param cinema a Cinema object
     * @return a Movie object with an updated id
     * @throws CinemaExistsException if there's a cinema object with that id or with that hallId in that city
     * @throws InvalidCinemaSizeException if the cinema' size is not in the allowed range (Rows must be between 5 and 15, columns between 10 and 30
     */
    public Cinema addCinema(Cinema cinema) throws CinemaExistsException, InvalidCinemaSizeException {
        if(cinemaRepository.existsById(cinema.getId()) || cinemaRepository.existsByCityAndHallId(cinema.getCity(), cinema.getHallId()))
            throw new CinemaExistsException();
        if(cinema.getNumOfColumns() < 10 || cinema.getNumOfColumns() > 30 || cinema.getNumOfRows() < 5 || cinema.getNumOfRows() > 15)
            throw new InvalidCinemaSizeException();
        cinema.setCity(cinema.getCity().toLowerCase());
        List<Cinema> cinemas = cinemaRepository.findByCity(cinema.getCity());
        cinema.setHallId(cinemas.size()+1);
        return cinemaRepository.save(cinema);
    }

    /***
     * This method receives a cinema id and deletes the corresponding object in the DB
     * @param id a Cinema id
     * @return a confirmation of deletion
     * @throws NoSuchCinemaException if there's no cinema in the DB with that id
     */
    public String deleteCinema(int id) throws NoSuchCinemaException {
        Cinema cinema = cinemaRepository.findById(id).orElseThrow(NoSuchCinemaException::new);
        List<Cinema> cinemas = cinemaRepository.findByCity(cinema.getCity());
        int hallId = cinema.getHallId();
        cinemas = cinemas.stream().filter(cin-> cin.getHallId() > hallId).collect(Collectors.toList());
        for(Cinema cinema1 : cinemas){
            cinema1.setHallId(cinema1.getHallId() - 1);
            cinemaRepository.save(cinema1);
        }
        cinemaRepository.deleteById(id);
        return "Cinema deleted";
    }

    /***
     * This method returns a Cinema object with a given id
     * @param id a cinema's id
     * @return a cinema object
     * @throws NoSuchCinemaException if there's no cinema in the DB with that id
     */
    public Cinema getCinemaById(int id) throws NoSuchCinemaException {
        Cinema cinema = cinemaRepository.findById(id).orElseThrow(NoSuchCinemaException::new);
        cinema.setScreenings(screeningRepository.findByCinemaId(id));
        return cinema;
    }

    /***
     * This method returns a list of all the cinemas in the DB
     * @return a list of all the cinemas in the DB
     */
    public List<Cinema> getAllCinemas(){
        return cinemaRepository.findAll();
    }

    /***
     * This method returns a list of cinemas whose city corresponds to the given one
     * @param city a cinema's city
     * @return a list of cinemas whose city corresponds to the given one
     */
    public List<Cinema> getCinemasByCity(String city){
        return cinemaRepository.findByCity(city.toLowerCase());
    }

    /***
     * This method receives a Screening object, creates seats for it and add them and the screening to the DB
     * @param screening a Screening object
     * @return a Movie object with an updated id
     * @throws UnavailableTimeException if there's already an existing screening in that cinema during the given one's time
     * @throws ScreeningExistsException if there's a screening with that id
     * @throws ScreeningExistsException if the given screening has seats
     */
    public Screening addScreening(Screening screening) throws UnavailableTimeException, ScreeningExistsException, ScreeningWithSeatsException {
        if(screeningRepository.existsById(screening.getId())){
            throw new ScreeningExistsException();
        }
        if(screening.getSeats() != null){
            throw new ScreeningWithSeatsException();
        }
        List<Screening> screenings = screeningRepository.findByCinemaId(screening.getCinema().getId());
        long startScreening = screening.getTime().getTime();
        long endScreening = startScreening + (screening.getMovie().getDuration()* 60000L);//60000L is 1 minute in long
        //There are 2 options for InvalidTimeException: 1. The screening time is not available
        // 2. Because the screening's time is in the past
        if(screenings.stream().anyMatch(s -> checkScreeningsTime(startScreening, endScreening, s)) || screening.getTime().getTime() < new Date().getTime())
            throw new UnavailableTimeException();
        screening = screeningRepository.save(screening);
        List<Seat> seats = new LinkedList<>();
        Cinema cinema = screening.getCinema();
        for(int i = 1; i <= cinema.getNumOfRows(); i++){
            for(int j = 1; j <= cinema.getNumOfColumns(); j++){
                Seat seat = new Seat(i, j, screening);
                seats.add(seat);
                seatRepository.save(seat);
            }
        }
        screening.setSeats(seats);
        return screening;
    }

    /***
     * This method receives a Screening object and updates it in the DB
     * @param screening a Screening object
     * @return an updated Screening object
     * @throws NoSuchScreeningException if there's no screening in the DB with that id
     * @throws InvalidScreeningUpdateException if the given screening's movie, cinema or seats is different from the one in the DB
     * (only screening's time can change)
     * @throws UnavailableTimeException if there's already an existing screening in that cinema during the given one's time
     */
    public Screening updateScreening(Screening screening) throws NoSuchScreeningException, InvalidScreeningUpdateException, UnavailableTimeException {
        Screening s = screeningRepository.findById(screening.getId()).orElseThrow(NoSuchScreeningException::new);
        List<Seat> screeningSeats = screening.getSeats();
        List<Seat> sSeats = s.getSeats();
        if(screening.getSeats() != null){
            if (screeningSeats.size() != sSeats.size())
                throw new InvalidScreeningUpdateException();
            for (int i = 0; i < screeningSeats.size(); i++) {
                if (!screeningSeats.get(i).equals(sSeats.get(i)))
                    throw new InvalidScreeningUpdateException();
            }
        }
        if ((screening.getMovie().getId() != s.getMovie().getId()) || (screening.getCinema().getId() != s.getCinema().getId())) {
            throw new InvalidScreeningUpdateException();
        }
        List<Screening> screenings = screeningRepository.findByCinemaId(screening.getCinema().getId());
        System.out.println(screenings);
        long screeningTime = screening.getTime().getTime();
        long endScreening = screeningTime + (screening.getMovie().getDuration() * 60000L);//60000L is 1 minute in long
        //There are 2 options for InvalidTimeException: 1. The screening time is not available (unless it's because the same one who is not up-to-date)
        // 2. Because the screening's time is in the past
        if (screenings.stream().anyMatch(scr -> scr.getId() != screening.getId() && checkScreeningsTime(screeningTime, endScreening, scr))
                || screening.getTime().getTime() < new Date().getTime())
            throw new UnavailableTimeException();
        return screeningRepository.save(screening);
    }

    /***
     * This method receives a Screening id and deletes the corresponding object in the DB
     * @param id a Screening id
     * @return a confirmation of deletion
     * @throws NoSuchScreeningException if there's no screening in the DB with that id
     */
    public String deleteScreening(int id) throws NoSuchScreeningException {
        if(!screeningRepository.existsById(id)){
            throw new NoSuchScreeningException();
        }
        screeningRepository.deleteById(id);
        return "Screening deleted";
    }

    /***
     * This method returns a list of all the users in the DB
     * @return a list of all the users in the DB
     */
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    /***
     * This method returns a User object with a given id
     * @param id a user's id
     * @return a User object
     * @throws NoSuchUserException if there's no user in the DB with that id
     */
    public User getUserById(int id) throws NoSuchUserException {
        User user =  userRepository.findById(id).orElseThrow(NoSuchUserException::new);
        user.setOrders(orderRepository.findByUserId(id));
        return user;
    }

    /***
     * This method returns a list of all the orders in the DB
     * @return a list of Order objects
     */
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    /***
     * This method checks if there's a screening from DB which runs during the one that is added/updated
     * @param startScreening the added/updated screening's start time
     * @param endScreening the added/updated screening's start time
     * @param s a Screening object from the DB
     * @return true if the screening's time is good na false if not
     */
    private boolean checkScreeningsTime(long startScreening, long endScreening, Screening s){
        //1. screening starts before the one from the DB but ends during it
        //2. screening starts during the one from the DB
        long startS = s.getTime().getTime();
        long endS =  startS + (s.getMovie().getDuration()* 60000L);
        boolean temp = (startS > startScreening && startS < endScreening) || (startS < startScreening && endS > startScreening);
        System.out.println(temp);
        return temp;
    }

    @Override
    public String toString() {
        return "AdminService";
    }
}
