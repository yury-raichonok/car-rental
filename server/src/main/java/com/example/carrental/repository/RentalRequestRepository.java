package com.example.carrental.repository;

import com.example.carrental.entity.rentalDetails.RentalRequest;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {

  Page<RentalRequest> findAllByConsideredFalse(Pageable pageable);

  int countAllByConsideredFalse();

  int countAllBySentDateAfter(LocalDateTime date);
}
