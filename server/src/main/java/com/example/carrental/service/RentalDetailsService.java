package com.example.carrental.service;

import com.example.carrental.controller.dto.rentalDetails.RentalAdminDetailsStatisticResponse;
import com.example.carrental.controller.dto.rentalDetails.RentalDetailsAndStatisticResponse;
import com.example.carrental.controller.dto.rentalDetails.RentalDetailsContactInformationResponse;
import com.example.carrental.controller.dto.rentalDetails.RentalDetailsUpdateRequest;
import com.example.carrental.controller.dto.rentalDetails.RentalUserDetailsStatisticResponse;
import com.example.carrental.entity.rentalDetails.RentalDetails;
import org.springframework.stereotype.Service;

@Service
public interface RentalDetailsService {

  RentalDetailsAndStatisticResponse getRentalDetailsAndStatistic(String language);

  RentalDetails getRentalDetails();

  RentalDetailsContactInformationResponse getContactInformation(String language);

  String createOrUpdate(RentalDetailsUpdateRequest rentalDetailsUpdateRequest);

  RentalAdminDetailsStatisticResponse getAdminDetailsStatistic();

  RentalUserDetailsStatisticResponse getUserDetailsStatistic();
}
