package com.example.carrental.repository;

import com.example.carrental.controller.dto.order.OrderSearchRequest;
import com.example.carrental.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

/**
 * Order Criteria Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface OrderCriteriaRepository {

  /**
   * @param orderSearchRequest search parameters.
   * @return orders page filtered by parameters.
   */
  Page<Order> findAll(OrderSearchRequest orderSearchRequest);
}
