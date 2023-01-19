package com.MovieTown.controllers;

import com.MovieTown.beans.*;
import com.MovieTown.exceptions.*;
import com.MovieTown.services.AdminService;
import com.MovieTown.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping(path = "/admin")
public class AdminController {

    private HashMap<String, MySession> sessions;

    public AdminController(HashMap<String, MySession> sessions) {
        this.sessions = sessions;
    }

    /***
     * This movie receives a Movie object and adds it to the DB
     * @param movie a Movie object
     * @param request a client's request
     * @return a Movie object or an error request if something's wrong (the client is not an admin or the
     * service wasn't able to add
     */
    @PostMapping(path = "/movies/add")
    public ResponseEntity<?> addMovie(@RequestBody Movie movie, HttpServletRequest request){
        try {
            AdminService adminService = ifAuthorized(request);
            return ResponseEntity.ok(adminService.addMovie(movie));
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /***
     * This movie receives a Movie object and updates it in the DB
     * @param movie a Movie object
     * @param request a client's request
     * @return a Movie object or an error request if something's wrong (the client is not an admin or the service wasn't
     * able to update
     */
    @PutMapping(path = "/movies/update")
    public ResponseEntity<?> updateMovie(@RequestBody Movie movie, HttpServletRequest request){
        try {
            AdminService adminService = ifAuthorized(request);
            return ResponseEntity.ok(adminService.updateMovie(movie));
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NoSuchMovieException | InvalidMovieUpdateException | MovieExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /***
     * This method receives a movie id and deletes the corresponding object in the DB
     * @param id a movie id
     * @param request a client's request
     * @return a confirmation of deletion or an error request if something's wrong (the client is not an admin or
     * the service wasn't able to delete
     */
    @DeleteMapping(path = "/movies/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable int id, HttpServletRequest request){
        try {
            AdminService adminService = ifAuthorized(request);
            return ResponseEntity.ok(adminService.deleteMovie(id));
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NoSuchMovieException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /***
     * This method returns a movie object with a given id
     * @param id a movie's id
     * @param request a client's request
     * @return a movie object or an error message if something's wrong (the client isn't an admin or the service wasn't able to fetch)
     */
    @GetMapping(path = "/movies/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable int id, HttpServletRequest request){
        try {
            AdminService adminService = ifAuthorized(request);
            return ResponseEntity.ok(adminService.getMovieById(id));
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NoSuchMovieException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /***
     * This method returns a list of all the movies in the DB
     * @param request a client's request
     * @return a list of all the movies in the DB or an error message if something's wrong (the client isn't an admin)
     */
    @GetMapping(path = "/movies")
    public ResponseEntity<?> getAllMovies(HttpServletRequest request){
        try {
            AdminService adminService = ifAuthorized(request);
            return ResponseEntity.ok(adminService.getAllMovies());
        }catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /***
     * This method receives a Cinema object and adds it to the DB
     * @param cinema a Cinema object
     * @param request a client's request
     * @return a Cinema object with an updated id or an error message if something's wrong (the client isn't an admin or
     * the service wasn't able to add)
     */
    @PostMapping(path = "/cinemas/add")
    public ResponseEntity<?> addCinema(@RequestBody Cinema cinema, HttpServletRequest request){
        try {
            AdminService adminService = ifAuthorized(request);
            return ResponseEntity.ok(adminService.addCinema(cinema));
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /***
     * This method receives a cinema id and deletes the corresponding object in the DB
     * @param id a Cinema id
     * @param request a client's request
     * @return a confirmation of deletion or an error request if something's wrong (the client is not an admin or
     * the service wasn't able to delete)
     */
    @DeleteMapping(path = "/cinemas/{id}")
    public ResponseEntity<?> deleteCinema(@PathVariable int id, HttpServletRequest request){
        try {
            AdminService adminService = ifAuthorized(request);
            return ResponseEntity.ok(adminService.deleteCinema(id));
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /***
     * This method returns a cinema object with a given id
     * @param id a cinema's id
     * @param request a client's request
     * @return a cinema object or an error message if something's wrong (the client isn't an admin or the service wasn't able to fetch)
     */
    @GetMapping(path = "/cinemas/{id}")
    public ResponseEntity<?> getCinemaById(@PathVariable int id, HttpServletRequest request){
        try {
            AdminService adminService = ifAuthorized(request);
            return ResponseEntity.ok(adminService.getCinemaById(id));
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /***
     * This method returns a list of all the cinemas in the DB
     * @param request a client's request
     * @return a list of all the cinemas in the DB or an error message if something's wrong (the client isn't an admin)
     */
    @GetMapping(path = "/cinemas")
    public ResponseEntity<?> getAllCinemas(HttpServletRequest request){
        try{
            AdminService adminService = ifAuthorized(request);
            return ResponseEntity.ok(adminService.getAllCinemas());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /***
     * This method returns a list of cinemas whose city corresponds to the given one
     * @param city a cinema's city
     * @return a list of cinemas whose city corresponds to the given one or an error message if
     * something's wrong (the client isn't an admin)
     */
    @GetMapping(path = "/cinemas/city/{city}")
    public ResponseEntity<?> getCinemasByCity(@PathVariable String city, HttpServletRequest request){
        try {
            AdminService adminService = ifAuthorized(request);
            return ResponseEntity.ok(adminService.getCinemasByCity(city));
        }catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /***
     * This method receives a Screening object and adds it to the DB
     * @param screening a Screening object
     * @param request a client's request
     * @return a Screening object with an updated id or an error message if something's wrong (the client isn't an admin or
     * the service wasn't able to add)
     */
    @PostMapping(path = "/screenings/add")
    public ResponseEntity<?> addScreening(@RequestBody Screening screening, HttpServletRequest request){
        try {
            AdminService adminService = ifAuthorized(request);
            return ResponseEntity.ok(adminService.addScreening(screening));
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /***
     * This method receives a Screening object and updates it in the DB
     * @param screening a Screening object
     * @param request a client's request
     * @return a Screening object with an updated id or an error message if something's wrong (the client isn't an admin or
     * the service was unable to update)
     */
    @PutMapping(path = "/screenings/update")
    public ResponseEntity<?> updateScreening(@RequestBody Screening screening, HttpServletRequest request){
        try {
            AdminService adminService = ifAuthorized(request);
            return ResponseEntity.ok(adminService.updateScreening(screening));
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NoSuchScreeningException | InvalidScreeningUpdateException | UnavailableTimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /***
     * This method receives a screening id and deletes the corresponding object in the DB
     * @param id a screening id
     * @param request a client's request
     * @return a confirmation of deletion or an error request if something's wrong (the client is not an admin or
     * the service wasn't able to delete)
     */
    @DeleteMapping(path = "/screenings/{id}")
    public ResponseEntity<?> deleteScreening(@PathVariable int id, HttpServletRequest request){
        try {
            AdminService adminService = ifAuthorized(request);
            return ResponseEntity.ok(adminService.deleteScreening(id));
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /***
     * This method returns a Screening object with a given id
     * @param id a screening's id
     * @param request a client's request
     * @return a Screening object or an error message if something's wrong (the client isn't an admin or the service wasn't able to fetch)
     */
    @GetMapping(path = "/screenings/{id}")
    public ResponseEntity<?> getScreeningById(@PathVariable int id, HttpServletRequest request){
        try {
            AdminService adminService = ifAuthorized(request);
            return ResponseEntity.ok(adminService.getScreeningById(id));
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /***
     * This method returns a list of all the screenings in the DB
     * @param request a client's request
     * @return a list of all the screenings in the DB or an error message if something's wrong (the client isn't an admin)
     */
    @GetMapping(path = "/screenings")
    public ResponseEntity<?> getAllScreenings(HttpServletRequest request){
        try {
            AdminService adminService = ifAuthorized(request);
            return ResponseEntity.ok(adminService.getAllScreenings());
        }catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /***
     * This method returns a list of screenings whose movie id corresponds to the given one
     * @param id a movie id
     * @param request a client's request
     * @return a list of screenings whose movie id corresponds to the given one or an error message if something's wrong
     * (the client isn't an admin)
     */
    @GetMapping(path = "/screenings/movie/{id}")
    public ResponseEntity<?> getAllScreeningsByMovie(int id, HttpServletRequest request){
        try{
            AdminService adminService = ifAuthorized(request);
            return ResponseEntity.ok(adminService.getScreeningsByMovieId(id));
        }catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /***
     * This method returns a list of screenings whose cinema's city corresponds to the given one
     * @param city a cinema's city
     * @param request a client's request
     * @return a list of screenings whose cinema's city corresponds to the given one or an error message if something's wrong
     * (the client isn't an admin)
     */
    @GetMapping(path = "/screenings/city/{city}")
    public ResponseEntity<?> getAllScreeningsByCity(String city, HttpServletRequest request){
        try{
            AdminService adminService = ifAuthorized(request);
            return ResponseEntity.ok(adminService.getScreeningsByCity(city));
        }catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /***
     * This method returns a list of screenings whose movie's genre corresponds to the given one
     * @param genre a movie's genre
     * @param request a client's request
     * @return a list of screenings whose movie's genre corresponds to the given one or an error message if something's wrong
     * (the client isn't an admin)
     */
    @GetMapping(path = "/screenings/genre/{genre}")
    public ResponseEntity<?> getAllScreeningsByGenre(Genre genre, HttpServletRequest request){
        try{
            AdminService adminService = ifAuthorized(request);
        return ResponseEntity.ok(adminService.getScreeningsByGenre(genre));
        }catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /***
     * This method receives a screening's id and returns a list of its seats
     * @param id a screening id
     * @param request a client's request
     * @return a list of seats or an error message if something's wrong (the client isn't a user)
     */
    @GetMapping(path = "/screenings/seats/{id}")
    public ResponseEntity<?> getSeatsOfScreening(@PathVariable int id, HttpServletRequest request){
        try{
            AdminService adminService = ifAuthorized(request);
            return  ResponseEntity.ok(adminService.getSeatsOfScreening(id));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NoSuchScreeningException | ScreeningWasScreenedException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /***
     * This method returns a list of all the users in the DB
     * @param request a client's request
     * @return a list of all the users in the DB or an error message if something's wrong (the client isn't an admin)
     */
    @GetMapping(path = "/users")
    public ResponseEntity<?> getAllUsers(HttpServletRequest request){
        try{
            AdminService adminService = ifAuthorized(request);
            return  ResponseEntity.ok(adminService.getAllUsers());
        }catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /***
     * This method returns a User object with a given id
     * @param id a user's id
     * @param request a client's request
     * @return a User object or an error message if something's wrong (the client isn't an admin or the service wasn't able to fetch)
     */
    @GetMapping(path = "/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id, HttpServletRequest request){
        try {
            AdminService adminService = ifAuthorized(request);
            return ResponseEntity.ok(adminService.getUserById(id));
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /***
     * This method returns a list of all the orders in the DB
     * @param request a client's request
     * @return a list of all the orders in the DB or an error message if something's wrong (the client isn't an admin)
     */
    @GetMapping(path = "/orders")
    public ResponseEntity<?> getAllOrders(HttpServletRequest request){
        try{
            AdminService adminService = ifAuthorized(request);
            return  ResponseEntity.ok(adminService.getAllOrders());
        }catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /***
     * This method checks if the token's type is admin and if there's an active session for it
     * @param request a client's request
     * @return an AdminService object
     * @throws UnauthorizedException if the token's type is user or if there's no active session for it
     */
    private AdminService ifAuthorized(HttpServletRequest request) throws UnauthorizedException {
        Object type = request.getAttribute("type");
        if(type != null && !(type.toString().equals("ADMINISTRATOR"))) {
            throw new UnauthorizedException();
        }
        try {
            String token = request.getHeader("authorization").replace("Bearer ", "");
            MySession session = sessions.get(token);
            session.setLastActive(System.currentTimeMillis());
            return (AdminService) session.getService();
        }catch (Exception e){
            throw new UnauthorizedException();
        }
    }
}
