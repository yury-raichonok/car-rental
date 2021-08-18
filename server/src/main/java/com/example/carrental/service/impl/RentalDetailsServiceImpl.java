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

/**
 * The service for Rental Details.
 * <p>
 * This class performs the CRUD operations for Rental Details.
 * </p>
 * @author Yury Raichonak
 */
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

  /**
   * @param language selected language.
   * @return rental details contact information.
   */
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

  /**
   * @return rental details.
   */
  @Override
  public RentalDetails getRentalDetails() {
    return rentalDetailsRepository.findById(RENTAL_DETAILS_ID).orElseThrow(() -> {
      log.error(DETAILS_DOES_NOT_SET);
      throw new IllegalStateException(DETAILS_DOES_NOT_SET);
    });
  }

  /**
   * @param language selected language.
   * @return rental details response.
   */
  @Override
  public RentalDetailsResponse getRentalDetailsResponse(String language) {
    var rentalDetails = getRentalDetails();
    var location = rentalDetails.getLocation();
    locationTranslationService.setTranslation(location, language);
    return rentalDetailsMapper.rentalDetailsToRentalDetailsResponse(rentalDetails);
  }

  /**
   * @param rentalDetailsUpdateRequest data for creating or updating rental details.
   */
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
