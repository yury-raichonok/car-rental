package com.example.carrental.repository.impl;

import com.example.carrental.controller.dto.bill.RepairBillSearchRequest;
import com.example.carrental.entity.bill.RepairBill;
import com.example.carrental.entity.car.Car;
import com.example.carrental.entity.order.Order;
import com.example.carrental.entity.user.User;
import com.example.carrental.repository.RepairBillCriteriaRepository;
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

/**
 * Repair Bill Criteria Repository class for searching Repair Bills by parameters.
 *
 * @author Yury Raichonak
 */
@Repository
@RequiredArgsConstructor
public class RepairBillCriteriaRepositoryImpl implements RepairBillCriteriaRepository {

  private final EntityManager entityManager;

  /**
   * @param repairBillSearchRequest data.
   * @return page of repair bills filtered by parameters.
   */
  @Override
  public Page<RepairBill> findAll(RepairBillSearchRequest repairBillSearchRequest) {
    var criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<RepairBill> repairBillCriteriaQuery = criteriaBuilder
        .createQuery(RepairBill.class);
    Root<RepairBill> repairBillRoot = repairBillCriteriaQuery.from(RepairBill.class);
    var predicate = getPredicate(criteriaBuilder, repairBillSearchRequest, repairBillRoot);
    repairBillCriteriaQuery.where(predicate);
    setRepairBill(criteriaBuilder, repairBillSearchRequest, repairBillCriteriaQuery,
        repairBillRoot);

    TypedQuery<RepairBill> repairBillTypedQuery = entityManager
        .createQuery(repairBillCriteriaQuery);
    repairBillTypedQuery
        .setFirstResult(
            repairBillSearchRequest.getPageNumber() * repairBillSearchRequest.getPageSize());
    repairBillTypedQuery.setMaxResults(repairBillSearchRequest.getPageSize());

    var pageable = getPageable(repairBillSearchRequest);

    long repairBillCount = getRepairBillCount(criteriaBuilder, predicate);

    List<RepairBill> paymentBills = repairBillTypedQuery.getResultList();

    return new PageImpl<>(paymentBills, pageable, repairBillCount);
  }

  /**
   * @param criteriaBuilder repair bills criteria.
   * @param predicate repair bills predicate.
   * @return amount of repair bills by criteria.
   */
  private long getRepairBillCount(CriteriaBuilder criteriaBuilder, Predicate predicate) {
    CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
    Root<RepairBill> paymentBillCountRoot = countQuery.from(RepairBill.class);
    Join<RepairBill, Order> orderCountJoin = paymentBillCountRoot.join("order");
    orderCountJoin.join("user");
    orderCountJoin.join("car");
    countQuery.select(criteriaBuilder.count(paymentBillCountRoot)).where(predicate);
    return entityManager.createQuery(countQuery).getSingleResult();
  }

  /**
   * @param repairBillSearchRequest data.
   * @return pageable representation.
   */
  private Pageable getPageable(RepairBillSearchRequest repairBillSearchRequest) {
    var sort = Sort
        .by(repairBillSearchRequest.getSortDirection(), repairBillSearchRequest.getSortBy());
    return PageRequest
        .of(repairBillSearchRequest.getPageNumber(), repairBillSearchRequest.getPageSize(), sort);
  }

  /**
   * @param criteriaBuilder data.
   * @param repairBillSearchRequest data.
   * @param repairBillRoot data.
   * @return predicates by search parameters.
   */
  private Predicate getPredicate(CriteriaBuilder criteriaBuilder,
      RepairBillSearchRequest repairBillSearchRequest,
      Root<RepairBill> repairBillRoot) {
    Join<RepairBill, Order> orderJoin = repairBillRoot.join("order");
    Join<RepairBill, User> userJoin = orderJoin.join("user");
    Join<RepairBill, Car> carJoin = orderJoin.join("car");

    List<Predicate> predicates = new ArrayList<>();

    if (Objects.nonNull(repairBillSearchRequest.getEmail())) {
      predicates.add(criteriaBuilder.like(userJoin.get("email"),
          "%" + repairBillSearchRequest.getEmail() + "%"));
    }
    if (Objects.nonNull(repairBillSearchRequest.getCarVin())) {
      predicates.add(criteriaBuilder.like(carJoin.get("vin"),
          "%" + repairBillSearchRequest.getCarVin() + "%"));
    }

    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
  }

  /**
   * @param criteriaBuilder data.
   * @param repairBillSearchRequest data.
   * @param repairBillCriteriaQuery data.
   * @param repairBillRoot data.
   */
  private void setRepairBill(CriteriaBuilder criteriaBuilder,
      RepairBillSearchRequest repairBillSearchRequest,
      CriteriaQuery<RepairBill> repairBillCriteriaQuery, Root<RepairBill> repairBillRoot) {
    if (repairBillSearchRequest.getSortDirection().equals("asc")) {
      repairBillCriteriaQuery
          .orderBy(criteriaBuilder.asc(repairBillRoot.get(repairBillSearchRequest.getSortBy())));
    } else {
      repairBillCriteriaQuery
          .orderBy(criteriaBuilder.desc(repairBillRoot.get(repairBillSearchRequest.getSortBy())));
    }
  }
}
