package com.example.carrental.controller.dto.order;

import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for reject Order.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRejectRequest {

  @Size(
      min = 1,
      max = 255
  )
  private String comments;
}
