package com.MovieTown.repositories;

import com.MovieTown.beans.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    public boolean existsByEmail(String email);

    public User findByEmailAndPassword(String email, String password);
}
