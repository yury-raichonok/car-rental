package com.example.carrental.service;

import com.example.carrental.controller.dto.car.CarBrandResponse;
import com.example.carrental.controller.dto.car.CreateCarBrandRequest;
import com.example.carrental.entity.car.CarBrand;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * The service for Car Brands.
 * <p>
 * This interface describes actions on Car Brands.
 * </p>
 * @author Yury Raichonak
 */
@Service
public interface CarBrandService {

  List<CarBrandResponse> findAll() throws NoContentException;

  List<CarBrandResponse> findAllBrandsWithRentalOffers() throws NoContentException;

  Page<CarBrandResponse> findAllPaged(Pageable pageable);

  CarBrand findById(Long id);

  CarBrand findByName(String name);

  void create(CreateCarBrandRequest createCarBrandRequest)
      throws EntityAlreadyExistsException;

  void uploadBrandImage(Long id, MultipartFile brandFile);

  void update(Long id, CreateCarBrandRequest createCarBrandRequest)
      throws EntityAlreadyExistsException;
}
