package com.MovieTown.repositories;

import com.MovieTown.beans.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    public boolean existsByEmail(String email);

    public User findByEmailAndPassword(String email, String password);

    public List<User> findByEmail(String email);
}
