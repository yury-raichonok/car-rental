package com.example.carrental.service;

import com.example.carrental.controller.dto.car.CarBrandResponse;
import com.example.carrental.controller.dto.car.CarBrandNameResponse;
import com.example.carrental.controller.dto.car.CarBrandSearchRequest;
import com.example.carrental.controller.dto.car.CarBrandSearchResponse;
import com.example.carrental.controller.dto.car.CarSearchResponse;
import com.example.carrental.controller.dto.car.CreateCarBrandRequest;
import com.example.carrental.controller.dto.car.UpdateCarBrandRequest;
import com.example.carrental.entity.car.CarBrand;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface CarBrandService {

  List<CarBrandResponse> findAll();

  Page<CarBrandResponse> findAllPaged(Pageable pageable);

  List<CarBrandResponse> findAllBrandsWithRentalOffers();

  CarBrand findById(Long id);

  String create(CreateCarBrandRequest createCarBrandRequest)
      throws EntityAlreadyExistsException;

  String update(Long id, CreateCarBrandRequest createCarBrandRequest)
      throws EntityAlreadyExistsException;

  String uploadBrandImage(Long id, MultipartFile brandFile);

  CarBrand findByName(String name);
}
