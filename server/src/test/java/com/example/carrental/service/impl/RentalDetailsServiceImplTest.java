package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.carrental.controller.dto.rentaldetails.RentalDetailsUpdateRequest;
import com.example.carrental.entity.location.Location;
import com.example.carrental.entity.message.Message;
import com.example.carrental.entity.rentaldetails.RentalDetails;
import com.example.carrental.repository.RentalDetailsRepository;
import com.example.carrental.service.LocationService;
import com.example.carrental.service.LocationTranslationService;
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
class RentalDetailsServiceImplTest {

  @Autowired
  private RentalDetailsServiceImpl rentalDetailsService;

  @MockBean
  private LocationService locationService;

  @MockBean
  private LocationTranslationService locationTranslationService;

  @MockBean
  private RentalDetailsRepository rentalDetailsRepository;

  @Test
  void givenValidRequest_whenGetContactInformation_thenSuccess() {
    var location = Location.builder().name("name").build();
    var response = RentalDetails.builder().id(1L).phone("+375111234567").location(location).build();
    when(rentalDetailsRepository.findById(1L)).thenReturn(Optional.of(response));
    doNothing().when(locationTranslationService).setTranslation(any(), any());

    var rentalDetailsContactInformationResponse = rentalDetailsService.getContactInformation(ENGLISH);

    assertThat(rentalDetailsContactInformationResponse).isNotNull();
    assertThat(rentalDetailsContactInformationResponse.getPhone()).isEqualTo(response.getPhone());
  }

  @Test
  void givenValidRequest_whenGetRentalDetails_thenSuccess() {
    var location = Location.builder().name("name").build();
    var response = RentalDetails.builder().id(1L).phone("+375111234567").location(location).build();
    when(rentalDetailsRepository.findById(1L)).thenReturn(Optional.of(response));

    var rentalDetails = rentalDetailsService.getRentalDetails();

    assertThat(rentalDetails).isNotNull();
    assertThat(rentalDetails.getPhone()).isEqualTo(response.getPhone());
    assertThat(rentalDetails.getLocation()).isEqualTo(response.getLocation());
  }

  @Test
  void givenValidRequest_whenGetRentalDetailsNotSet_thenThrowIllegalStateException() {
    when(rentalDetailsRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> rentalDetailsService.getRentalDetails());
  }

  @Test
  void givenValidRequest_whenGetRentalDetailsResponse_thenSuccess() {
    var location = Location.builder().name("name").build();
    var response = RentalDetails.builder().id(1L).phone("+375111234567").location(location).build();
    when(rentalDetailsRepository.findById(1L)).thenReturn(Optional.of(response));
    doNothing().when(locationTranslationService).setTranslation(any(), any());

    var rentalDetailsResponse = rentalDetailsService.getRentalDetailsResponse(ENGLISH);

    assertThat(rentalDetailsResponse).isNotNull();
    assertThat(rentalDetailsResponse.getPhoneNumber()).isEqualTo(response.getPhone());
    assertThat(rentalDetailsResponse.getLocation()).isEqualTo(location.getName());
  }

  @Test
  void givenValidRequest_whenCreate_thenSuccess() {
    var rentalDetailsUpdateRequest = RentalDetailsUpdateRequest.builder().location(1L).phoneNumber("+375111234567").build();
    var location = Location.builder().name("name").build();
    when(locationService.findById(rentalDetailsUpdateRequest.getLocation())).thenReturn(location);
    when(rentalDetailsRepository.findById(1L)).thenReturn(Optional.empty());
    when(rentalDetailsRepository.save(any(RentalDetails.class))).thenReturn(new RentalDetails());

    assertDoesNotThrow(() -> rentalDetailsService.createOrUpdate(rentalDetailsUpdateRequest));
  }

  @Test
  void givenValidRequest_whenUpdate_thenSuccess() {
    var rentalDetailsUpdateRequest = RentalDetailsUpdateRequest.builder().location(1L).phoneNumber("+375119999999").build();
    var location = Location.builder().name("name").build();
    var response = RentalDetails.builder().id(1L).phone("+375111234567").location(location).build();
    when(locationService.findById(rentalDetailsUpdateRequest.getLocation())).thenReturn(location);
    when(rentalDetailsRepository.findById(1L)).thenReturn(Optional.of(response));
    when(rentalDetailsRepository.save(any(RentalDetails.class))).thenReturn(new RentalDetails());

    assertDoesNotThrow(() -> rentalDetailsService.createOrUpdate(rentalDetailsUpdateRequest));
  }
}