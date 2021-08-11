package com.example.carrental.repository.impl;

import com.example.carrental.controller.dto.user.UserSearchRequest;
import com.example.carrental.entity.user.User;
import com.example.carrental.repository.UserCriteriaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
public class UserCriteriaRepositoryImpl implements UserCriteriaRepository {

  private final EntityManager entityManager;

  @Override
  public Page<User> findUsers(UserSearchRequest userSearchRequest) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> userCriteriaQuery = criteriaBuilder.createQuery(User.class);
    Root<User> userRoot = userCriteriaQuery.from(User.class);
    Predicate predicate = getPredicate(criteriaBuilder, userSearchRequest, userRoot);
    userCriteriaQuery.where(predicate);
    setOrder(criteriaBuilder, userSearchRequest, userCriteriaQuery, userRoot);

    TypedQuery<User> userTypedQuery = entityManager.createQuery(userCriteriaQuery);
    userTypedQuery
        .setFirstResult(userSearchRequest.getPageNumber() * userSearchRequest.getPageSize());
    userTypedQuery.setMaxResults(userSearchRequest.getPageSize());

    Pageable pageable = getPageable(userSearchRequest);

    long usersCount = getUsersCount(criteriaBuilder, predicate);

    List<User> users = userTypedQuery.getResultList();

    return new PageImpl<>(users, pageable, usersCount);
  }

  private long getUsersCount(CriteriaBuilder criteriaBuilder, Predicate predicate) {
    CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
    Root<User> userCountRoot = countQuery.from(User.class);
    countQuery.select(criteriaBuilder.count(userCountRoot)).where(predicate);
    return entityManager.createQuery(countQuery).getSingleResult();
  }

  private Pageable getPageable(UserSearchRequest userSearchRequest) {
    Sort sort = Sort.by(userSearchRequest.getSortDirection(), userSearchRequest.getSortBy());
    return PageRequest.of(userSearchRequest.getPageNumber(), userSearchRequest.getPageSize(), sort);
  }

  private Predicate getPredicate(CriteriaBuilder criteriaBuilder,
      UserSearchRequest userSearchRequest,
      Root<User> userRoot) {
    List<Predicate> predicates = new ArrayList<>();

    if (Objects.nonNull(userSearchRequest.getEmail())) {
      predicates.add(criteriaBuilder.like(userRoot.get("email"),
          "%" + userSearchRequest.getEmail() + "%"));
    }

    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
  }

  private void setOrder(CriteriaBuilder criteriaBuilder, UserSearchRequest userSearchRequest,
      CriteriaQuery<User> userCriteriaQuery, Root<User> userRoot) {
    if (userSearchRequest.getSortDirection().equals("asc")) {
      userCriteriaQuery.orderBy(criteriaBuilder.asc(userRoot.get(userSearchRequest.getSortBy())));
    } else {
      userCriteriaQuery.orderBy(criteriaBuilder.desc(userRoot.get(userSearchRequest.getSortBy())));
    }
  }
}
