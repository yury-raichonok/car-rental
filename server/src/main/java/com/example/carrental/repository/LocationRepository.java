package com.example.carrental.repository;

import com.example.carrental.entity.location.Location;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Location Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

  /**
   * @param name of location.
   * @return optional location.
   */
  Optional<Location> findByName(String name);
}
