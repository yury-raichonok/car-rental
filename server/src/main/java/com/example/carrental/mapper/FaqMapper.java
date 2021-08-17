package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.BELORUSSIAN;
import static com.example.carrental.constants.ApplicationConstants.NOT_SPECIFIED;
import static com.example.carrental.constants.ApplicationConstants.RUSSIAN;

import com.example.carrental.controller.dto.faq.FaqResponse;
import com.example.carrental.controller.dto.faq.FaqWithTranslationsResponse;
import com.example.carrental.entity.faq.Faq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface FaqMapper {

  @Mapping(target = "id", source = "faq.id")
  @Mapping(target = "question", source = "faq.question")
  @Mapping(target = "answer", source = "faq.answer")
  FaqResponse faqToFaqResponse(Faq faq);

  @Mapping(target = "id", source = "faq.id")
  @Mapping(target = "questionEn", source = "faq.question")
  @Mapping(target = "answerEn", source = "faq.answer")
  @Mapping(target = "questionRu", source = "faq", qualifiedByName = "getQuestionRu")
  @Mapping(target = "answerRu", source = "faq", qualifiedByName = "getAnswerRu")
  @Mapping(target = "questionBe", source = "faq", qualifiedByName = "getQuestionBe")
  @Mapping(target = "answerBe", source = "faq", qualifiedByName = "getAnswerBe")
  FaqWithTranslationsResponse faqToFaqWithTranslationsResponse(Faq faq);

  @Named("getQuestionRu")
  default String getQuestionRu(Faq faq) {
    var translationRu = faq.getFaqTranslations().stream()
        .filter(translation -> RUSSIAN.equals(translation.getLanguage())).findFirst();
    if (translationRu.isPresent()) {
      return translationRu.get().getQuestion();
    } else {
      return NOT_SPECIFIED;
    }
  }

  @Named("getAnswerRu")
  default String getAnswerRu(Faq faq) {
    var translationRu = faq.getFaqTranslations().stream()
        .filter(translation -> RUSSIAN.equals(translation.getLanguage())).findFirst();
    if (translationRu.isPresent()) {
      return translationRu.get().getAnswer();
    } else {
      return NOT_SPECIFIED;
    }
  }

  @Named("getQuestionBe")
  default String getQuestionBe(Faq faq) {
    var translationBe = faq.getFaqTranslations().stream()
        .filter(translation -> BELORUSSIAN.equals(translation.getLanguage())).findFirst();
    if (translationBe.isPresent()) {
      return translationBe.get().getQuestion();
    } else {
      return NOT_SPECIFIED;
    }
  }

  @Named("getAnswerBe")
  default String getAnswerBe(Faq faq) {
    var translationBe = faq.getFaqTranslations().stream()
        .filter(translation -> BELORUSSIAN.equals(translation.getLanguage())).findFirst();
    if (translationBe.isPresent()) {
      return translationBe.get().getAnswer();
    } else {
      return NOT_SPECIFIED;
    }
  }
}
