package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.BELORUSSIAN;
import static com.example.carrental.constants.ApplicationConstants.RUSSIAN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.carrental.entity.faq.Faq;
import com.example.carrental.entity.faq.FaqTranslation;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FaqMapperTest {

  private Faq faq;
  private FaqTranslation faqTranslationRu;
  private FaqTranslation faqTranslationBe;

  @Autowired
  private FaqMapper faqMapper;

  @BeforeEach
  public void setup() {
    faqTranslationRu = FaqTranslation.builder().id(1L).question("questionRu").answer("answerRu")
        .language(RUSSIAN).build();
    faqTranslationBe = FaqTranslation.builder().id(2L).question("questionBe").answer("answerBe")
        .language(BELORUSSIAN).build();
    faq = Faq.builder().id(1L).question("question").answer("answer")
        .faqTranslations(Arrays.asList(faqTranslationRu, faqTranslationBe)).build();
  }

  @Test
  void faqToFaqResponse() {
    var faqResponse = faqMapper.faqToFaqResponse(faq);

    assertThat(faqResponse).isNotNull();
    assertThat(faqResponse.getId()).isEqualTo(faq.getId());
    assertThat(faqResponse.getQuestion()).isEqualTo(faq.getQuestion());
    assertThat(faqResponse.getAnswer()).isEqualTo(faq.getAnswer());
  }

  @Test
  void faqToFaqWithTranslationsResponse() {
    var faqWithTranslationsResponse = faqMapper.faqToFaqWithTranslationsResponse(faq);

    assertThat(faqWithTranslationsResponse).isNotNull();
    assertThat(faqWithTranslationsResponse.getQuestionRu())
        .isEqualTo(faqTranslationRu.getQuestion());
    assertThat(faqWithTranslationsResponse.getAnswerRu()).isEqualTo(faqTranslationRu.getAnswer());
    assertThat(faqWithTranslationsResponse.getQuestionBe())
        .isEqualTo(faqTranslationBe.getQuestion());
    assertThat(faqWithTranslationsResponse.getAnswerBe()).isEqualTo(faqTranslationBe.getAnswer());
    assertThat(faqWithTranslationsResponse.getQuestionEn()).isEqualTo(faq.getQuestion());
    assertThat(faqWithTranslationsResponse.getAnswerEn()).isEqualTo(faq.getAnswer());
  }

  @Test
  void getQuestionRu() {
    var questionRu = faqMapper.getQuestionRu(faq);

    assertThat(questionRu).isNotNull();
    assertThat(questionRu).isEqualTo(faqTranslationRu.getQuestion());
  }

  @Test
  void getAnswerRu() {
    var answerRu = faqMapper.getAnswerRu(faq);

    assertThat(answerRu).isNotNull();
    assertThat(answerRu).isEqualTo(faqTranslationRu.getAnswer());
  }

  @Test
  void getQuestionBe() {
    var questionBe = faqMapper.getQuestionBe(faq);

    assertThat(questionBe).isNotNull();
    assertThat(questionBe).isEqualTo(faqTranslationBe.getQuestion());
  }

  @Test
  void getAnswerBe() {
    var answerBe = faqMapper.getAnswerBe(faq);

    assertThat(answerBe).isNotNull();
    assertThat(answerBe).isEqualTo(faqTranslationBe.getAnswer());
  }
}