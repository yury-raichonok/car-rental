package com.example.carrental.repository;

import com.example.carrental.entity.user.UserDrivingLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * User Driving License Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface UserDrivingLicenseRepository extends JpaRepository<UserDrivingLicense, Long> {

}
