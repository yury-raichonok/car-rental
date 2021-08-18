package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.BELORUSSIAN;
import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static com.example.carrental.constants.ApplicationConstants.RUSSIAN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.carrental.controller.dto.faq.CreateFaqRequest;
import com.example.carrental.controller.dto.faq.UpdateFaqRequest;
import com.example.carrental.entity.faq.Faq;
import com.example.carrental.entity.faq.FaqTranslation;
import com.example.carrental.repository.FaqTranslationRepository;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureMockMvc
class FaqTranslationServiceImplTest {

  @Autowired
  private FaqTranslationServiceImpl faqTranslationService;

  @MockBean
  private FaqTranslationRepository faqTranslationRepository;

  @Test
  void givenValidRequest_whenCreate_thenSuccess() {
    var createFaqRequest = CreateFaqRequest.builder().questionEn("question")
        .questionRu("question").questionBe("question").answerBe("answer").answerEn("answer")
        .answerRu("answer").build();
    var faq = Faq.builder().id(1L).build();
    when(faqTranslationRepository.save(any(FaqTranslation.class)))
        .thenReturn(new FaqTranslation());

    assertDoesNotThrow(() -> faqTranslationService.create(createFaqRequest, faq));
  }

  @Test
  void givenValidRequest_whenUpdate_thenSuccess() {
    var translations = Arrays
        .asList(FaqTranslation.builder().question("questionRu").answer("answerRu")
                .language(RUSSIAN).build(),
            FaqTranslation.builder().question("questionBe").answer("answerBe")
                .language(BELORUSSIAN).build());
    var updateFaqRequest = UpdateFaqRequest.builder().questionEn("question")
        .questionRu("question").questionBe("question").answerBe("answer").answerEn("answer")
        .answerRu("answer").build();
    when(faqTranslationRepository.save(any(FaqTranslation.class))).thenReturn(new FaqTranslation());

    assertDoesNotThrow(() -> faqTranslationService.update(updateFaqRequest, translations));
  }

  @Test
  void givenRequestWithEnglishLanguage_whenSetTranslation_thenSuccess() {
    var translations = Arrays
        .asList(FaqTranslation.builder().question("questionRu").answer("answerRu")
                .language(RUSSIAN).build(),
            FaqTranslation.builder().question("questionBe").answer("answerBe")
                .language(BELORUSSIAN).build());
    var faq = Faq.builder().id(1L).question("question").answer("answer")
        .faqTranslations(translations).build();

    faqTranslationService.setTranslation(faq, ENGLISH);

    assertThat(faq.getQuestion()).isEqualTo("question");
    assertThat(faq.getAnswer()).isEqualTo("answer");
  }

  @Test
  void givenRequestWithRussianLanguage_whenSetTranslation_thenSuccess() {
    var translations = Arrays
        .asList(FaqTranslation.builder().question("questionRu").answer("answerRu")
                .language(RUSSIAN).build(),
            FaqTranslation.builder().question("questionBe").answer("answerBe")
                .language(BELORUSSIAN).build());
    var faq = Faq.builder().id(1L).question("question").answer("answer")
        .faqTranslations(translations).build();

    faqTranslationService.setTranslation(faq, RUSSIAN);

    assertThat(faq.getQuestion()).isEqualTo("questionRu");
    assertThat(faq.getAnswer()).isEqualTo("answerRu");
  }

  @Test
  void givenRequestWithBelorussianLanguage_whenSetTranslation_thenSuccess() {
    var translations = Arrays
        .asList(FaqTranslation.builder().question("questionRu").answer("answerRu")
                .language(RUSSIAN).build(),
            FaqTranslation.builder().question("questionBe").answer("answerBe")
                .language(BELORUSSIAN).build());
    var faq = Faq.builder().id(1L).question("question").answer("answer")
        .faqTranslations(translations).build();

    faqTranslationService.setTranslation(faq, BELORUSSIAN);

    assertThat(faq.getQuestion()).isEqualTo("questionBe");
    assertThat(faq.getAnswer()).isEqualTo("answerBe");
  }
}