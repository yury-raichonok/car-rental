package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.NO_CONTENT;

import com.example.carrental.controller.dto.location.CreateLocationRequest;
import com.example.carrental.controller.dto.location.LocationNameResponse;
import com.example.carrental.controller.dto.location.LocationWithTranslationsResponse;
import com.example.carrental.entity.location.Location;
import com.example.carrental.mapper.LocationMapper;
import com.example.carrental.repository.LocationRepository;
import com.example.carrental.service.LocationService;
import com.example.carrental.service.LocationTranslationService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The service for rental Locations.
 * <p>
 * This class performs the CRUD operations for rental Locations.
 * </p>
 * @author Yury Raichonak
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

  private final LocationMapper locationMapper;
  private final LocationRepository locationRepository;
  private final LocationTranslationService locationTranslationService;

  /**
   * @param language selected language.
   * @return list of locations for select.
   * @throws NoContentException if locations list is empty.
   */
  @Override
  public List<LocationNameResponse> findAllForSelect(String language) throws NoContentException {
    var locations = locationRepository.findAll();
    List<LocationNameResponse> locationsResponse = new ArrayList<>();
    locations.forEach(location -> {
      locationTranslationService.setTranslation(location, language);
      locationsResponse.add(locationMapper.locationToLocationNameResponse(location));
    });
    if (locationsResponse.isEmpty()) {
      throw new NoContentException(NO_CONTENT);
    }
    return locationsResponse;
  }

  /**
   * @param pageable data.
   * @return page of locations with translations.
   */
  @Override
  public Page<LocationWithTranslationsResponse> findAllPaged(Pageable pageable) {
    var locationsPage = locationRepository.findAll(pageable);
    List<LocationWithTranslationsResponse> locationsResponse = new ArrayList<>();
    locationsPage.forEach(location -> locationsResponse
        .add(locationMapper.locationToLocationWithTranslationsResponse(location)));
    return new PageImpl<>(locationsResponse, locationsPage.getPageable(),
        locationsPage.getTotalElements());
  }

  /**
   * @param id of location.
   * @return location.
   */
  @Override
  public Location findById(Long id) {
    return locationRepository.findById(id).orElseThrow(() -> {
      log.error("Location with id {} does not exist", id);
      throw new IllegalStateException(String.format("Location with id %d does not exists", id));
    });
  }

  /**
   * @param createLocationRequest data for creating new location.
   * @throws EntityAlreadyExistsException if location with same name already exists.
   */
  @Override
  @Transactional
  public void create(CreateLocationRequest createLocationRequest)
      throws EntityAlreadyExistsException {
    if (locationRepository.findByName(createLocationRequest.getNameEn()).isPresent()) {
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
  }

  /**
   * @param id of location.
   * @param createLocationRequest data for updating location.
   */
  @Override
  @Transactional
  public void update(Long id, CreateLocationRequest createLocationRequest) {
    var location = findById(id);
    location.setName(createLocationRequest.getNameEn());
    location.setCoordinateX(createLocationRequest.getCoordinateX());
    location.setCoordinateY(createLocationRequest.getCoordinateY());
    location.setZoom(createLocationRequest.getZoom());
    location.setChangedAt(LocalDateTime.now());

    locationRepository.save(location);
    locationTranslationService.update(createLocationRequest, location.getLocationTranslations());
  }
}
