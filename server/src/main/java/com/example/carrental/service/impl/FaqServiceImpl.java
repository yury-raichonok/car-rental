package com.example.carrental.service.impl;

import com.example.carrental.controller.dto.faq.CreateFaqRequest;
import com.example.carrental.controller.dto.faq.FaqResponse;
import com.example.carrental.controller.dto.faq.FaqWithTranslationsResponse;
import com.example.carrental.controller.dto.faq.UpdateFaqRequest;
import com.example.carrental.entity.faq.Faq;
import com.example.carrental.mapper.FaqMapper;
import com.example.carrental.repository.FaqRepository;
import com.example.carrental.service.FaqService;
import com.example.carrental.service.FaqTranslationService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

  private final FaqRepository faqRepository;
  private final FaqTranslationService faqTranslationService;
  private final FaqMapper faqMapper;

  @Override
  public List<FaqResponse> findAll(String language) {
    var faqs = faqRepository.findAll();
    List<FaqResponse> faqResponses = new ArrayList<>();
    faqs.forEach(faq -> {
      if (!"en".equals(language)) {
        faqTranslationService.setTranslations(faq, language);
      }
      faqResponses.add(faqMapper.faqToFaqResponse(faq));
    });
    return faqResponses;
  }

  @Override
  public Page<FaqWithTranslationsResponse> findAllPaged(Pageable pageable) {
    Page<Faq> faqsPage = faqRepository.findAll(pageable);
    List<FaqWithTranslationsResponse> faqResponses = new ArrayList<>();
    faqsPage.forEach(faq -> faqResponses.add(faqMapper.faqToFaqWithTranslationsResponse(faq)));
    return new PageImpl<>(faqResponses, faqsPage.getPageable(), faqsPage.getTotalElements());
  }

  @Override
  public Faq findById(Long id) {
    Optional<Faq> optionalFaq = faqRepository.findById(id);
    if (optionalFaq.isEmpty()) {
      log.error("Faq with id {} does not exist", id);
      throw new IllegalStateException(String.format("Faq with id %d does not exists", id));
    }
    return optionalFaq.get();
  }

  @Override
  @Transactional
  public String create(CreateFaqRequest createFaqRequest) throws EntityAlreadyExistsException {
    if (faqRepository.findByQuestion(createFaqRequest.getQuestionEn()).isPresent()) {
      log.error("Faq with question {} already exists", createFaqRequest.getQuestionEn());
      throw new EntityAlreadyExistsException(String.format("Faq with question %s already exists",
          createFaqRequest.getQuestionEn()));
    }

    var faq = Faq
        .builder()
        .question(createFaqRequest.getQuestionEn())
        .answer(createFaqRequest.getAnswerEn())
        .createdAt(LocalDateTime.now())
        .build();

    faqRepository.save(faq);
    faqTranslationService.create(createFaqRequest, faq);
    return "Success";
  }

  @Override
  @Transactional
  public String update(Long id, UpdateFaqRequest updateFaqRequest) {
    Faq faq = findById(id);

    faq.setQuestion(updateFaqRequest.getQuestionEn());
    faq.setAnswer(updateFaqRequest.getAnswerEn());
    faq.setChangedAt(LocalDateTime.now());
    faqRepository.save(faq);

    faqTranslationService.update(updateFaqRequest, faq.getFaqTranslations());
    return "Success";
  }

  @Override
  @Transactional
  public String delete(Long id) {
    Faq faq = findById(id);

    faqRepository.delete(faq);
    return "Success";
  }
}
