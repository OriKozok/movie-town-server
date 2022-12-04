package com.MovieTown.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private boolean isReserved;
    private int row;
    private int column;
    @ManyToOne(fetch=FetchType.EAGER)
    private Screening screening;
    @ManyToOne(cascade = CascadeType.DETACH)
    @JsonIgnore
    private Order order;

    public Seat() {
    }

    public Seat(int row, int column, Screening screening){
        this.row = row;
        this.column = column;
        this.screening = screening;
    }

    @PreRemove
    private void UpdateOrderStatus(){
        if(this.order != null) {
            if (this.screening.getTime().getTime() < new Date(System.currentTimeMillis()).getTime())
                this.order.setWatchedPaid();
            else
                this.order.setCancelled();
        }
    }

    public int getId() {
        return id;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved() {
        isReserved = true;
    }

    public void setFree(){isReserved = false;}

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
                ", isReserved=" + isReserved +
                ", row=" + row +
                ", column=" + column +
                ", screening=" + screening +
                ", order=" + order +
                '}';
    }
}
