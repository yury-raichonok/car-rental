package com.example.carrental.repository;

import com.example.carrental.controller.dto.order.OrderSearchRequest;
import com.example.carrental.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCriteriaRepository {

  Page<Order> findAll(OrderSearchRequest orderSearchRequest);
}
