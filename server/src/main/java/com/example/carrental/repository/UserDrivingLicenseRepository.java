package com.example.carrental.repository;

import com.example.carrental.entity.user.UserDrivingLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDrivingLicenseRepository extends JpaRepository<UserDrivingLicense, Long> {

}
