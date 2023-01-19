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
import java.util.List;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    private HashMap<String, MySession> sessions;

    public UserController(HashMap<String, MySession> sessions) {
        this.sessions = sessions;
    }

    /***
     * This method receives a User object and updates it in the DB
     * @param user a User object
     * @param request a client's request
     * @return a User object or an error request if something's wrong (the client is not a user or the service wasn't
     * able to update
     */
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user, HttpServletRequest request){
        try {
            UserService userService= ifAuthorized(request);
            return ResponseEntity.ok(userService.updateUser(user));
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /***
     * This method returns a User object with details corresponding to the one in the DB with the service's id
     * @param request a client's request
     * @return a User object or an error message if something's wrong (the client isn't a user or the service wasn't able to fetch)
     */
    @GetMapping(path = "/details")
    public ResponseEntity<?> getDetails(HttpServletRequest request){
        try {
            UserService userService= ifAuthorized(request);
            return ResponseEntity.ok(userService.getDetails());
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NoSuchUserException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /***
     * This method returns a list of all the movies in the DB
     * @param request a client's request
     * @return a list of all the movies in the DB or an error message if something's wrong (the client isn't a user)
     */
    @GetMapping(path = "/movies")
    public ResponseEntity<?> getAllMovies(HttpServletRequest request){
        try {
            UserService userService = ifAuthorized(request);
            return ResponseEntity.ok(userService.getAllMovies());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /***
     * This method returns a movie object with a given id
     * @param id a movie's id
     * @param request a client's request
     * @return a movie object or an error message if something's wrong (the client isn't a user or the service wasn't able to fetch)
     */
    @GetMapping(path = "/movies/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable int id, HttpServletRequest request){
        try {
            UserService userService= ifAuthorized(request);
            return ResponseEntity.ok(userService.getMovieById(id));
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NoSuchMovieException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /***
     * This method returns a list of all the screenings in the DB
     * @param request a client's request
     * @return a list of all the screenings in the DB or an error message if something's wrong (the client isn't a user)
     */
    @GetMapping(path = "/screenings")
    public ResponseEntity<?> getAllScreenings(HttpServletRequest request){
        try {
            UserService userService = ifAuthorized(request);
            return ResponseEntity.ok(userService.getAllScreenings());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /***
     * This method returns a list of screenings whose movie id corresponds to the given one
     * @param id a movie id
     * @param request a client's request
     * @return a list of screenings whose movie id corresponds to the given one or an error message if something's wrong
     * (the client isn't a user)
     */
    @GetMapping(path = "/screenings/movie/{id}")
    public ResponseEntity<?> getAllScreeningsByMovie(@PathVariable int id, HttpServletRequest request){
        try{
            UserService userService= ifAuthorized(request);
            return ResponseEntity.ok(userService.getScreeningsByMovieId(id));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /***
     * This method returns a list of screenings whose cinema's city corresponds to the given one
     * @param city a cinema's city
     * @param request a client's request
     * @return a list of screenings whose cinema's city corresponds to the given one or an error message if something's wrong
     * (the client isn't a user)
     */
    @GetMapping(path = "/screenings/city/{city}")
    public ResponseEntity<?> getAllScreeningsByCity(@PathVariable String city, HttpServletRequest request){
        try{
            UserService userService= ifAuthorized(request);
            return  ResponseEntity.ok(userService.getScreeningsByCity(city));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /***
     * This method returns a list of screenings whose movie's genre corresponds to the given one
     * @param genre a movie's genre
     * @param request a client's request
     * @return a list of screenings whose movie's genre corresponds to the given one or an error message if something's wrong
     * (the client isn't a user)
     */
    @GetMapping(path = "/screenings/genre/{genre}")
    public ResponseEntity<?> getAllScreeningsByGenre(@PathVariable Genre genre, HttpServletRequest request){
        try{
            UserService userService= ifAuthorized(request);
            return ResponseEntity.ok(userService.getScreeningsByGenre(genre));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /***
     * This method returns a Screening object with a given id
     * @param id a screening's id
     * @param request a client's request
     * @return a Screening object or an error message if something's wrong (the client isn't a user or the service wasn't able to fetch)
     */
    @GetMapping(path = "/screenings/{id}")
    public ResponseEntity<?> getScreeningById(@PathVariable int id, HttpServletRequest request){
        try {
            UserService userService= ifAuthorized(request);
            return ResponseEntity.ok(userService.getScreeningById(id));
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NoSuchScreeningException e) {
            return ResponseEntity.status(404).body(e.getMessage());
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
        UserService userService= ifAuthorized(request);
        return  ResponseEntity.ok(userService.getSeatsOfScreening(id));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NoSuchScreeningException | ScreeningWasScreenedException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /***
     * This method returns a list of orders of the service's user
     * @param request a client' request
     * @return a list of the user's orders or an error message if something's wrong (the client isn't a user)
     */
    @GetMapping(path = "/orders")
    public ResponseEntity<?> getOrders(HttpServletRequest request){
        try{
            UserService userService= ifAuthorized(request);
            return ResponseEntity.ok(userService.getUserOrders());
        }catch (UnauthorizedException e){
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /***
     * This method returns an order object with a given id
     * @param id an order's id
     * @param request a client's request
     * @return an order object or an error message if something's wrong (the client isn't a user or the service wasn't able to fetch)
     */
    @GetMapping(path = "/orders/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable int id, HttpServletRequest request){
        try {
            UserService userService= ifAuthorized(request);
            return ResponseEntity.ok(userService.getOrderById(id));
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NoSuchOrderException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /***
     * This method receives a list of seats' ids and add an order to the DB with those seats
     * @param seats a list of seats
     * @param request a client's request
     * @return an Order object or an error message if something's wrong (the client isn't a user or the service wasn't able to add)
     */
    @PostMapping(path = "/orders")
    public ResponseEntity<?> addOrder(@RequestBody List<Seat> seats, HttpServletRequest request){
        try {
            UserService userService= ifAuthorized(request);
            return ResponseEntity.ok(userService.addOrder(seats));
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /***
     * This method receives an order's id and cancel it in the DB
     * @param id an order's id
     * @param request a client's request
     * @return a confirmation of cancellation or an error message if something's wrong (the client isn't a user or the service wasn't able to cancel)
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> cancelOrder(@PathVariable int id, HttpServletRequest request){
        try {
            UserService userService= ifAuthorized(request);
            return ResponseEntity.ok(userService.cancelOrder(id));
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NoSuchOrderException | OrderCancellationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /***
     * This method checks if the token's type is user and if there's an active session for it
     * @param request a client's request
     * @return a UserService object
     * @throws UnauthorizedException if the token's type is admin or if there's no active session for it
     */
    private UserService ifAuthorized(HttpServletRequest request) throws UnauthorizedException {
        try {
            String type = request.getAttribute("type").toString();
            if(!type.equals("USER")) {
                throw new UnauthorizedException();
            }
            String token = request.getHeader("authorization").replace("Bearer ", "");
            MySession session = sessions.get(token);
            session.setLastActive(System.currentTimeMillis());
            return (UserService) session.getService();
        }catch (Exception e){
            throw new UnauthorizedException();
        }
    }
}
