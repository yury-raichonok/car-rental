package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.carrental.controller.dto.location.CreateLocationRequest;
import com.example.carrental.entity.location.Location;
import com.example.carrental.repository.LocationRepository;
import com.example.carrental.service.LocationTranslationService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@SpringBootTest
@AutoConfigureMockMvc
class LocationServiceImplTest {

  @Autowired
  private LocationServiceImpl locationService;

  @MockBean
  private LocationRepository locationRepository;

  @MockBean
  private LocationTranslationService locationTranslationService;

  @Test
  void givenValidRequest_whenFindAllForSelect_thenSuccess() {
    var locations = Arrays.asList(Location.builder().id(1L).name("name").build(),
        Location.builder().id(2L).name("name1").build());
    when(locationRepository.findAll()).thenReturn(locations);
    doNothing().when(locationTranslationService).setTranslation(any(), any());

    var locationNameResponse = locationService.findAllForSelect(ENGLISH);

    assertThat(locationNameResponse).isNotNull();
    assertThat(locationNameResponse.size()).isEqualTo(locations.size());
  }

  @Test
  void givenValidRequest_whenFindAllForSelectNoContent_thenThrowNoContentException() {
    when(locationRepository.findAll()).thenReturn(new ArrayList<>());

    assertThrows(NoContentException.class, () -> locationService.findAllForSelect(ENGLISH));
  }

  @Test
  void givenValidRequest_whenFindAllPaged_thenSuccess() {
    var pageable = Pageable.ofSize(10).withPage(0);
    var locations = new PageImpl<>(Arrays.asList(Location.builder().id(1L).name("name").build(),
        Location.builder().id(2L).name("name1").build()));
    when(locationRepository.findAll(pageable)).thenReturn(locations);

    var locationWithTranslationsResponse = locationService.findAllPaged(pageable);

    assertThat(locationWithTranslationsResponse).isNotNull();
    assertThat(locationWithTranslationsResponse.getTotalElements())
        .isEqualTo(locations.getTotalElements());
  }

  @Test
  void givenValidRequest_whenFindById_thenSuccess() {
    var optionalLocation = Optional.of(Location.builder().id(1L).name("name").build());
    when(locationRepository.findById(1L)).thenReturn(optionalLocation);

    var location = locationService.findById(1L);

    assertThat(location).isNotNull();
    assertThat(location.getId()).isEqualTo(optionalLocation.get().getId());
    assertThat(location.getName()).isEqualTo(optionalLocation.get().getName());
  }

  @Test
  void givenRequestWithNotExistingId_whenFindById_thenThrowIllegalStateException() {
    when(locationRepository.findById(1L)).thenThrow(new IllegalStateException());

    assertThrows(IllegalStateException.class, () -> locationService.findById(1L));
  }

  @Test
  void givenValidRequest_whenCreate_thenSuccess() {
    var createLocationRequest = CreateLocationRequest.builder().nameBe("name").nameEn("name")
        .nameRu("name").coordinateX(1).coordinateY(1).zoom(1).build();
    when(locationRepository.findByName(createLocationRequest.getNameEn()))
        .thenReturn(Optional.empty());
    when(locationRepository.save(any(Location.class))).thenReturn(new Location());
    doNothing().when(locationTranslationService).create(any(), any());

    assertDoesNotThrow(() -> locationService.create(createLocationRequest));
  }

  @Test
  void givenRequestWithExistingName_whenCreate_thenThrowEntityAlreadyExistsException() {
    var createLocationRequest = CreateLocationRequest.builder().nameBe("name").nameEn("name")
        .nameRu("name").coordinateX(1).coordinateY(1).zoom(1).build();
    when(locationRepository.findByName(createLocationRequest.getNameEn()))
        .thenReturn(Optional.of(new Location()));

    assertThrows(EntityAlreadyExistsException.class,
        () -> locationService.create(createLocationRequest));
  }

  @Test
  void givenValidRequest_whenUpdate_thenSuccess() {
    var createLocationRequest = CreateLocationRequest.builder().nameBe("name").nameEn("name")
        .nameRu("name").coordinateX(1).coordinateY(1).zoom(1).build();
    var optionalLocation = Optional.of(Location.builder().id(1L).name("oldName").build());
    when(locationRepository.findById(1L)).thenReturn(optionalLocation);
    when(locationRepository.save(any(Location.class))).thenReturn(new Location());
    doNothing().when(locationTranslationService).update(any(), any());

    locationService.update(1L, createLocationRequest);

    assertThat(optionalLocation.get().getName()).isEqualTo(createLocationRequest.getNameEn());
  }
}