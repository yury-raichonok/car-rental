package com.example.carrental.service;

import com.example.carrental.controller.dto.faq.CreateFaqRequest;
import com.example.carrental.controller.dto.faq.FaqResponse;
import com.example.carrental.controller.dto.faq.FaqWithTranslationsResponse;
import com.example.carrental.controller.dto.faq.UpdateFaqRequest;
import com.example.carrental.entity.faq.Faq;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface FaqService {

  List<FaqResponse> findAll(String language);

  Page<FaqWithTranslationsResponse> findAllPaged(Pageable pageable);

  Faq findById(Long id);

  String create(CreateFaqRequest createFaqRequest) throws EntityAlreadyExistsException;

  String update(Long id, UpdateFaqRequest updateFaqRequest);

  String delete(Long id);
}
