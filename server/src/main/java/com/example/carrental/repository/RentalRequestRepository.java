package com.example.carrental.repository;

import com.example.carrental.entity.rentaldetails.RentalRequest;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Rental Request Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {

  /**
   * @return amount of new rental requests.
   */
  int countAllByConsideredFalse();

  /**
   * @param date data.
   * @return amount of new rental requests per day.
   */
  int countAllBySentDateAfter(LocalDateTime date);

  /**
   * @param pageable data.
   * @return page of new rental requests.
   */
  Page<RentalRequest> findAllByConsideredFalse(Pageable pageable);
}
