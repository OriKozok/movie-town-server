package com.MovieTown.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private boolean reserved;
    private int row;
    private int column;
    @ManyToOne(fetch=FetchType.EAGER)
    private Screening screening;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Order order;

    public Seat() {
    }

    public Seat(int row, int column, Screening screening){
        this.row = row;
        this.column = column;
        this.screening = screening;
        this.reserved = false;
    }

    public long getId() {
        return id;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved() {
        reserved = true;
    }

    public void setFree(){reserved = false;}

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", isReserved=" + reserved +
                ", row=" + row +
                ", column=" + column +
                ", screening=" + screening +
                ", order=" + order +
                '}';
    }
}
