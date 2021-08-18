package com.example.carrental.service;

import com.example.carrental.controller.dto.location.CreateLocationRequest;
import com.example.carrental.controller.dto.location.LocationNameResponse;
import com.example.carrental.controller.dto.location.LocationWithTranslationsResponse;
import com.example.carrental.entity.location.Location;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The service for Locations.
 * <p>
 * This interface describes actions on Locations.
 * </p>
 * @author Yury Raichonak
 */
@Service
public interface LocationService {

  List<LocationNameResponse> findAllForSelect(String language) throws NoContentException;

  Page<LocationWithTranslationsResponse> findAllPaged(Pageable pageable);

  Location findById(Long id);

  void create(CreateLocationRequest createLocationRequest) throws EntityAlreadyExistsException;

  void update(Long id, CreateLocationRequest createLocationRequest);
}
