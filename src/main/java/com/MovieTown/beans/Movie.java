package com.MovieTown.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //@Column(nullable = false)
    private String name;
    @Enumerated(value = EnumType.STRING)
    //@Column(nullable = false)
    private Genre genre;
    private String description;

    //@Column(columnDefinition = "MEDIUMTEXT")
    //private String image;
    private int duration;//in minutes
    @OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Screening> screenings;

    public Movie() {
    }

    public Movie(String name, Genre genre, String description, int duration) {
        this.name = name;
        this.genre = genre;
        this.description = description;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", genre=" + genre +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                '}';
    }
}
