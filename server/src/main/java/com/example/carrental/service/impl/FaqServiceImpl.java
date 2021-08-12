package com.example.carrental.service.impl;

import static com.example.carrental.service.impl.CarBrandServiceImpl.NO_CONTENT;

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
import com.example.carrental.service.exceptions.NoContentException;
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
  public List<FaqResponse> findAll(String language) throws NoContentException {
    var faqs = faqRepository.findAll();
    List<FaqResponse> faqsResponse = new ArrayList<>();
    faqs.forEach(faq -> {
      faqTranslationService.setTranslation(faq, language);
      faqsResponse.add(faqMapper.faqToFaqResponse(faq));
    });
    if (faqsResponse.isEmpty()) {
      throw new NoContentException(NO_CONTENT);
    }
    return faqsResponse;
  }

  @Override
  public Page<FaqWithTranslationsResponse> findAllPaged(Pageable pageable) {
    var faqsPage = faqRepository.findAll(pageable);
    List<FaqWithTranslationsResponse> faqResponses = new ArrayList<>();
    faqsPage.forEach(faq -> faqResponses.add(faqMapper.faqToFaqWithTranslationsResponse(faq)));
    return new PageImpl<>(faqResponses, faqsPage.getPageable(), faqsPage.getTotalElements());
  }

  @Override
  public Faq findById(Long id) {
    return faqRepository.findById(id).orElseThrow(() -> {
      log.error("Faq with id {} does not exist", id);
      throw new IllegalStateException(String.format("Faq with id %d does not exists", id));
    });
  }

  @Override
  @Transactional
  public void create(CreateFaqRequest createFaqRequest) throws EntityAlreadyExistsException {
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
  }

  @Override
  @Transactional
  public void update(Long id, UpdateFaqRequest updateFaqRequest) {
    var faq = findById(id);
    faq.setQuestion(updateFaqRequest.getQuestionEn());
    faq.setAnswer(updateFaqRequest.getAnswerEn());
    faq.setChangedAt(LocalDateTime.now());

    faqRepository.save(faq);
    faqTranslationService.update(updateFaqRequest, faq.getFaqTranslations());
  }

  @Override
  @Transactional
  public void delete(Long id) {
    var faq = findById(id);
    faqRepository.delete(faq);
  }
}
