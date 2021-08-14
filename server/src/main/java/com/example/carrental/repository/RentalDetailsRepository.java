package com.example.carrental.repository;

import com.example.carrental.entity.rentaldetails.RentalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalDetailsRepository extends JpaRepository<RentalDetails, Long> {

}
