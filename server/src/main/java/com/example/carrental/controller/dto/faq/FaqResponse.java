package com.example.carrental.controller.dto.faq;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for display FAQ.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaqResponse {

  @NotNull
  @Positive
  private long id;
  @NotNull
  @Size(
      min = 1,
      max = 2024
  )
  private String question;
  @NotNull
  @Size(
      min = 1,
      max = 2024
  )
  private String answer;
}
