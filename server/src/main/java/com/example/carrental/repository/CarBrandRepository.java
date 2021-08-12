package com.example.carrental.repository;

import com.example.carrental.entity.car.CarBrand;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarBrandRepository extends JpaRepository<CarBrand, Long> {

  @Query("select distinct b from CarBrand b join b.models m join m.cars c "
      + "where c.inRental = true order by b.name")
  List<CarBrand> findAllWithRentalOffers();

  Optional<CarBrand> findByName(String name);
}
