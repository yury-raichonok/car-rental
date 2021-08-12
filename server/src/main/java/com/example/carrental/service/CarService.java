package com.example.carrental.service;

import com.example.carrental.controller.dto.car.CarAdminSearchResponse;
import com.example.carrental.controller.dto.car.CarByIdResponse;
import com.example.carrental.controller.dto.car.CarProfitableOfferResponse;
import com.example.carrental.controller.dto.car.CarSearchRequest;
import com.example.carrental.controller.dto.car.CarSearchResponse;
import com.example.carrental.controller.dto.car.CreateCarRequest;
import com.example.carrental.controller.dto.car.UpdateCarRequest;
import com.example.carrental.entity.car.Car;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface CarService {

  List<CarProfitableOfferResponse> findAllProfitableOffers(String language)
      throws NoContentException;

  Car findById(Long id);

  Car findByIdInRental(Long id);

  CarByIdResponse findCarById(Long id, String language);

  void create(CreateCarRequest createCarRequest) throws EntityAlreadyExistsException;

  Page<CarSearchResponse> searchCars(CarSearchRequest carSearchRequest, String language);

  Page<CarAdminSearchResponse> searchCarsByAdmin(CarSearchRequest carSearchRequest,
      String language);

  void uploadCarImage(Long id, MultipartFile carFile);

  void update(Long id, UpdateCarRequest updateCarRequest) throws EntityAlreadyExistsException;

  void updateRentalStatus(Long id);
}
