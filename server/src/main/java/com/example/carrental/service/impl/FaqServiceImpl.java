package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.NO_CONTENT;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The service for FAQs.
 * <p>
 * This class performs the CRUD operations for FAQs.
 * </p>
 * @author Yury Raichonak
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

  private final FaqMapper faqMapper;
  private final FaqRepository faqRepository;
  private final FaqTranslationService faqTranslationService;

  /**
   * @param language selected language.
   * @return faq response list
   * @throws NoContentException if faq response list is empty.
   */
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

  /**
   * @param pageable data.
   * @return faq with translations list.
   */
  @Override
  public Page<FaqWithTranslationsResponse> findAllPaged(Pageable pageable) {
    var faqsPage = faqRepository.findAll(pageable);
    List<FaqWithTranslationsResponse> faqResponses = new ArrayList<>();
    faqsPage.forEach(faq -> faqResponses.add(faqMapper.faqToFaqWithTranslationsResponse(faq)));
    return new PageImpl<>(faqResponses, faqsPage.getPageable(), faqsPage.getTotalElements());
  }

  /**
   * @param id of faq.
   * @return faq.
   */
  @Override
  public Faq findById(Long id) {
    return faqRepository.findById(id).orElseThrow(() -> {
      log.error("Faq with id {} does not exist", id);
      throw new IllegalStateException(String.format("Faq with id %d does not exists", id));
    });
  }

  /**
   * @param createFaqRequest request data for creating new faq.
   * @throws EntityAlreadyExistsException if faq with same question already exists.
   */
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

  /**
   * @param id of updating faq.
   * @param updateFaqRequest request data for updating faq.
   */
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

  /**
   * @param id of faq.
   */
  @Override
  @Transactional
  public void delete(Long id) {
    var faq = findById(id);
    faqRepository.delete(faq);
  }
}
