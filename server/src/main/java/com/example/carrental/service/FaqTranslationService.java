package com.example.carrental.service;

import com.example.carrental.controller.dto.faq.CreateFaqRequest;
import com.example.carrental.controller.dto.faq.UpdateFaqRequest;
import com.example.carrental.entity.faq.Faq;
import com.example.carrental.entity.faq.FaqTranslation;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface FaqTranslationService {

  void create(CreateFaqRequest createFaqRequest, Faq faq);

  void update(UpdateFaqRequest updateFaqRequest, List<FaqTranslation> translations);

  void setTranslation(Faq faq, String language);
}
