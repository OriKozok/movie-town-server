package com.MovieTown;

import com.MovieTown.beans.*;
import com.MovieTown.exceptions.InvalidSeatsException;
import com.MovieTown.exceptions.NoSuchSeatException;
import com.MovieTown.exceptions.NoSuchUserException;
import com.MovieTown.exceptions.SeatIsReservedException;
import com.MovieTown.repositories.*;
import com.MovieTown.services.AdminService;
import com.MovieTown.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
public class MovieTownApplication {

	public static void main(String[] args) {
		//try {
			ConfigurableApplicationContext ctx = SpringApplication.run(MovieTownApplication.class, args);
			MovieRepository movieRepository = ctx.getBean(MovieRepository.class);
			CinemaRepository cinemaRepository = ctx.getBean(CinemaRepository.class);
			ScreeningRepository screeningRepository = ctx.getBean(ScreeningRepository.class);
			SeatRepository seatRepository = ctx.getBean(SeatRepository.class);
			//UserRepository userRepository = ctx.getBean(UserRepository.class);
			//OrderRepository orderRepository = ctx.getBean(OrderRepository.class);
			//Movie movie = new Movie("hi", Genre.ACTION, "hi", 100);
			//movie = movieRepository.save(movie);
			//Cinema cinema = new Cinema("ramat gan", 10, 10);
			//cinema = cinemaRepository.save(cinema);
			//Screening screening = new Screening(cinemaRepository.findById(1).orElseThrow(), movieRepository.findById(3).orElseThrow(), new Date(2030, 1, 1));
			//screening = screeningRepository.save(screening);
			//Seat seat = new Seat(1, 1, screening);
			//screeningRepository.deleteById(screeningRepository.findById(3).orElseThrow().getId());
			//User user = new User("ori", "ori@gmail.com");
			//user = userRepository.save(user);
			//Order order = new Order(userRepository.findById(1).orElseThrow(), seatRepository.findAll());
			//orderRepository.save(order);
			//Seat seat = seatRepository.findById(2).orElseThrow();
			//seat.setOrder(order);
			//seatRepository.save(seat);
			//System.out.println(orderRepository.findById(2).orElseThrow().getSeats());
			UserRepository userRepository = ctx.getBean(UserRepository.class);
			AdminService adminService = ctx.getBean(AdminService.class);
			UserService userService = ctx.getBean(UserService.class);
			userService.setUserId(1);
			List<Integer> seats = new ArrayList<>();
			seats.add(1);
			seats.add(2);
			//userService.addOrder(seats);
			//Calendar calendar = Calendar.getInstance();
			//calendar.add(Calendar.DAY_OF_MONTH, 28);
			//adminService.addScreening(new Screening(adminService.getCinemaById(1), adminService.getMovieById(1), calendar.getTime()));
		//}catch (NoSuchUserException | SeatIsReservedException | InvalidSeatsException | NoSuchSeatException e){
		//	System.out.println(e.getMessage());
		//}
	}

	@Bean
	public HashMap<String, MySession> sessions(){
		return new HashMap<String, MySession>();
	}
}
