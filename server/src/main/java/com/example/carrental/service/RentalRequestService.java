package com.example.carrental.service;

import com.example.carrental.controller.dto.rentalDetails.CreateRentalRequestRequest;
import com.example.carrental.controller.dto.rentalDetails.RentalAllRequestResponse;
import com.example.carrental.controller.dto.rentalDetails.RentalRequestRejectRequest;
import com.example.carrental.controller.dto.rentalDetails.RentalRequestResponse;
import com.example.carrental.controller.dto.user.UserDrivingLicenseConfirmationDataResponse;
import com.example.carrental.controller.dto.user.UserPassportConfirmationDataResponse;
import com.example.carrental.entity.rentalDetails.RentalRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface RentalRequestService {


  Page<RentalAllRequestResponse> findAll(Pageable pageable);

  Page<RentalRequestResponse> findAllNew(Pageable pageable);

  RentalRequest findById(Long id);

  int findNewRequestsAmount();

  UserPassportConfirmationDataResponse findRequestPassportData(Long id);

  UserDrivingLicenseConfirmationDataResponse findRequestDrivingLicenseData(Long id);

  String create(CreateRentalRequestRequest createRentalRequestRequest);

  String rejectRequest(Long id, RentalRequestRejectRequest rentalRequestRejectRequest);

  String approveRequest(Long id);

  int findNewRequestsAmountPerDay();
}
