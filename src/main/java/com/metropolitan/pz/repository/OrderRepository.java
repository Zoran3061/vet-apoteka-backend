package com.metropolitan.pz.repository;

import com.metropolitan.pz.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findTopByOrderByIdDesc();
}

