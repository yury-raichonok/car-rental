package com.example.carrental.service;

import com.example.carrental.controller.dto.location.CreateLocationRequest;
import com.example.carrental.controller.dto.location.LocationNameResponse;
import com.example.carrental.controller.dto.location.LocationWithTranslationsResponse;
import com.example.carrental.entity.location.Location;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface LocationService {

  Page<LocationWithTranslationsResponse> findAllPaged(Pageable pageable);

  List<LocationNameResponse> findAllForSelect(String language);

  Location findById(Long id);

  String create(CreateLocationRequest createLocationRequest) throws EntityAlreadyExistsException;

  String update(Long id, CreateLocationRequest createLocationRequest);
}
