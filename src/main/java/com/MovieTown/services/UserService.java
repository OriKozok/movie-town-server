package com.MovieTown.services;

import com.MovieTown.beans.*;
import com.MovieTown.exceptions.*;
import com.MovieTown.repositories.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Scope("prototype")
public class UserService extends ClientService implements Registered{
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private int userId;

    public UserService(MovieRepository movieRepository, ScreeningRepository screeningRepository, CinemaRepository cinemaRepository, UserRepository userRepository,
                       SeatRepository seatRepository, UserRepository userRepository1, OrderRepository orderRepository) {
        super(movieRepository, screeningRepository, cinemaRepository,seatRepository, userRepository);
        this.userRepository = userRepository1;
        this.orderRepository = orderRepository;
    }

    /**
     * This method attempts to log in as a user.
     * @param email the email of the admin
     * @param password the password of the admin
     * @return UserService if the login succeeded and null if it failed.
     */
    public UserService login(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password);
        if(user != null){
            this.userId = user.getId();
            return this;
        }
        else
            return null;
    }

    /***
     * This method receives a User object and updates it in the DB
     * @param user a User object
     * @return an updated User object
     * @throws UserExistsException if there's a user with the mail
     * @throws UnauthorizedEmailException if the received user's mail doesn't contains @ and .
     */
    public User updateUser(User user) throws UserExistsException, UnauthorizedEmailException {
        if(user.getEmail().equals("admin@admin.com"))
            throw new UserExistsException();
        if(userRepository.findAll().stream().anyMatch(u -> user.getEmail().equals(u.getEmail()) && user.getId() != u.getId()))
            throw new UserExistsException();
        if(!user.getEmail().contains("@") || !user.getEmail().contains("."))
            throw new UnauthorizedEmailException();
        return userRepository.save(user);
    }

    /***
     * This method returns the user's details
     * @return a User object
     * @throws NoSuchUserException if there's no user with the service's userId
     */
    public User getDetails() throws NoSuchUserException {
        User user = userRepository.findById(this.userId).orElseThrow(NoSuchUserException::new);
        user.setOrders(orderRepository.findByUserId(this.userId));
        return user;
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

    /***
     * This method returns a list of the user's orders
     * @return a list of the user's orders
     */
    public List<Order> getUserOrders(){
        List<Order> orders = this.orderRepository.findByUserId(this.userId);
        for(Order order : orders){
            order.setSeats(seatRepository.findByOrderId(order.getId()));
        }
        return orders;
    }

    /***
     * This method returns an Order object with the given id
     * @param id an Order's io
     * @return an Order object with the given id
     * @throws NoSuchOrderException if there's no order with the given id in the DB
     */
    public Order getOrderById(int id) throws NoSuchOrderException {
        return this.orderRepository.findById(id).orElseThrow(NoSuchOrderException::new);
    }

    /***
     * This method receives a list of seats, creates an order with those seats and adds it to the DB
     * @param seats al ist of Seat objects
     * @return an Order object
     * @throws SeatIsReservedException if the selected seats are reserved for another order
     * @throws NoSuchUserException if there's no user with the service's userId
     * @throws InvalidSeatsException if the seats are not in the same row or not in executive columns
     */
    public Order addOrder(List<Seat> seats) throws SeatIsReservedException, NoSuchUserException, InvalidSeatsException, NoSuchSeatException {
        if(seats.size() == 0)
            throw new NoSuchSeatException();
        if(seats.get(0).isReserved())
            throw new SeatIsReservedException();
        if(seats.size() > 1){
            seats.sort(Comparator.comparing(Seat::getColumn));
            int row = seats.get(0).getRow();
            for (int i = 1; i < seats.size(); i++){
                if(seats.get(i).getRow() != row || seats.get(i).getColumn() != seats.get(i-1).getColumn() +1)
                    throw new InvalidSeatsException();
                if(seats.get(i).isReserved())
                    throw new SeatIsReservedException();
                }
        }
        User user = getDetails();
        Order order = new Order(user, seats);
        orderRepository.save(order);
        for(Seat seat : seats){
            seat.setReserved();
            seat.setOrder(order);
            seatRepository.save(seat);
        }
        return order;
    }

    /***
     * This method cancel's a user's order with the given id
     * @param id
     * @return a confirmation of cancellation
     * @throws UnauthorizedException if the user's id doesn't match the order's user's id
     * @throws NoSuchOrderException if there's no order with that id in the DB
     * @throws OrderCancellationException if the order is already cancelled or had been watched
     */
    public String cancelOrder(int id) throws NoSuchOrderException, OrderCancellationException, UnauthorizedException {
        Order order = orderRepository.findById(id).orElseThrow(NoSuchOrderException::new);
        if(order.getUser().getId() != this.userId)
            throw new UnauthorizedException();
        if(order.getStatus() == Status.PAID_WATCHED)
            throw new OrderCancellationException();
        System.out.println(order);
        for(Seat seat : order.getSeats()){
            seat.setFree();
            seat.setOrder(null);
            seatRepository.save(seat);
        }
        orderRepository.deleteById(id);
        return "Order cancelled";
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserService{" +
                ", userId=" + userId +
                '}';
    }
}
