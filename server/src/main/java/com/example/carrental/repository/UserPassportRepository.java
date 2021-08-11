package com.example.carrental.repository;

import com.example.carrental.entity.user.UserPassport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPassportRepository extends JpaRepository<UserPassport, Long> {

}
