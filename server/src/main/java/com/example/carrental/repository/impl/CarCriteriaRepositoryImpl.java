package com.example.carrental.repository.impl;

import com.example.carrental.controller.dto.car.CarSearchRequest;
import com.example.carrental.entity.car.Car;
import com.example.carrental.entity.car.CarBrand;
import com.example.carrental.entity.car.CarClass;
import com.example.carrental.entity.car.CarModel;
import com.example.carrental.entity.location.Location;
import com.example.carrental.repository.CarCriteriaRepository;
import java.time.LocalDate;
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
 * Car Criteria Repository class for searching Cars by parameters.
 *
 * @author Yury Raichonak
 */
@Repository
@RequiredArgsConstructor
public class CarCriteriaRepositoryImpl implements CarCriteriaRepository {

  private final EntityManager entityManager;

  /**
   * @param carSearchRequest data.
   * @return page of cars filtered by parameters.
   */
  @Override
  public Page<Car> findCars(CarSearchRequest carSearchRequest) {
    var criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Car> carCriteriaQuery = criteriaBuilder.createQuery(Car.class);
    Root<Car> carRoot = carCriteriaQuery.from(Car.class);
    var predicate = getPredicate(criteriaBuilder, carSearchRequest, carRoot);
    carCriteriaQuery.where(predicate);
    setOrder(criteriaBuilder, carSearchRequest, carCriteriaQuery, carRoot);

    TypedQuery<Car> carTypedQuery = entityManager.createQuery(carCriteriaQuery);
    carTypedQuery.setFirstResult(carSearchRequest.getPageNumber() * carSearchRequest.getPageSize());
    carTypedQuery.setMaxResults(carSearchRequest.getPageSize());

    var pageable = getPageable(carSearchRequest);

    long carsCount = getCarsCount(criteriaBuilder, predicate);

    var cars = carTypedQuery.getResultList();

    return new PageImpl<>(cars, pageable, carsCount);
  }

  /**
   * @param criteriaBuilder cars criteria.
   * @param predicate cars predicate.
   * @return amount of cars by criteria.
   */
  private long getCarsCount(CriteriaBuilder criteriaBuilder, Predicate predicate) {
    CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
    Root<Car> carCountRoot = countQuery.from(Car.class);
    Join<Car, CarModel> modelJoin = carCountRoot.join("model");
    modelJoin.join("brand");
    carCountRoot.join("carClass");
    carCountRoot.join("location");
    countQuery.select(criteriaBuilder.count(carCountRoot)).where(predicate);
    return entityManager.createQuery(countQuery).getSingleResult();
  }

  /**
   * @param carSearchRequest data.
   * @return pageable representation.
   */
  private Pageable getPageable(CarSearchRequest carSearchRequest) {
    var sort = Sort.by(carSearchRequest.getSortDirection(), carSearchRequest.getSortBy());
    return PageRequest.of(carSearchRequest.getPageNumber(), carSearchRequest.getPageSize(), sort);
  }

  /**
   * @param criteriaBuilder data.
   * @param carSearchRequest data.
   * @param carCriteriaQuery data.
   * @param carRoot data.
   */
  private void setOrder(CriteriaBuilder criteriaBuilder, CarSearchRequest carSearchRequest,
      CriteriaQuery<Car> carCriteriaQuery, Root<Car> carRoot) {
    if (carSearchRequest.getSortDirection().equals("asc")) {
      carCriteriaQuery.orderBy(criteriaBuilder.asc(carRoot.get(carSearchRequest.getSortBy())));
    } else {
      carCriteriaQuery.orderBy(criteriaBuilder.desc(carRoot.get(carSearchRequest.getSortBy())));
    }
  }

  /**
   * @param criteriaBuilder data.
   * @param carSearchRequest data.
   * @param carRoot data.
   * @return predicates by search parameters.
   */
  private Predicate getPredicate(CriteriaBuilder criteriaBuilder, CarSearchRequest carSearchRequest,
      Root<Car> carRoot) {
    Join<Car, CarModel> modelJoin = carRoot.join("model");
    Join<CarModel, CarBrand> brandJoin = modelJoin.join("brand");
    Join<Car, CarClass> carClassJoin = carRoot.join("carClass");
    Join<Car, Location> locationJoin = carRoot.join("location");

    List<Predicate> predicates = new ArrayList<>();

    if (Objects.nonNull(carSearchRequest.getBrandName())) {
      predicates.add(
          criteriaBuilder.like(brandJoin.get("name"), "%" + carSearchRequest.getBrandName() + "%"));
    }
    if (Objects.nonNull(carSearchRequest.getModelName())) {
      predicates.add(
          criteriaBuilder.like(modelJoin.get("name"), "%" + carSearchRequest.getModelName() + "%"));
    }
    if (Objects.nonNull(carSearchRequest.getLocation())) {
      predicates.add(criteriaBuilder.equal(locationJoin.get("id"), carSearchRequest.getLocation()));
    }
    if (Objects.nonNull(carSearchRequest.getCarClass())) {
      predicates.add(criteriaBuilder.equal(carClassJoin.get("id"), carSearchRequest.getCarClass()));
    }
    if (Objects.nonNull(carSearchRequest.getVin())) {
      predicates
          .add(criteriaBuilder.like(carRoot.get("vin"), "%" + carSearchRequest.getVin() + "%"));
    }

    if (Objects.nonNull(carSearchRequest.getCostFrom())) {
      predicates.add(criteriaBuilder.greaterThanOrEqualTo(carRoot.get("costPerHour"),
          Integer.parseInt(carSearchRequest.getCostFrom())));
    }
    if (Objects.nonNull(carSearchRequest.getCostTo())) {
      predicates.add(criteriaBuilder.lessThanOrEqualTo(carRoot.get("costPerHour"),
          Integer.parseInt(carSearchRequest.getCostTo())));
    }
    if (Objects.nonNull(carSearchRequest.getYearFrom())) {
      predicates.add(criteriaBuilder.greaterThanOrEqualTo(carRoot.get("dateOfIssue"),
          LocalDate.of(Integer.parseInt(carSearchRequest.getYearFrom()), 1, 1)));
    }
    if (Objects.nonNull(carSearchRequest.getYearTo())) {
      predicates.add(criteriaBuilder.lessThanOrEqualTo(carRoot.get("dateOfIssue"),
          LocalDate.of(Integer.parseInt(carSearchRequest.getYearTo()), 12, 31)));
    }
    if (Objects.nonNull(carSearchRequest.getBodyType())) {
      predicates.add(criteriaBuilder.equal(carRoot.get("bodyType"),
          carSearchRequest.getBodyType()));
    }
    if (Objects.nonNull(carSearchRequest.getEngineType())) {
      predicates.add(criteriaBuilder.equal(carRoot.get("engineType"),
          carSearchRequest.getEngineType()));
    }
    if (carSearchRequest.isAirConditioner()) {
      predicates.add(criteriaBuilder.isTrue(carRoot.get("hasConditioner")));
    }
    if (carSearchRequest.isAutoTransmission()) {
      predicates.add(criteriaBuilder.isTrue(carRoot.get("isAutomaticTransmission")));
    }
    if (carSearchRequest.isInRental()) {
      predicates.add(criteriaBuilder.isTrue(carRoot.get("inRental")));
    }

    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
  }
}
