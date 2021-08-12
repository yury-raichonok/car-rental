package com.example.carrental.repository.impl;

import com.example.carrental.controller.dto.bill.PaymentBillSearchRequest;
import com.example.carrental.entity.bill.PaymentBill;
import com.example.carrental.entity.car.Car;
import com.example.carrental.entity.order.Order;
import com.example.carrental.entity.user.User;
import com.example.carrental.repository.PaymentBillCriteriaRepository;
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
public class PaymentBillCriteriaRepositoryImpl implements PaymentBillCriteriaRepository {

  private final EntityManager entityManager;

  @Override
  public Page<PaymentBill> findAll(PaymentBillSearchRequest paymentBillSearchRequest) {
    var criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PaymentBill> paymentBillCriteriaQuery = criteriaBuilder
        .createQuery(PaymentBill.class);
    Root<PaymentBill> paymentBillRoot = paymentBillCriteriaQuery.from(PaymentBill.class);
    var predicate = getPredicate(criteriaBuilder, paymentBillSearchRequest, paymentBillRoot);
    paymentBillCriteriaQuery.where(predicate);
    setPaymentBill(criteriaBuilder, paymentBillSearchRequest, paymentBillCriteriaQuery,
        paymentBillRoot);

    TypedQuery<PaymentBill> paymentBillTypedQuery = entityManager
        .createQuery(paymentBillCriteriaQuery);
    paymentBillTypedQuery
        .setFirstResult(
            paymentBillSearchRequest.getPageNumber() * paymentBillSearchRequest.getPageSize());
    paymentBillTypedQuery.setMaxResults(paymentBillSearchRequest.getPageSize());

    var pageable = getPageable(paymentBillSearchRequest);

    long paymentBillCount = getPaymentBillCount(criteriaBuilder, predicate);

    List<PaymentBill> paymentBills = paymentBillTypedQuery.getResultList();

    return new PageImpl<>(paymentBills, pageable, paymentBillCount);
  }

  private long getPaymentBillCount(CriteriaBuilder criteriaBuilder, Predicate predicate) {
    CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
    Root<PaymentBill> paymentBillCountRoot = countQuery.from(PaymentBill.class);
    Join<PaymentBill, Order> orderCountJoin = paymentBillCountRoot.join("order");
    orderCountJoin.join("user");
    orderCountJoin.join("car");
    countQuery.select(criteriaBuilder.count(paymentBillCountRoot)).where(predicate);
    return entityManager.createQuery(countQuery).getSingleResult();
  }

  private Pageable getPageable(PaymentBillSearchRequest paymentBillSearchRequest) {
    var sort = Sort
        .by(paymentBillSearchRequest.getSortDirection(), paymentBillSearchRequest.getSortBy());
    return PageRequest
        .of(paymentBillSearchRequest.getPageNumber(), paymentBillSearchRequest.getPageSize(), sort);
  }

  private Predicate getPredicate(CriteriaBuilder criteriaBuilder,
      PaymentBillSearchRequest paymentBillSearchRequest,
      Root<PaymentBill> paymentBillRoot) {
    Join<PaymentBill, Order> orderJoin = paymentBillRoot.join("order");
    Join<PaymentBill, User> userJoin = orderJoin.join("user");
    Join<PaymentBill, Car> carJoin = orderJoin.join("car");

    List<Predicate> predicates = new ArrayList<>();

    if (Objects.nonNull(paymentBillSearchRequest.getEmail())) {
      predicates.add(criteriaBuilder.like(userJoin.get("email"),
          "%" + paymentBillSearchRequest.getEmail() + "%"));
    }
    if (Objects.nonNull(paymentBillSearchRequest.getCarVin())) {
      predicates.add(criteriaBuilder.like(carJoin.get("vin"),
          "%" + paymentBillSearchRequest.getCarVin() + "%"));
    }

    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
  }

  private void setPaymentBill(CriteriaBuilder criteriaBuilder,
      PaymentBillSearchRequest paymentBillSearchRequest,
      CriteriaQuery<PaymentBill> paymentBillCriteriaQuery, Root<PaymentBill> paymentBillRoot) {
    if (paymentBillSearchRequest.getSortDirection().equals("asc")) {
      paymentBillCriteriaQuery
          .orderBy(criteriaBuilder.asc(paymentBillRoot.get(paymentBillSearchRequest.getSortBy())));
    } else {
      paymentBillCriteriaQuery
          .orderBy(criteriaBuilder.desc(paymentBillRoot.get(paymentBillSearchRequest.getSortBy())));
    }
  }
}
