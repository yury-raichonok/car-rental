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

  String create(CreateCarClassRequest createCarClassRequest) throws EntityAlreadyExistsException;

  List<CarClassNameResponse> findAll(String language) throws NoContentException;

  Page<CarClassNameWithTranslationsResponse> findAllPaged(Pageable pageable);

  CarClass findById(Long id);

  String update(Long id, CreateCarClassRequest createCarClassRequest);
}
