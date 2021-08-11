package com.example.carrental.service.impl;

import com.example.carrental.controller.dto.location.CreateLocationRequest;
import com.example.carrental.controller.dto.location.LocationNameResponse;
import com.example.carrental.controller.dto.location.LocationWithTranslationsResponse;
import com.example.carrental.entity.location.Location;
import com.example.carrental.mapper.LocationMapper;
import com.example.carrental.repository.LocationRepository;
import com.example.carrental.service.LocationService;
import com.example.carrental.service.LocationTranslationService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

  private final LocationRepository locationRepository;
  private final LocationTranslationService locationTranslationService;
  private final LocationMapper locationMapper;

  @Override
  public Page<LocationWithTranslationsResponse> findAllPaged(Pageable pageable) {
    Page<Location> locationsPage = locationRepository.findAll(pageable);
    List<LocationWithTranslationsResponse> locationList = new ArrayList<>();
    locationsPage.forEach(location -> locationList
        .add(locationMapper.locationToLocationWithTranslationsResponse(location)));
    return new PageImpl<>(locationList, locationsPage.getPageable(),
        locationsPage.getTotalElements());
  }

  @Override
  public List<LocationNameResponse> findAllForSelect(String language) {
    List<Location> locations = locationRepository.findAll();
    List<LocationNameResponse> locationList = new ArrayList<>();
    locations.forEach(location -> {
      if (!"en".equals(language)) {
        locationTranslationService.setTranslation(location, language);
      }
      locationList.add(locationMapper.locationToLocationNameResponse(location));
    });
    return locationList;
  }

  @Override
  public Location findById(Long id) {
    Optional<Location> optionalLocation = locationRepository.findById(id);
    if (optionalLocation.isEmpty()) {
      log.error("Location with id {} does not exist", id);
      throw new IllegalStateException(String.format("Location with id %d does not exists", id));
    }
    return optionalLocation.get();
  }

  @Override
  @Transactional
  public String create(CreateLocationRequest createLocationRequest)
      throws EntityAlreadyExistsException {
    Optional<Location> optionalLocation = locationRepository
        .findByName(createLocationRequest.getNameEn());

    if (optionalLocation.isPresent()) {
      log.error("Location with name {} already exists", createLocationRequest.getNameEn());
      throw new EntityAlreadyExistsException(String.format("Location with name %s already exists",
          createLocationRequest.getNameEn()));
    }

    var location = Location
        .builder()
        .name(createLocationRequest.getNameEn())
        .coordinateX(createLocationRequest.getCoordinateX())
        .coordinateY(createLocationRequest.getCoordinateY())
        .zoom(createLocationRequest.getZoom())
        .createdAt(LocalDateTime.now())
        .build();

    locationRepository.save(location);
    locationTranslationService.create(createLocationRequest, location);
    return "Success";
  }

  @Override
  @Transactional
  public String update(Long id, CreateLocationRequest createLocationRequest) {
    Location location = findById(id);

    location.setName(createLocationRequest.getNameEn());
    location.setCoordinateX(createLocationRequest.getCoordinateX());
    location.setCoordinateY(createLocationRequest.getCoordinateY());
    location.setZoom(createLocationRequest.getZoom());
    location.setChangedAt(LocalDateTime.now());
    locationRepository.save(location);
    locationTranslationService.update(createLocationRequest, location.getLocationTranslations());
    return "Success";
  }
}
