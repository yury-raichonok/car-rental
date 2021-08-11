package com.example.carrental.service;

import com.example.carrental.controller.dto.location.CreateLocationRequest;
import com.example.carrental.entity.location.Location;
import com.example.carrental.entity.location.LocationTranslation;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface LocationTranslationService {

  void create(CreateLocationRequest createLocationRequest, Location location);

  void update(CreateLocationRequest createLocationRequest,
      List<LocationTranslation> translations);

  void setTranslation(Location location, String language);
}
