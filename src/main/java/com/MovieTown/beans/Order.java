package com.MovieTown.beans;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int price;

    @Enumerated(value = EnumType.STRING)
    //3 Status modes:
    //1.Paid not watched: for upcoming screenings
    //2.Paid watched: for past screenings
    private Status status;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<Seat> seats;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public Order(User user, List<Seat> seats){
        this.user = user;
        this.seats = seats;
        this.price = seats.size() * 15;
        this.status = Status.PAID_NOT_WATCHED;
    }
    public Order(){}

    public int getId() {
        return id;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setWatchedPaid(){this.status=Status.PAID_WATCHED;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", price=" + price +
                ", status=" + status +
                ", seats=" + seats.size() +
                ", user=" + user +
                '}';
    }
}
