package com.example.carrental.controller.dto.faq;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for display FAQ with translations.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaqWithTranslationsResponse {

  @NotNull
  @Positive
  private long id;
  @NotNull
  @Size(
      min = 1,
      max = 2024
  )
  private String questionEn;
  @NotNull
  @Size(
      min = 1,
      max = 2024
  )
  private String answerEn;
  @NotNull
  @Size(
      min = 1,
      max = 2024
  )
  private String questionRu;
  @NotNull
  @Size(
      min = 1,
      max = 2024
  )
  private String answerRu;
  @NotNull
  @Size(
      min = 1,
      max = 2024
  )
  private String questionBe;
  @NotNull
  @Size(
      min = 1,
      max = 2024
  )
  private String answerBe;
}
