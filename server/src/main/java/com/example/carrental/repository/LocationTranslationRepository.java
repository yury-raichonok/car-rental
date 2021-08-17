package com.example.carrental.repository;

import com.example.carrental.entity.location.LocationTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Location Translation Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface LocationTranslationRepository extends JpaRepository<LocationTranslation, Long> {

}
