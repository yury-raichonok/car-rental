package com.example.carrental.repository;

import com.example.carrental.controller.dto.user.UserSearchRequest;
import com.example.carrental.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

/**
 * User Criteria Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface UserCriteriaRepository {

  /**
   * @param userSearchRequest search parameters.
   * @return page of users filtered by parameters.
   */
  Page<User> findUsers(UserSearchRequest userSearchRequest);
}
