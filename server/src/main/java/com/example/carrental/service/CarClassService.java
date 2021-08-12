package com.example.carrental.service;

import com.example.carrental.controller.dto.car.CarClassNameResponse;
import com.example.carrental.controller.dto.car.CarClassNameWithTranslationsResponse;
import com.example.carrental.controller.dto.car.CreateCarClassRequest;
import com.example.carrental.entity.car.CarClass;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CarClassService {

  List<CarClassNameResponse> findAll(String language) throws NoContentException;

  Page<CarClassNameWithTranslationsResponse> findAllPaged(Pageable pageable);

  CarClass findById(Long id);

  void create(CreateCarClassRequest createCarClassRequest) throws EntityAlreadyExistsException;

  void update(Long id, CreateCarClassRequest createCarClassRequest);
}
