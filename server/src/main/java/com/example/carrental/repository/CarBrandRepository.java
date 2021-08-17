package com.example.carrental.repository;

import com.example.carrental.entity.car.CarBrand;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Car Brand Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface CarBrandRepository extends JpaRepository<CarBrand, Long> {

  /**
   * @return list of car brands with rental offers.
   */
  @Query("select distinct b from CarBrand b join b.models m join m.cars c "
      + "where c.inRental = true order by b.name")
  List<CarBrand> findAllWithRentalOffers();

  /**
   * @param name of car brand.
   * @return optional car brand.
   */
  Optional<CarBrand> findByName(String name);
}
