package com.example.carrental.controller.dto.faq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaqWithTranslationsResponse {

  private Long id;
  private String questionEn;
  private String answerEn;
  private String questionRu;
  private String answerRu;
  private String questionBe;
  private String answerBe;
}
