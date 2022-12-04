package com.MovieTown.repositories;

import com.MovieTown.beans.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {

    public List<Order> findByUserId(int id);
}
