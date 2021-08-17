package com.example.carrental.service.impl;

import com.example.carrental.config.ApplicationPropertiesConfig;
import com.example.carrental.controller.dto.rentaldetails.RentalDetailsContactInformationResponse;
import com.example.carrental.controller.dto.rentaldetails.RentalDetailsResponse;
import com.example.carrental.controller.dto.rentaldetails.RentalDetailsUpdateRequest;
import com.example.carrental.entity.rentaldetails.RentalDetails;
import com.example.carrental.mapper.RentalDetailsMapper;
import com.example.carrental.repository.RentalDetailsRepository;
import com.example.carrental.service.LocationService;
import com.example.carrental.service.LocationTranslationService;
import com.example.carrental.service.RentalDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RentalDetailsServiceImpl implements RentalDetailsService {

  private static final long RENTAL_DETAILS_ID = 1L;
  private static final String DETAILS_DOES_NOT_SET = "Rental details does not set";

  private final ApplicationPropertiesConfig applicationPropertiesConfig;
  private final LocationService locationService;
  private final LocationTranslationService locationTranslationService;
  private final RentalDetailsRepository rentalDetailsRepository;
  private final RentalDetailsMapper rentalDetailsMapper;

  @Override
  public RentalDetailsContactInformationResponse getContactInformation(String language) {
    var rentalDetails = getRentalDetails();
    var location = rentalDetails.getLocation();
    locationTranslationService.setTranslation(location, language);

    return RentalDetailsContactInformationResponse
        .builder()
        .phone(rentalDetails.getPhone())
        .email(applicationPropertiesConfig.getRentalEmail())
        .locationName(location.getName())
        .locationCoordinateX(rentalDetails.getLocation().getCoordinateX())
        .locationCoordinateY(rentalDetails.getLocation().getCoordinateY())
        .zoom(rentalDetails.getLocation().getZoom())
        .build();
  }

  @Override
  public RentalDetails getRentalDetails() {
    return rentalDetailsRepository.findById(RENTAL_DETAILS_ID).orElseThrow(() -> {
      log.error(DETAILS_DOES_NOT_SET);
      throw new IllegalStateException(DETAILS_DOES_NOT_SET);
    });
  }

  @Override
  public RentalDetailsResponse getRentalDetailsResponse(String language) {
    var rentalDetails = rentalDetailsRepository.findById(RENTAL_DETAILS_ID)
        .orElseThrow(() -> {
          log.error(DETAILS_DOES_NOT_SET);
          throw new IllegalStateException(DETAILS_DOES_NOT_SET);
        });
    var location = rentalDetails.getLocation();
    locationTranslationService.setTranslation(location, language);
    return rentalDetailsMapper.rentalDetailsToRentalDetailsResponse(rentalDetails);
  }

  @Override
  public void createOrUpdate(RentalDetailsUpdateRequest rentalDetailsUpdateRequest) {
    var location = locationService.findById(rentalDetailsUpdateRequest.getLocation());
    rentalDetailsRepository.findById(RENTAL_DETAILS_ID).ifPresentOrElse(
        details -> {
          details.setPhone(rentalDetailsUpdateRequest.getPhoneNumber());
          details.setLocation(location);
          rentalDetailsRepository.save(details);
        },
        () -> rentalDetailsRepository.save(RentalDetails
            .builder()
            .phone(rentalDetailsUpdateRequest.getPhoneNumber())
            .location(location)
            .build())
    );
  }
}
