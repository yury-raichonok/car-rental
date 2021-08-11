package com.example.carrental.repository;

import com.example.carrental.entity.rentalDetails.RentalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalDetailsRepository extends JpaRepository<RentalDetails, Long> {

}
