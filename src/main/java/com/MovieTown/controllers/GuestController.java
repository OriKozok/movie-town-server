package com.MovieTown.controllers;

import com.MovieTown.beans.*;
import com.MovieTown.exceptions.*;
import com.MovieTown.services.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/guest")
public class GuestController {

    //private sessions

    private GuestService guestService;
    private HashMap<String, MySession> sessions;
    private final ApplicationContext applicationContext;

    public GuestController(GuestService guestService, HashMap<String, MySession> sessions, ApplicationContext applicationContext) {
        this.guestService = guestService;
        this.sessions = sessions;
        this.applicationContext = applicationContext;
    }

    /***
     * This method register a user in the DB and returns a token with the user's details
     * The method also generates a token for the user and stores it in a session
     * @param user a User object
     * @return a token with the user's details or an error message if something's wrong
     * (user's registration failed)
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user){
        try {
            user = guestService.register(user);
            System.out.println(user);
            String token = createNewUserToken(user);
            UserService userService = applicationContext.getBean(UserService.class).login(user.getEmail(), user.getPassword());
            System.out.println(userService.getUserId());
            sessions.put(token, new MySession(userService, System.currentTimeMillis()));
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /***
     * This method receives an email and a password from the user and attempts to log him in.
     * The method also generates a token for the user and stores it in a session.
     * @param email user's email
     * @param password user's password
     * @return a token with the user's details or an error message if something's wrong
     * (password or email are wrong)
     */
    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password){
        try {
            Registered registeredService = applicationContext.getBean(AdminService.class).login(email, password);
            if(registeredService == null){
                registeredService = applicationContext.getBean(UserService.class).login(email, password);
                if(registeredService == null)
                    throw new LoginException();
            }
            String token = createToken(registeredService);
            sessions.put(token, new MySession(registeredService, System.currentTimeMillis()));
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    /***
     * This method logs out the user, while removing its active token from the session.
     * @param request a client's request
     * @return String which indicates that the user has signed out or an error message if something's wrong
     * (deletion from the session failed)
     */
    @PostMapping(path = "/out")
    public ResponseEntity<String> signOut(HttpServletRequest request){
        try {
            String token = request.getHeader("authorization").replace("Bearer ", "");
            sessions.remove(token);
            return ResponseEntity.ok("signed out");
        }catch (NullPointerException e){
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /***
     * This method returns a list of all the movies in the DB
     * @return a list of all the movies in the DB
     */
    @GetMapping(path = "/movies")
    public List<Movie> getAllMovies(){
            return guestService.getAllMovies();
    }

    /***
     * This method returns a movie object with a given id
     * @param id a movie's id
     * @return a movie object an error message if something's wrong (no movie with this id)
     */
    @GetMapping(path = "/movies/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable int id){
        try {
            return ResponseEntity.ok(guestService.getMovieById(id));
        } catch (NoSuchMovieException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /***
     * This method returns a list of all the screenings in the DB
     * @return a list of all the screenings in the DB
     */
    @GetMapping(path = "/screenings")
    public List<Screening> getAllScreenings(){
        return guestService.getAllScreenings();
    }

    /***
     * This method returns a Screening object with a given id
     * @param id a screening's id
     * @return a Screening object an error message if something's wrong (no screening with this id)
     */
    @GetMapping(path = "/screenings/{id}")
    public ResponseEntity<?> getScreeningById(@PathVariable int id){
        try {
            return ResponseEntity.ok(guestService.getScreeningById(id));
        } catch (NoSuchScreeningException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /***
     * This method returns a list of screenings whose movie id corresponds to the given one
     * @param id a movie id
     * @return a list of screenings whose movie id corresponds to the given one
     */
    @GetMapping(path = "/screenings/movie/{id}")
    public List<Screening> getAllScreeningsByMovie(int id){
            return guestService.getScreeningsByMovieId(id);
    }

    /***
     * This method returns a list of screenings whose cinema's city corresponds to the given one
     * @param city a cinema's city
     * @return a list of screenings whose cinema's city corresponds to the given one
     */
    @GetMapping(path = "/screenings/city/{city}")
    public List<Screening> getScreeningsByCity(@PathVariable String city){
        return guestService.getScreeningsByCity(city);
    }

    /***
     * This method returns a list of screenings whose movie's genre corresponds to the given one
     * @param genre a movie's genre
     * @return a list of screenings whose movie's genre corresponds to the given one
     */
    @GetMapping(path = "/screenings/genre/{genre}")
    public List<Screening> getScreeningsByGenre(@PathVariable Genre genre){
        return guestService.getScreeningsByGenre(genre);
    }

    /***
     * This method receives a Registered interface and returns a token according to its type
     * @param registeredService a Registered interface
     * @return a token according to its type (AdminService returns an admin token, UserService a user's token)
     * @throws NoSuchUserException if the service is a UserService and there's no user with the service's id
     */
    private String createToken(Registered registeredService) throws NoSuchUserException {
        String token = "";
        if (registeredService instanceof UserService) {
            User user = ((UserService) registeredService).getDetails();
            token = createNewUserToken(user);
        }
        else {
            token = JWT.create()
                    .withIssuer("Movie Town")
                    .withIssuedAt(new Date())
                    .withClaim("name", "admin")
                    .withClaim("email", "admin@admin.com")
                    .withClaim("type", "ADMINISTRATOR")
                    .sign(Algorithm.HMAC256("The town of all movies"));
        }
        return token;
    }

    /***
     * This method creates a user's token according to his details
     * @param user a User object
     * @return a user's token according to his details
     */
    private String createNewUserToken(User user){
        return JWT.create()
                .withIssuer("Movie Town")
                .withIssuedAt(new Date())
                .withClaim("id", user.getId())
                .withClaim("name", user.getName())
                .withClaim("email", user.getEmail())
                .withClaim("type", "USER")
                .sign(Algorithm.HMAC256("The town of all movies"));
    }
}
