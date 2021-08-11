package com.example.carrental.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchRequest {

  private int pageNumber = 0;
  private int pageSize = 10;
  private String sortDirection = "asc";
  private String sortBy = "id";
  private String email;
}
