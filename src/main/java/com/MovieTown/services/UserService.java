package com.MovieTown.services;

import com.MovieTown.beans.*;
import com.MovieTown.exceptions.*;
import com.MovieTown.repositories.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.persistence.RollbackException;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Scope("prototype")
public class UserService extends ClientService implements Registered{

    private SeatRepository seatRepository;
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private int userId;

    public UserService(MovieRepository movieRepository, ScreeningRepository screeningRepository, CinemaRepository cinemaRepository,
                       UserRepository userRepository, SeatRepository seatRepository, UserRepository userRepository1, OrderRepository orderRepository) {
        super(movieRepository, screeningRepository, cinemaRepository, userRepository);
        this.seatRepository = seatRepository;
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
    public List<Seat> getSeatsOfScreening(int id){//works
        return this.seatRepository.findByScreeningId(id);
    }

    /***
     * This method returns a list of the user's orders
     * @return a list of the user's orders
     */
    public List<Order> getUserOrders(){
        return this.orderRepository.findByUserId(this.userId);
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
     * @param seatsId al ist of seats ids
     * @return an Order object
     * @throws SeatIsReservedException if the selected seats are reserved for another order
     * @throws NoSuchUserException if there's no user with the service's userId
     * @throws InvalidSeatsException if the seats are not in the same row or not in executive columns
     */
    public Order addOrder(List<Integer> seatsId) throws SeatIsReservedException, NoSuchUserException, InvalidSeatsException, NoSuchSeatException {
        //seats must be in the same row and have executive columns
        List<Seat> seats = new LinkedList<>();
        for(int seatId: seatsId){
            Seat seat = seatRepository.findById(seatId).orElseThrow(NoSuchSeatException::new);
            seats.add(seat);
        }
        seats.sort(Comparator.comparing(Seat::getColumn));
        int row = seats.get(0).getRow();
        for (int i = 1; i < seats.size(); i++){
            if(seats.get(i).getRow() != row || seats.get(i).getColumn() != seats.get(i-1).getColumn() +1)
                throw new InvalidSeatsException();
            if(seats.get(i).isReserved())
                throw new SeatIsReservedException();
        }
        User user = getDetails();
        Order order = new Order(user, seats);
        orderRepository.save(order);
        for(Seat seat : seats){
            seat.setReserved();
            seat.setOrder(order);
            System.out.println(seat);
            seatRepository.save(seat);
        }
        System.out.println(order);
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
        if(order.getId() != this.userId)
            throw new UnauthorizedException();
        if(order.getStatus() == Status.CANCELLED || order.getStatus() == Status.PAID_WATCHED)
            throw new OrderCancellationException();
        for(Seat seat : order.getSeats()){
            seat.setFree();
            seat.setOrder(null);
            seatRepository.save(seat);
        }
        order.setCancelled();
        orderRepository.save(order);
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
