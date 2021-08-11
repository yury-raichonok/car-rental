package com.example.carrental.service.impl;

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
        .language("be")
        .build());

    faqTranslationRepository.save(FaqTranslation
        .builder()
        .question(createFaqRequest.getQuestionRu())
        .answer(createFaqRequest.getAnswerRu())
        .faq(faq)
        .language("ru")
        .build());
  }

  @Override
  @Transactional
  public void update(UpdateFaqRequest updateFaqRequest, List<FaqTranslation> translations) {
    var faqTranslationRu = translations.stream()
        .filter(translation -> "ru".equals(translation.getLanguage())).findFirst();
    faqTranslationRu.ifPresent(faqTranslation -> {
      faqTranslation.setQuestion(updateFaqRequest.getQuestionRu());
      faqTranslation.setAnswer(updateFaqRequest.getAnswerRu());
      faqTranslationRepository.save(faqTranslation);
    });

    var faqTranslationBe = translations.stream()
        .filter(translation -> "be".equals(translation.getLanguage())).findFirst();
    faqTranslationBe.ifPresent(faqTranslation -> {
      faqTranslation.setQuestion(updateFaqRequest.getQuestionBe());
      faqTranslation.setAnswer(updateFaqRequest.getAnswerBe());
      faqTranslationRepository.save(faqTranslation);
    });
  }

  @Override
  public void setTranslation(Faq faq, String language) {
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
