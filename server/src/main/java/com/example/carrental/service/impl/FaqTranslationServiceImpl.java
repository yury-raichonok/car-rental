package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.BELORUSSIAN;
import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static com.example.carrental.constants.ApplicationConstants.RUSSIAN;

import com.example.carrental.controller.dto.faq.CreateFaqRequest;
import com.example.carrental.controller.dto.faq.UpdateFaqRequest;
import com.example.carrental.entity.faq.Faq;
import com.example.carrental.entity.faq.FaqTranslation;
import com.example.carrental.repository.FaqTranslationRepository;
import com.example.carrental.service.FaqTranslationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FaqTranslationServiceImpl implements FaqTranslationService {

  private final FaqTranslationRepository faqTranslationRepository;

  @Override
  @Transactional
  public void create(CreateFaqRequest createFaqRequest, Faq faq) {
    faqTranslationRepository.save(FaqTranslation
        .builder()
        .question(createFaqRequest.getQuestionBe())
        .answer(createFaqRequest.getAnswerBe())
        .faq(faq)
        .language(BELORUSSIAN)
        .build());

    faqTranslationRepository.save(FaqTranslation
        .builder()
        .question(createFaqRequest.getQuestionRu())
        .answer(createFaqRequest.getAnswerRu())
        .faq(faq)
        .language(RUSSIAN)
        .build());
  }

  @Override
  @Transactional
  public void update(UpdateFaqRequest updateFaqRequest, List<FaqTranslation> translations) {
    var faqTranslationRu = translations.stream()
        .filter(translation -> RUSSIAN.equals(translation.getLanguage())).findFirst();
    faqTranslationRu.ifPresent(faqTranslation -> {
      faqTranslation.setQuestion(updateFaqRequest.getQuestionRu());
      faqTranslation.setAnswer(updateFaqRequest.getAnswerRu());
      faqTranslationRepository.save(faqTranslation);
    });

    var faqTranslationBe = translations.stream()
        .filter(translation -> BELORUSSIAN.equals(translation.getLanguage())).findFirst();
    faqTranslationBe.ifPresent(faqTranslation -> {
      faqTranslation.setQuestion(updateFaqRequest.getQuestionBe());
      faqTranslation.setAnswer(updateFaqRequest.getAnswerBe());
      faqTranslationRepository.save(faqTranslation);
    });
  }

  @Override
  public void setTranslation(Faq faq, String language) {
    if (!ENGLISH.equals(language)) {
      faq.getFaqTranslations()
          .stream()
          .filter(translation -> language.equals(translation.getLanguage()))
          .findFirst()
          .ifPresent(faqTranslation -> {
            faq.setQuestion(faqTranslation.getQuestion());
            faq.setAnswer(faqTranslation.getAnswer());
          });
    }
  }
}
