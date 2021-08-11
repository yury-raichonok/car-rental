package com.example.carrental.repository;

import com.example.carrental.entity.user.UserPhone;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPhoneRepository extends JpaRepository<UserPhone, Long> {

  Optional<UserPhone> findByPhoneAndActiveTrue(String phone);
}
