package com.MovieTown.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "screenings")
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Cinema cinema;
    @ManyToOne(fetch = FetchType.EAGER)
    private Movie movie;
    @OneToMany(mappedBy = "screening", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Seat> seats;
    //@Column(nullable = false)
    private Date time;

    public Screening() {
    }

    public Screening(Cinema cinema, Movie movie, Date time) {
        this.cinema = cinema;
        this.movie = movie;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public Movie getMovie() {
        return movie;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Screening{" +
                "id=" + id +
                ", cinema=" + cinema +
                ", movie=" + movie +
                ", time=" + time +
                '}';
    }
}
