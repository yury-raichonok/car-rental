package com.example.carrental.service;

import com.example.carrental.controller.dto.rentaldetails.RentalDetailsContactInformationResponse;
import com.example.carrental.controller.dto.rentaldetails.RentalDetailsResponse;
import com.example.carrental.controller.dto.rentaldetails.RentalDetailsUpdateRequest;
import com.example.carrental.entity.rentaldetails.RentalDetails;
import org.springframework.stereotype.Service;

/**
 * The service for Rental Details.
 * <p>
 * This interface describes actions on Rental Details.
 * </p>
 * @author Yury Raichonak
 */
@Service
public interface RentalDetailsService {

  RentalDetailsContactInformationResponse getContactInformation(String language);

  RentalDetails getRentalDetails();

  RentalDetailsResponse getRentalDetailsResponse(String language);

  void createOrUpdate(RentalDetailsUpdateRequest rentalDetailsUpdateRequest);
}
