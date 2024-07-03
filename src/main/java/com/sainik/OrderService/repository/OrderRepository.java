package com.sainik.OrderService.repository;

import com.sainik.OrderService.entity.Order;
import com.sainik.OrderService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

}
