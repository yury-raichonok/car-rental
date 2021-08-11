package com.example.carrental.controller.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewMessageResponse {

  private Long id;
  private String name;
  private String email;
  private String phone;
  private String message;
  private String sentDate;
  private String isRead;
}
