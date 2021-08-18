package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.carrental.controller.dto.faq.CreateFaqRequest;
import com.example.carrental.controller.dto.faq.UpdateFaqRequest;
import com.example.carrental.entity.faq.Faq;
import com.example.carrental.repository.FaqRepository;
import com.example.carrental.service.FaqTranslationService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@SpringBootTest
@AutoConfigureMockMvc
class FaqServiceImplTest {

  @Autowired
  private FaqServiceImpl faqService;

  @MockBean
  private FaqRepository faqRepository;

  @MockBean
  private FaqTranslationService faqTranslationService;

  @Test
  void givenValidRequest_whenFindAl_thenSuccess() {
    var faqs = Arrays.asList(Faq.builder().id(1L).question("question").answer("answer").build(),
        Faq.builder().id(2L).question("question1").answer("answer1").build());
    when(faqRepository.findAll()).thenReturn(faqs);
    doNothing().when(faqTranslationService).setTranslation(any(), any());

    var faqResponse = faqService.findAll(ENGLISH);

    assertThat(faqResponse).isNotNull();
    assertThat(faqResponse.size()).isEqualTo(faqs.size());
  }

  @Test
  void givenValidRequest_whenFindAlNoContent_thenThrowNoContentException() {
    when(faqRepository.findAll()).thenReturn(new ArrayList<>());

    assertThrows(NoContentException.class, () -> faqService.findAll(ENGLISH));
  }

  @Test
  void givenValidRequest_whenFindAllPaged_thenSuccess() {
    var pageable = Pageable.ofSize(10).withPage(0);
    var faqs = new PageImpl<>(
        Arrays.asList(Faq.builder().id(1L).question("question").answer("answer").build(),
            Faq.builder().id(2L).question("question1").answer("answer1").build()));
    when(faqRepository.findAll(pageable)).thenReturn(faqs);

    var faqWithTranslationsResponse = faqService.findAllPaged(pageable);

    assertThat(faqWithTranslationsResponse).isNotNull();
    assertThat(faqWithTranslationsResponse.getTotalElements()).isEqualTo(faqs.getTotalElements());
  }

  @Test
  void givenValidRequest_whenFindById_thenSuccess() {
    var optionalFaq = Optional
        .of(Faq.builder().id(1L).question("question").answer("answer").build());
    when(faqRepository.findById(1L)).thenReturn(optionalFaq);

    var faq = faqService.findById(1L);

    assertThat(faq).isNotNull();
    assertThat(faq.getId()).isEqualTo(optionalFaq.get().getId());
    assertThat(faq.getQuestion()).isEqualTo(optionalFaq.get().getQuestion());
    assertThat(faq.getAnswer()).isEqualTo(optionalFaq.get().getAnswer());
  }

  @Test
  void givenRequestWithNotExistingId_whenFindById_thenThrowIllegalStateException() {
    when(faqRepository.findById(1L)).thenThrow(new IllegalStateException());

    assertThrows(IllegalStateException.class, () -> faqService.findById(1L));
  }

  @Test
  void givenValidRequest_whenCreate_thenSuccess() {
    var createFaqRequest = CreateFaqRequest.builder().questionEn("question")
        .questionRu("question").questionBe("question").answerBe("answer").answerEn("answer")
        .answerRu("answer").build();
    when(faqRepository.findByQuestion(createFaqRequest.getQuestionEn()))
        .thenReturn(Optional.empty());
    when(faqRepository.save(any(Faq.class))).thenReturn(new Faq());
    doNothing().when(faqTranslationService).create(any(), any());

    assertDoesNotThrow(() -> faqService.create(createFaqRequest));
  }

  @Test
  void givenRequestWithExistingQuestion_whenCreate_thenThrowEntityAlreadyExistsException() {
    var createFaqRequest = CreateFaqRequest.builder().questionEn("question")
        .questionRu("question").questionBe("question").answerBe("answer").answerEn("answer")
        .answerRu("answer").build();
    when(faqRepository.findByQuestion(createFaqRequest.getQuestionEn()))
        .thenReturn(Optional.of(new Faq()));

    assertThrows(EntityAlreadyExistsException.class, () -> faqService.create(createFaqRequest));
  }

  @Test
  void givenValidRequest_whenUpdate_thenSuccess() {
    var updateFaqRequest = UpdateFaqRequest.builder().questionEn("question")
        .questionRu("question").questionBe("question").answerBe("answer").answerEn("answer")
        .answerRu("answer").build();
    var optionalFaq = Optional
        .of(Faq.builder().id(1L).question("oldQuestion").answer("oldAnswer").build());
    when(faqRepository.findById(1L)).thenReturn(optionalFaq);
    when(faqRepository.save(any(Faq.class))).thenReturn(new Faq());
    doNothing().when(faqTranslationService).create(any(), any());

    faqService.update(1L, updateFaqRequest);

    assertThat(optionalFaq.get().getQuestion()).isEqualTo(updateFaqRequest.getQuestionEn());
    assertThat(optionalFaq.get().getAnswer()).isEqualTo(updateFaqRequest.getAnswerEn());
  }

  @Test
  void givenValidRequest_whenDelete_thenSuccess() {
    var optionalFaq = Optional
        .of(Faq.builder().id(1L).question("oldQuestion").answer("oldAnswer").build());
    when(faqRepository.findById(1L)).thenReturn(optionalFaq);
    doNothing().when(faqRepository).delete(any(Faq.class));

    assertDoesNotThrow(() -> faqService.delete(1L));
  }
}