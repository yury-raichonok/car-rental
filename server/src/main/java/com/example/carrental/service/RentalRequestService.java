package com.example.carrental.service;

import com.example.carrental.controller.dto.rentaldetails.CreateRentalRequestRequest;
import com.example.carrental.controller.dto.rentaldetails.RentalAllRequestResponse;
import com.example.carrental.controller.dto.rentaldetails.RentalRequestRejectRequest;
import com.example.carrental.controller.dto.rentaldetails.RentalRequestResponse;
import com.example.carrental.controller.dto.user.UserDrivingLicenseConfirmationDataResponse;
import com.example.carrental.controller.dto.user.UserPassportConfirmationDataResponse;
import com.example.carrental.entity.rentaldetails.RentalRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface RentalRequestService {

  Page<RentalAllRequestResponse> findAll(Pageable pageable);

  Page<RentalRequestResponse> findAllNew(Pageable pageable);

  RentalRequest findById(Long id);

  UserDrivingLicenseConfirmationDataResponse findRequestDrivingLicenseData(Long id);

  UserPassportConfirmationDataResponse findRequestPassportData(Long id);

  void create(CreateRentalRequestRequest createRentalRequestRequest);

  void approveRequest(Long id);

  void rejectRequest(Long id, RentalRequestRejectRequest rentalRequestRejectRequest);

  int findNewRequestsAmount();

  int findNewRequestsAmountPerDay();
}
