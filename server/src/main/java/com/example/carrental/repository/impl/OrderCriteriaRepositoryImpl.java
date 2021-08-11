package com.example.carrental.repository.impl;

import com.example.carrental.controller.dto.order.OrderSearchRequest;
import com.example.carrental.entity.car.Car;
import com.example.carrental.entity.order.Order;
import com.example.carrental.entity.user.User;
import com.example.carrental.repository.OrderCriteriaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderCriteriaRepositoryImpl implements OrderCriteriaRepository {

  private final EntityManager entityManager;

  @Override
  public Page<Order> findAll(OrderSearchRequest orderSearchRequest) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Order> orderCriteriaQuery = criteriaBuilder.createQuery(Order.class);
    Root<Order> orderRoot = orderCriteriaQuery.from(Order.class);
    Predicate predicate = getPredicate(criteriaBuilder, orderSearchRequest, orderRoot);
    orderCriteriaQuery.where(predicate);
    setOrder(criteriaBuilder, orderSearchRequest, orderCriteriaQuery, orderRoot);

    TypedQuery<Order> orderTypedQuery = entityManager.createQuery(orderCriteriaQuery);
    orderTypedQuery
        .setFirstResult(orderSearchRequest.getPageNumber() * orderSearchRequest.getPageSize());
    orderTypedQuery.setMaxResults(orderSearchRequest.getPageSize());

    Pageable pageable = getPageable(orderSearchRequest);

    long ordersCount = getOrdersCount(criteriaBuilder, predicate);

    List<Order> orders = orderTypedQuery.getResultList();

    return new PageImpl<>(orders, pageable, ordersCount);
  }

  private long getOrdersCount(CriteriaBuilder criteriaBuilder, Predicate predicate) {
    CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
    Root<Order> orderCountRoot = countQuery.from(Order.class);
    orderCountRoot.join("user");
    orderCountRoot.join("car");
    countQuery.select(criteriaBuilder.count(orderCountRoot)).where(predicate);
    return entityManager.createQuery(countQuery).getSingleResult();
  }

  private Pageable getPageable(OrderSearchRequest orderSearchRequest) {
    Sort sort = Sort.by(orderSearchRequest.getSortDirection(), orderSearchRequest.getSortBy());
    return PageRequest
        .of(orderSearchRequest.getPageNumber(), orderSearchRequest.getPageSize(), sort);
  }

  private Predicate getPredicate(CriteriaBuilder criteriaBuilder,
      OrderSearchRequest orderSearchRequest,
      Root<Order> orderRoot) {
    Join<Order, User> userJoin = orderRoot.join("user");
    Join<Order, Car> carJoin = orderRoot.join("car");

    List<Predicate> predicates = new ArrayList<>();

    if (Objects.nonNull(orderSearchRequest.getEmail())) {
      predicates.add(criteriaBuilder.like(userJoin.get("email"),
          "%" + orderSearchRequest.getEmail() + "%"));
    }
    if (Objects.nonNull(orderSearchRequest.getCarVin())) {
      predicates.add(criteriaBuilder.like(carJoin.get("vin"),
          "%" + orderSearchRequest.getCarVin() + "%"));
    }

    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
  }

  private void setOrder(CriteriaBuilder criteriaBuilder, OrderSearchRequest orderSearchRequest,
      CriteriaQuery<Order> orderCriteriaQuery, Root<Order> orderRoot) {
    if (orderSearchRequest.getSortDirection().equals("asc")) {
      orderCriteriaQuery
          .orderBy(criteriaBuilder.asc(orderRoot.get(orderSearchRequest.getSortBy())));
    } else {
      orderCriteriaQuery
          .orderBy(criteriaBuilder.desc(orderRoot.get(orderSearchRequest.getSortBy())));
    }
  }
}
