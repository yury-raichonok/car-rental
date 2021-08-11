package com.example.carrental.repository;

import com.example.carrental.controller.dto.user.UserSearchRequest;
import com.example.carrental.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCriteriaRepository {

  Page<User> findUsers(UserSearchRequest userSearchRequest);
}
