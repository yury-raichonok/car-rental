package com.example.carrental.service;

import com.example.carrental.controller.dto.car.CarAdminSearchResponse;
import com.example.carrental.controller.dto.car.CarByIdResponse;
import com.example.carrental.controller.dto.car.CarChipOfferResponse;
import com.example.carrental.controller.dto.car.CarSearchRequest;
import com.example.carrental.controller.dto.car.CarSearchResponse;
import com.example.carrental.controller.dto.car.CreateCarRequest;
import com.example.carrental.controller.dto.car.UpdateCarRequest;
import com.example.carrental.entity.car.Car;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface CarService {

  Car findById(Long id);

  Car findByIdInRental(Long id);

  CarByIdResponse findCarById(Long id, String language);

  String create(CreateCarRequest createCarRequest) throws EntityAlreadyExistsException;

  String uploadCarImage(Long id, MultipartFile carFile);

  String update(Long id, UpdateCarRequest updateCarRequest) throws EntityAlreadyExistsException;

  Page<CarSearchResponse> searchCars(CarSearchRequest carSearchRequest, String language);

  List<CarChipOfferResponse> findProfitableCarOffers(String language);

  Page<CarAdminSearchResponse> searchCarsByAdmin(CarSearchRequest carSearchRequest,
      String language);

  String updateRentalStatus(Long id);
}
