package com.example.carrental.service;

import com.example.carrental.controller.dto.rentaldetails.RentalDetailsContactInformationResponse;
import com.example.carrental.controller.dto.rentaldetails.RentalDetailsResponse;
import com.example.carrental.controller.dto.rentaldetails.RentalDetailsUpdateRequest;
import com.example.carrental.entity.rentaldetails.RentalDetails;
import org.springframework.stereotype.Service;

@Service
public interface RentalDetailsService {

  RentalDetailsContactInformationResponse getContactInformation(String language);

  RentalDetails getRentalDetails();

  RentalDetailsResponse getRentalDetailsResponse();

  void createOrUpdate(RentalDetailsUpdateRequest rentalDetailsUpdateRequest);
}
