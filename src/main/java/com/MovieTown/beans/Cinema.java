package com.MovieTown.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "cinemas")
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String city;
    private int numOfRows;
    private int numOfColumns;

    private int hallId;
    @OneToMany(mappedBy = "cinema", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Screening> screenings;

    public Cinema() {
    }

    public Cinema(String city, int numOfRows, int numOfColumns) {
        this.city = city;
        this.numOfRows = numOfRows;
        this.numOfColumns = numOfColumns;
    }

    public int getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }

    public int getNumOfColumns() {
        return numOfColumns;
    }

    public void setNumOfColumns(int numOfColumns) {
        this.numOfColumns = numOfColumns;
    }

    public int getHallId() {
        return hallId;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public List<Screening> getScreenings() {
        return screenings;
    }

    public void setScreenings(List<Screening> screenings) {
        this.screenings = screenings;
    }

    public boolean addScreening(Screening screening){
        return screenings.add(screening);
    }

    @Override
    public String toString() {
        return "Cinema{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", numOfRows=" + numOfRows +
                ", numOfColumns=" + numOfColumns +
                ", hallId=" + hallId +
                '}';
    }
}
