package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.BELORUSSIAN;
import static com.example.carrental.constants.ApplicationConstants.NOT_SPECIFIED;
import static com.example.carrental.constants.ApplicationConstants.RUSSIAN;

import com.example.carrental.controller.dto.faq.FaqResponse;
import com.example.carrental.controller.dto.faq.FaqWithTranslationsResponse;
import com.example.carrental.entity.faq.Faq;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * The interface for mapping Car Class entity to DTO.
 *
 * @author Yury Raichonak
 */
@Mapper(componentModel = "spring")
public interface FaqMapper {

  /**
   * @param faq data.
   * @return FaqResponse DTO.
   */
  @Mapping(target = "id", source = "faq.id")
  @Mapping(target = "question", source = "faq.question")
  @Mapping(target = "answer", source = "faq.answer")
  FaqResponse faqToFaqResponse(Faq faq);

  /**
   * @param faq data.
   * @return FaqWithTranslationsResponse DTO.
   */
  @Mapping(target = "id", source = "faq.id")
  @Mapping(target = "questionEn", source = "faq.question")
  @Mapping(target = "answerEn", source = "faq.answer")
  @Mapping(target = "questionRu", source = "faq", qualifiedByName = "getQuestionRu")
  @Mapping(target = "answerRu", source = "faq", qualifiedByName = "getAnswerRu")
  @Mapping(target = "questionBe", source = "faq", qualifiedByName = "getQuestionBe")
  @Mapping(target = "answerBe", source = "faq", qualifiedByName = "getAnswerBe")
  FaqWithTranslationsResponse faqToFaqWithTranslationsResponse(Faq faq);

  /**
   * @param faq data.
   * @return FAQ question in russian as String.
   */
  @Named("getQuestionRu")
  default String getQuestionRu(Faq faq) {
    if (Optional.ofNullable(faq.getFaqTranslations()).isPresent()) {
      var translationRu = faq.getFaqTranslations().stream()
          .filter(translation -> RUSSIAN.equals(translation.getLanguage())).findFirst();
      if (translationRu.isPresent()) {
        return translationRu.get().getQuestion();
      } else {
        return NOT_SPECIFIED;
      }
    } else {
      return NOT_SPECIFIED;
    }
  }

  /**
   * @param faq data.
   * @return FAQ answer in russian as String.
   */
  @Named("getAnswerRu")
  default String getAnswerRu(Faq faq) {
    if (Optional.ofNullable(faq.getFaqTranslations()).isPresent()) {
      var translationRu = faq.getFaqTranslations().stream()
          .filter(translation -> RUSSIAN.equals(translation.getLanguage())).findFirst();
      if (translationRu.isPresent()) {
        return translationRu.get().getAnswer();
      } else {
        return NOT_SPECIFIED;
      }
    } else {
      return NOT_SPECIFIED;
    }
  }

  /**
   * @param faq data.
   * @return FAQ question in belorussian as String.
   */
  @Named("getQuestionBe")
  default String getQuestionBe(Faq faq) {
    if (Optional.ofNullable(faq.getFaqTranslations()).isPresent()) {
      var translationBe = faq.getFaqTranslations().stream()
          .filter(translation -> BELORUSSIAN.equals(translation.getLanguage())).findFirst();
      if (translationBe.isPresent()) {
        return translationBe.get().getQuestion();
      } else {
        return NOT_SPECIFIED;
      }
    } else {
      return NOT_SPECIFIED;
    }

  }

  /**
   * @param faq data.
   * @return FAQ answer in belorussian as String.
   */
  @Named("getAnswerBe")
  default String getAnswerBe(Faq faq) {
    if (Optional.ofNullable(faq.getFaqTranslations()).isPresent()) {
      var translationBe = faq.getFaqTranslations().stream()
          .filter(translation -> BELORUSSIAN.equals(translation.getLanguage())).findFirst();
      if (translationBe.isPresent()) {
        return translationBe.get().getAnswer();
      } else {
        return NOT_SPECIFIED;
      }
    } else {
      return NOT_SPECIFIED;
    }
  }
}
